package dev.hydroh.mixxy.ui.screen.timeline

import androidx.lifecycle.ViewModel
import arrow.core.raise.either
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.hydroh.mixxy.data.InstanceRepository
import dev.hydroh.mixxy.data.NotesRepository
import dev.hydroh.mixxy.data.local.model.Timeline
import dev.hydroh.mixxy.data.remote.model.Note
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class TimelineViewModel @Inject constructor(
    private val notesRepository: NotesRepository,
    private val instanceRepository: InstanceRepository,
) : ViewModel() {
    private val _uiState: MutableStateFlow<NotesUIState> = MutableStateFlow(NotesUIState.Init)
    val uiState = _uiState.asStateFlow()

    val emojis = instanceRepository.emojis
    val homeTimeline = notesRepository.pager(Timeline.HOME)
    val localTimeline = notesRepository.pager(Timeline.LOCAL)
    val hybridTimeline = notesRepository.pager(Timeline.HYBRID)
    val globalTimeline = notesRepository.pager(Timeline.GLOBAL)

    val tabs = listOf(
        TabInfo(timeline = Timeline.HOME, title = "Home"),
        TabInfo(timeline = Timeline.LOCAL, title = "Local"),
        TabInfo(timeline = Timeline.HYBRID, title = "Hybrid"),
        TabInfo(timeline = Timeline.GLOBAL, title = "Global"),
    )

    data class TabInfo(
        val timeline: Timeline,
        val title: String,
    )

    suspend fun fetchEmojis() = instanceRepository.fetchEmojis()

    suspend fun createReaction(note: Note, reaction: String) = either {
        if (note.myReaction != null) {
            deleteReaction(note).bind()
        }
        notesRepository.createReaction(note.id, reaction).bind()
    }

    suspend fun deleteReaction(note: Note) =
        notesRepository.deleteReaction(note.id)

    suspend fun createNote(text: String, replyID: String?, renoteID: String?) =
        notesRepository.createNote(text = text, replyId = replyID, renoteId = renoteID)

    suspend fun renote(noteID: String) =
        notesRepository.createNote(text = null, renoteId = noteID)
}

sealed class NotesUIState {
    object Init: NotesUIState()
    object Loading: NotesUIState()
    data class Error(val message: String): NotesUIState()
}
