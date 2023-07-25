package dev.hydroh.mixxy.ui.screen.notes

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import dev.hydroh.mixxy.ui.components.NoteItemList
import dev.hydroh.mixxy.util.pagerTabIndicatorOffset
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Destination
@Composable
fun NotesScreen(
    navigator: DestinationsNavigator? = null,
    viewModel: NotesViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()

    val homeTimeline = viewModel.homeTimeline.pager.collectAsLazyPagingItems()
    val localTimeline = viewModel.localTimeline.pager.collectAsLazyPagingItems()
    val hybridTimeline = viewModel.hybridTimeline.pager.collectAsLazyPagingItems()
    val globalTimeline = viewModel.globalTimeline.pager.collectAsLazyPagingItems()

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
            beyondBoundsPageCount = 1,
        )
        { page ->
            // TODO: Disable preloading.
            NoteItemList(
                notes = when (viewModel.tabs[page].timeline) {
                    NotesTimeline.HOME -> homeTimeline
                    NotesTimeline.LOCAL -> localTimeline
                    NotesTimeline.HYBRID -> hybridTimeline
                    NotesTimeline.GLOBAL -> globalTimeline
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }

}
