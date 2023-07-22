package dev.hydroh.mixxy.ui.screen.notes

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.hydroh.misskey.client.entity.Note
import dev.hydroh.misskey.client.entity.request.NotesReq
import dev.hydroh.mixxy.data.DataProvider
import dev.hydroh.mixxy.ui.components.LoadingState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NotesViewModel : ViewModel() {
    var uiState by mutableStateOf(NotesUIState())
        private set

    fun loadNotes(timeline: NotesTimeline) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                when (timeline) {
                    NotesTimeline.HOME -> {
                        val notes =
                            DataProvider.misskeyClient.notes.timeline(NotesReq.HomeTimeline())
                        uiState = uiState.copy(homeNotes = notes.toMutableStateList())
                    }

                    NotesTimeline.LOCAL -> {
                        val notes =
                            DataProvider.misskeyClient.notes.localTimeline(NotesReq.Timeline())
                        uiState = uiState.copy(localNotes = notes.toMutableStateList())
                    }

                    NotesTimeline.HYBRID -> {
                        val notes =
                            DataProvider.misskeyClient.notes.hybridTimeline(NotesReq.Timeline())
                        uiState = uiState.copy(hybridNotes = notes.toMutableStateList())
                    }

                    NotesTimeline.GLOBAL -> {
                        val notes =
                            DataProvider.misskeyClient.notes.globalTimeline(NotesReq.Timeline())
                        uiState = uiState.copy(globalNotes = notes.toMutableStateList())
                    }
                }
                uiState = uiState.copy(
                    loadingState = LoadingState.SUCCESS,
                    errorMessage = null,
                )
            } catch (e: Exception) {
                uiState = uiState.copy(
                    loadingState = LoadingState.FAIL,
                    errorMessage = e.message,
                )
            }
        }
    }
}

data class NotesUIState(
    var homeNotes: SnapshotStateList<Note> = mutableStateListOf(),
    var localNotes: SnapshotStateList<Note> = mutableStateListOf(),
    var hybridNotes: SnapshotStateList<Note> = mutableStateListOf(),
    var globalNotes: SnapshotStateList<Note> = mutableStateListOf(),
    val loadingState: LoadingState = LoadingState.LOADING,
    val errorMessage: String? = null,
)

enum class NotesTimeline {
    HOME,
    LOCAL,
    HYBRID,
    GLOBAL,
}