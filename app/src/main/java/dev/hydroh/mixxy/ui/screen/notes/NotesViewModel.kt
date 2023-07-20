package dev.hydroh.mixxy.ui.screen.notes

import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import dev.hydroh.misskey.client.entity.Note

class NotesViewModel: ViewModel() {
}

data class NotesUIState(
    var notes: SnapshotStateList<Note>
)