package dev.hydroh.mixxy.ui.screen.timeline

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.hydroh.misskey.client.entity.Note
import dev.hydroh.mixxy.data.InstanceRepository
import dev.hydroh.mixxy.data.NotesRepository
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

    fun getEmojiMap() = instanceRepository.emojiMap

    fun updateEmojis(emojis: List<String>) {
        viewModelScope.launch(Dispatchers.IO) {
            instanceRepository.updateEmojis(emojis)
        }
    }

    fun updateRespondUIState(respondUIState: RespondUIState?) {
        _uiState.update { it.copy(respondUIState = respondUIState) }
    }
}

data class NotesUIState(
    val respondUIState: RespondUIState? = null,
    val loadingState: LoadingState = LoadingState.INIT,
    val errorMessage: String? = null,
)

enum class Timeline {
    HOME,
    LOCAL,
    HYBRID,
    GLOBAL,
}

sealed class RespondUIState {
    class Reply(val text: String) : RespondUIState()
    class Renote(val note: Note) : RespondUIState()
    class Reaction(val note: Note) : RespondUIState()
}
