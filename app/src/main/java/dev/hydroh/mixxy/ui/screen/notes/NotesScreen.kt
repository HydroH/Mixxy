package dev.hydroh.mixxy.ui.screen.notes

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import dev.hydroh.mixxy.ui.components.NoteItemList
import dev.hydroh.mixxy.util.pagerTabIndicatorOffset
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterialApi::class)
@Destination
@Composable
fun NotesScreen(
    navigator: DestinationsNavigator? = null,
    viewModel: NotesViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()

    val pagerState = rememberPagerState()
    val coroutineScope = rememberCoroutineScope()

    Column(modifier = Modifier.fillMaxWidth()) {
        TabRow(
            selectedTabIndex = pagerState.currentPage,
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
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
            pageCount = viewModel.tabs.count(),
            state = pagerState,
        )
        { page ->
            val timeline = when (viewModel.tabs[page].timeline) {
                NotesTimeline.HOME -> viewModel.homeTimeline
                NotesTimeline.LOCAL -> viewModel.localTimeline
                NotesTimeline.HYBRID -> viewModel.hybridTimeline
                NotesTimeline.GLOBAL -> viewModel.globalTimeline
            }
            val pagingItems = timeline.pager.collectAsLazyPagingItems()
            val pullRefreshState = rememberPullRefreshState(
                refreshing = pagingItems.loadState.refresh is LoadState.Loading,
                onRefresh = {
                    pagingItems.refresh()
                    timeline.invalidate()
                })
            Box(
                modifier = Modifier.pullRefresh(pullRefreshState)
            ) {
                NoteItemList(
                    notes = pagingItems,
                    emojiMap = viewModel.getEmojiMap(),
                    updateEmojis = viewModel::updateEmojis,
                    modifier = Modifier.fillMaxWidth(),
                    prefixId = viewModel.tabs[page].timeline.name,
                )
                PullRefreshIndicator(
                    refreshing = pagingItems.loadState.refresh is LoadState.Loading,
                    state = pullRefreshState,
                    modifier = Modifier.align(Alignment.TopCenter))
            }
        }
    }
}
