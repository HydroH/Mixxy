package dev.hydroh.mixxy.ui.screen.notes

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.result.NavResult
import com.ramcosta.composedestinations.result.ResultRecipient
import dev.hydroh.mixxy.ui.components.NoteItemList
import dev.hydroh.mixxy.ui.screen.destinations.LoginScreenDestination
import dev.hydroh.mixxy.ui.screen.destinations.RedirectScreenDestination

@Destination
@Composable
fun NotesScreen(
    navigator: DestinationsNavigator? = null,
    resultRecipient: ResultRecipient<RedirectScreenDestination, Boolean>? = null,
    notesViewModel: NotesViewModel = viewModel(),
) {
    val uiState by notesViewModel.uiState.collectAsState()
    val globalTimeline = notesViewModel.globalTimeline.collectAsLazyPagingItems()

    resultRecipient?.onNavResult { result ->
        when (result) {
            is NavResult.Value -> {
                if (result.value) globalTimeline.refresh()
            }
            else -> {}
        }
    }
    LaunchedEffect(Unit) {
        // if has client load else nav to login
        navigator?.navigate(LoginScreenDestination)
    }

    NoteItemList(notes = globalTimeline, modifier = Modifier.fillMaxWidth())
}

@Preview
@Composable
fun NotesScreenPreview() {
    NotesScreen()
}