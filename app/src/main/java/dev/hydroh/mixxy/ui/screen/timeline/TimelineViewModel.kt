package dev.hydroh.mixxy.ui.screen.timeline

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.hydroh.misskey.client.entity.Note
import dev.hydroh.mixxy.data.InstanceRepository
import dev.hydroh.mixxy.data.NotesRepository
import dev.hydroh.mixxy.ui.components.LoadingState
import dev.hydroh.mixxy.util.cachedPager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
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
        .cachedIn(viewModelScope).cachedPager { it.id }

    val localTimeline = notesRepository.pagingFlow(Timeline.LOCAL)
        .cachedIn(viewModelScope).cachedPager { it.id }

    val hybridTimeline = notesRepository.pagingFlow(Timeline.HYBRID)
        .cachedIn(viewModelScope).cachedPager { it.id }

    val globalTimeline = notesRepository.pagingFlow(Timeline.GLOBAL)
        .cachedIn(viewModelScope).cachedPager { it.id }

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
        viewModelScope.launch(Dispatchers.IO) {
            notesRepository.createReaction(note.id, reaction)
            val oldReaction = note.myReaction
            val newNote = note.copy(
                myReaction = reaction,
                reactions = note.reactions.toMutableMap().apply {
                    if (oldReaction != null && oldReaction in this) {
                        this[oldReaction] = this[oldReaction]!! - 1
                    }
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