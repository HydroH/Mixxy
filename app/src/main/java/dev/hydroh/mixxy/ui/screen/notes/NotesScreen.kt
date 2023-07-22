package dev.hydroh.mixxy.ui.screen.notes

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import dev.hydroh.mixxy.ui.components.NoteItemList

@RootNavGraph(start = true)
@Destination
@Composable
fun NotesScreen(
    navigator: DestinationsNavigator? = null,
    viewModel: NotesViewModel = NotesViewModel(),
) {
    val uiState = viewModel.uiState

    LaunchedEffect(Unit) {
        viewModel.loadNotes(NotesTimeline.GLOBAL)
    }

    NoteItemList(notes = uiState.globalNotes, modifier = Modifier.fillMaxWidth())
}

@Preview
@Composable
fun NotesScreenPreview() {
    NotesScreen()
}