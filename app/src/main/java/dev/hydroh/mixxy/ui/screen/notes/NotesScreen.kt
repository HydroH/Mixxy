package dev.hydroh.mixxy.ui.screen.notes

import androidx.compose.foundation.layout.fillMaxWidth
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
    val globalTimeline = viewModel.globalTimeline.collectAsLazyPagingItems()

    NoteItemList(notes = globalTimeline, modifier = Modifier.fillMaxWidth())
}
