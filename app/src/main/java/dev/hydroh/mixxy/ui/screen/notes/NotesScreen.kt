package dev.hydroh.mixxy.ui.screen.notes

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import dev.hydroh.mixxy.ui.components.NoteItemList

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

    TabRow(selectedTabIndex = uiState.timelineIndex) {
        viewModel.tabs.forEachIndexed { index, tab ->
            Tab(
                selected = uiState.timelineIndex == index,
                onClick = { viewModel.updateTabIndex(index) },
                text = { Text(tab.title) },
            )
        }

        NoteItemList(
            notes = when (viewModel.tabs[uiState.timelineIndex].timeline) {
                NotesTimeline.HOME -> homeTimeline
                NotesTimeline.LOCAL -> localTimeline
                NotesTimeline.HYBRID -> hybridTimeline
                NotesTimeline.GLOBAL -> globalTimeline
            },
            modifier = Modifier.fillMaxWidth()
        )
    }

}
