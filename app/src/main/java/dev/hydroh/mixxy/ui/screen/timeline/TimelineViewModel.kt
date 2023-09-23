package dev.hydroh.mixxy.ui.screen.timeline

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.hydroh.mixxy.data.InstanceRepository
import dev.hydroh.mixxy.data.NotesRepository
import dev.hydroh.mixxy.data.remote.model.Note
import dev.hydroh.mixxy.ui.enums.LoadingState
import dev.hydroh.mixxy.util.cachedPager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TimelineViewModel @Inject constructor(
    private val notesRepository: NotesRepository,
    private val instanceRepository: InstanceRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(NotesUIState())
    val uiState = _uiState.asStateFlow()

    val emojis = instanceRepository.getEmojis()

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

    fun createReaction(note: Note, reaction: String) {
        if (note.myReaction != null) {
            deleteReaction(note)
        }
        viewModelScope.launch(Dispatchers.IO) {
            notesRepository.createReaction(note.id, reaction)
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

    fun deleteReaction(note: Note) {
        viewModelScope.launch(Dispatchers.IO) {
            notesRepository.deleteReaction(note.id)
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
    }

    fun updatePopupUIState(popupUIState: PopupUIState?) {
        _uiState.update { it.copy(popupUIState = popupUIState) }
    }

    fun createNote() {
        _uiState.value.popupUIState.apply {
            if (this !is PopupUIState.Create) return
            viewModelScope.launch(Dispatchers.IO) {
                notesRepository.createNote(text = text, replyId = replyID, renoteId = renoteID)
                    .map {
                        updatePopupUIState(null)
                    }
            }
        }
    }

    fun renote() {
        _uiState.value.popupUIState.apply {
            if (this !is PopupUIState.Renote) return
            viewModelScope.launch(Dispatchers.IO) {
                notesRepository.createNote(text = null, renoteId = note.id)
                updatePopupUIState(null)
            }
        }
    }
}

data class NotesUIState(
    val popupUIState: PopupUIState? = null,
    val loadingState: LoadingState = LoadingState.INIT,
    val errorMessage: String? = null,
)

enum class Timeline {
    HOME,
    LOCAL,
    HYBRID,
    GLOBAL,
}

sealed class PopupUIState {
    class Create(val text: String, val replyID: String? = null, val renoteID: String? = null) :
        PopupUIState()

    class Renote(val note: Note) : PopupUIState()
    class Reaction(val note: Note) : PopupUIState()
}
