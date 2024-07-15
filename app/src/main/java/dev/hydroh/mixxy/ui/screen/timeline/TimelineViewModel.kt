package dev.hydroh.mixxy.ui.screen.timeline

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import arrow.core.raise.either
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.hydroh.mixxy.data.InstanceRepository
import dev.hydroh.mixxy.data.NotesRepository
import dev.hydroh.mixxy.data.remote.model.Note
import dev.hydroh.mixxy.ui.enum.LoadingState
import dev.hydroh.mixxy.util.cachedPager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class TimelineViewModel @Inject constructor(
    private val notesRepository: NotesRepository,
    private val instanceRepository: InstanceRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(NotesUIState())
    val uiState = _uiState.asStateFlow()

    val emojis = instanceRepository.emojis

    val homeTimeline = notesRepository.pagingFlow(Timeline.HOME)
        .cachedIn(viewModelScope).cachedPager()

    val localTimeline = notesRepository.pagingFlow(Timeline.LOCAL)
        .cachedIn(viewModelScope).cachedPager()

    val hybridTimeline = notesRepository.pagingFlow(Timeline.HYBRID)
        .cachedIn(viewModelScope).cachedPager()

    val globalTimeline = notesRepository.pagingFlow(Timeline.GLOBAL)
        .cachedIn(viewModelScope).cachedPager()

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

    suspend fun createReaction(note: Note, reaction: String) = either<Throwable, Unit> {
        if (note.myReaction != null) {
            deleteReaction(note).bind()
        }
        return notesRepository.createReaction(note.id, reaction).map {
            val newNote = note.copy(
                myReaction = reaction,
                reactions = note.reactions.toMutableMap().apply {
                    this[reaction] = (this[reaction] ?: 0) + 1
                }
            )
            homeTimeline.update(newNote)
            localTimeline.update(newNote)
            hybridTimeline.update(newNote)
            globalTimeline.update(newNote)
        }
    }

    suspend fun deleteReaction(note: Note) =
        notesRepository.deleteReaction(note.id).map {
            val oldReaction = note.myReaction
            val newNote = note.copy(
                myReaction = null,
                reactions = note.reactions.toMutableMap().apply {
                    if (oldReaction != null && oldReaction in this) {
                        this[oldReaction] = this[oldReaction]!! - 1
                    }
                }
            )
            homeTimeline.update(newNote)
            localTimeline.update(newNote)
            hybridTimeline.update(newNote)
            globalTimeline.update(newNote)
        }

    suspend fun createNote(text: String, replyID: String?, renoteID: String?) =
        notesRepository.createNote(text = text, replyId = replyID, renoteId = renoteID)

    suspend fun renote(noteID: String) =
        notesRepository.createNote(text = null, renoteId = noteID)
}

data class NotesUIState(
    val loadingState: LoadingState = LoadingState.INIT,
    val errorMessage: String? = null,
)

enum class Timeline {
    HOME,
    LOCAL,
    HYBRID,
    GLOBAL,
}
