package dev.hydroh.mixxy.ui.screen.notes

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@RootNavGraph(start = true)
@Destination
@Composable
fun NotesScreen(
    navigator: DestinationsNavigator? = null,
    viewModel: NotesViewModel = NotesViewModel(),
) {

}

@Preview
@Composable
fun NotesScreenPreview() {
    NotesScreen()
}