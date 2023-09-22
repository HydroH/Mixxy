package dev.hydroh.mixxy.ui.screen.timeline

import android.view.Gravity
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.DialogWindowProvider
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import dev.hydroh.mixxy.ui.components.EmojiSelectionGrid
import dev.hydroh.mixxy.ui.components.NoteEditor
import dev.hydroh.mixxy.ui.components.NoteItem
import dev.hydroh.mixxy.util.pagerTabIndicatorOffset
import kotlinx.collections.immutable.persistentMapOf
import kotlinx.coroutines.launch

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
    val coroutineScope = rememberCoroutineScope()

    Column(modifier = Modifier.fillMaxWidth()) {
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
        HorizontalPager(
            state = pagerState,
        ) { page ->
            val timeline = when (viewModel.tabs[page].timeline) {
                Timeline.HOME -> viewModel.homeTimeline
                Timeline.LOCAL -> viewModel.localTimeline
                Timeline.HYBRID -> viewModel.hybridTimeline
                Timeline.GLOBAL -> viewModel.globalTimeline
            }
            val pagingItems = timeline.pager.collectAsLazyPagingItems()

            SwipeRefresh(
                state = rememberSwipeRefreshState(isRefreshing = pagingItems.loadState.refresh is LoadState.Loading),
                onRefresh = {
                    pagingItems.refresh()
                    timeline.invalidate()
                }
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(12.dp),
                ) {
                    items(
                        count = pagingItems.itemCount,
                        key = { index -> pagingItems[index]?.id ?: "" },
                    ) { index ->
                        pagingItems[index]?.let {
                            NoteItem(
                                note = it,
                                onCreateReaction = viewModel::createReaction,
                                onDeleteReaction = viewModel::deleteReaction,
                                onClickReplyButton = {
                                    viewModel.updatePopupUIState(PopupUIState.Create("@${it.user.username} "))
                                    coroutineScope.launch { sheetState.show() }
                                },
                                onClickRenoteButton = {
                                    viewModel.updatePopupUIState(PopupUIState.Renote(it))
                                    coroutineScope.launch { sheetState.show() }
                                },
                                onClickReactionButton = {
                                    viewModel.updatePopupUIState(PopupUIState.Reaction(it))
                                    coroutineScope.launch { sheetState.show() }
                                },
                                emojis = emojisState,
                                modifier = when (index) {
                                    0 ->
                                        Modifier.clip(RoundedCornerShape(12.dp, 12.dp, 0.dp, 0.dp))

                                    pagingItems.itemCount ->
                                        Modifier.clip(RoundedCornerShape(0.dp, 0.dp, 12.dp, 12.dp))

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

    uiState.popupUIState?.let { popupUIState ->
        when (popupUIState) {
            is PopupUIState.Reaction -> {
                ModalBottomSheet(onDismissRequest = {
                    viewModel.updatePopupUIState(null)
                }) {
                    EmojiSelectionGrid(
                        emojis = emojisState,
                        onEmojiSelected = { emoji ->
                            coroutineScope.launch {
                                sheetState.hide()
                            }.invokeOnCompletion {
                                if (!sheetState.isVisible) {
                                    viewModel.updatePopupUIState(null)
                                }
                            }
                            viewModel.createReaction(popupUIState.note, emoji)
                        }
                    )
                }
            }

            is PopupUIState.Renote -> {
                ModalBottomSheet(onDismissRequest = {
                    viewModel.updatePopupUIState(null)
                }) {
                    Column {
                        Text(
                            text = "转发",
                            modifier = Modifier.clickable { viewModel.renote() }
                        )
                        Text(
                            text = "引用",
                            modifier = Modifier.clickable {
                                viewModel.updatePopupUIState(
                                    PopupUIState.Create("", renoteID = popupUIState.note.id)
                                )
                            }
                        )
                    }
                }
            }

            is PopupUIState.Create -> {
                BoxWithConstraints {
                    Dialog(
                        onDismissRequest = {
                            viewModel.updatePopupUIState(null)
                        },
                        properties = DialogProperties(
                            dismissOnBackPress = true,
                            dismissOnClickOutside = true,
                            usePlatformDefaultWidth = false,
                        ),
                    ) {
                        (LocalView.current.parent as DialogWindowProvider).window.setGravity(Gravity.TOP)
                        NoteEditor(
                            text = popupUIState.text,
                            onClickSubmit = { viewModel.createNote() },
                            onTextChange = {
                                viewModel.updatePopupUIState(
                                    PopupUIState.Create(
                                        it,
                                        replyID = popupUIState.replyID,
                                        renoteID = popupUIState.renoteID
                                    )
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(Dp.Hairline, maxHeight * 0.5f)
                                .padding(20.dp)
                        )
                    }
                }
            }
        }
    }
}
