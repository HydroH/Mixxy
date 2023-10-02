package dev.hydroh.mixxy.ui.screen.timeline

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import dev.hydroh.mixxy.ui.components.NoteAction
import dev.hydroh.mixxy.ui.components.NoteActionDialog
import dev.hydroh.mixxy.ui.components.NoteActionDialogState
import dev.hydroh.mixxy.ui.components.NoteItem
import dev.hydroh.mixxy.util.pagerTabIndicatorOffset
import kotlinx.collections.immutable.persistentMapOf
import kotlinx.coroutines.launch

// Use accompanist swiperefresh until it's added to material3
@Suppress("DEPRECATION")
@OptIn(
    ExperimentalFoundationApi::class,
    ExperimentalMaterial3Api::class
)
@Destination
@Composable
fun TimelineScreen(
    navigator: DestinationsNavigator? = null,
    viewModel: TimelineViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    val emojisState by viewModel.emojis.collectAsState(persistentMapOf())

    val sheetState = rememberModalBottomSheetState()
    val pagerState = rememberPagerState { viewModel.tabs.count() }
    val noteActionDialogState = remember { NoteActionDialogState() }
    val coroutineScope = rememberCoroutineScope()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(
        rememberTopAppBarState()
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    TabRow(
                        selectedTabIndex = pagerState.currentPage,
                        indicator = { tabPositions ->
                            TabRowDefaults.PrimaryIndicator(
                                Modifier.pagerTabIndicatorOffset(pagerState, tabPositions)
                            )
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        viewModel.tabs.forEachIndexed { index, tab ->
                            Tab(
                                selected = pagerState.currentPage == index,
                                onClick = {
                                    coroutineScope.launch { pagerState.animateScrollToPage(index) }
                                },
                                text = { Text(tab.title) },
                            )
                        }
                    }
                },
                scrollBehavior = scrollBehavior
            )
        }
    ) { innerPadding ->
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxWidth()
        ) { page ->
            val timeline = when (viewModel.tabs[page].timeline) {
                Timeline.HOME -> viewModel.homeTimeline
                Timeline.LOCAL -> viewModel.localTimeline
                Timeline.HYBRID -> viewModel.hybridTimeline
                Timeline.GLOBAL -> viewModel.globalTimeline
            }
            val pagingItems = timeline.pager.collectAsLazyPagingItems()

            SwipeRefresh(
                state = rememberSwipeRefreshState(
                    isRefreshing = pagingItems.loadState.refresh is LoadState.Loading
                ),
                onRefresh = {
                    pagingItems.refresh()
                    timeline.invalidate()
                }
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .nestedScroll(scrollBehavior.nestedScrollConnection),
                    contentPadding = PaddingValues(12.dp),
                ) {
                    items(
                        count = pagingItems.itemCount,
                        key = { index -> pagingItems[index]?.id ?: "" },
                    ) { index ->
                        pagingItems[index]?.let {
                            NoteItem(
                                note = it,
                                onCreateReaction = { note, reaction ->
                                    coroutineScope.launch {
                                        viewModel.createReaction(note, reaction)
                                    }
                                },
                                onDeleteReaction = { note ->
                                    coroutineScope.launch {
                                        viewModel.deleteReaction(note)
                                    }
                                },
                                onClickReplyButton = {
                                    noteActionDialogState.noteAction =
                                        NoteAction.Create(
                                            "@${it.user.username} ",
                                            replyID = it.id
                                        )
                                },
                                onClickRenoteButton = {
                                    noteActionDialogState.noteAction = NoteAction.Renote(it)
                                    coroutineScope.launch { sheetState.show() }
                                },
                                onClickReactionButton = {
                                    noteActionDialogState.noteAction = NoteAction.Reaction(it)
                                    coroutineScope.launch { sheetState.show() }
                                },
                                emojis = emojisState,
                                modifier = when (index) {
                                    0 ->
                                        Modifier.clip(
                                            RoundedCornerShape(
                                                12.dp,
                                                12.dp,
                                                0.dp,
                                                0.dp
                                            )
                                        )

                                    pagingItems.itemCount ->
                                        Modifier.clip(
                                            RoundedCornerShape(
                                                0.dp,
                                                0.dp,
                                                12.dp,
                                                12.dp
                                            )
                                        )

                                    else -> Modifier
                                }
                                    .background(MaterialTheme.colorScheme.surface)
                                    .padding(16.dp)
                            )
                            HorizontalDivider()
                        }
                    }
                }
            }
        }
    }

    NoteActionDialog(
        noteActionDialogState = noteActionDialogState,
        sheetState = sheetState,
        emojis = emojisState,
        onCreateNote = { text, replyID, renoteID ->
            coroutineScope.launch {
                viewModel.createNote(text, replyID, renoteID).map {
                    noteActionDialogState.noteAction = null
                }
            }
        },
        onRenote = { noteId ->
            coroutineScope.launch { viewModel.renote(noteId) }
            coroutineScope.launch {
                sheetState.hide()
            }.invokeOnCompletion {
                if (!sheetState.isVisible) noteActionDialogState.noteAction = null
            }
        },
        onReaction = { note, reaction ->
            coroutineScope.launch { viewModel.createReaction(note, reaction) }
            coroutineScope.launch {
                sheetState.hide()
            }.invokeOnCompletion {
                if (!sheetState.isVisible) noteActionDialogState.noteAction = null
            }
        }
    )
}
