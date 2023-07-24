package dev.hydroh.mixxy.ui.screen.notes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.paging.map
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.hydroh.misskey.client.entity.Note
import dev.hydroh.mixxy.data.remote.MisskeyDataSource
import dev.hydroh.mixxy.data.remote.NotesPagingSource
import dev.hydroh.mixxy.ui.components.LoadingState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor(
    private val misskeyDataSource: MisskeyDataSource,
) : ViewModel() {
    private val _uiState = MutableStateFlow(NotesUIState())
    val uiState = _uiState.asStateFlow()

    companion object {
        const val PAGE_SIZE = 20
    }

    private val localNotes = MutableStateFlow(listOf<Note>())

    val homeTimeline = Pager(PagingConfig(pageSize = PAGE_SIZE)) {
        NotesPagingSource(misskeyDataSource, NotesTimeline.HOME)
    }.flow.cachedIn(viewModelScope)
        .combine(localNotes) { paging, local ->
            paging.map { pagingItem ->
                local.find { it.id == pagingItem.id } ?: pagingItem
            }
        }

    val localTimeline = Pager(PagingConfig(pageSize = PAGE_SIZE)) {
        NotesPagingSource(misskeyDataSource, NotesTimeline.LOCAL)
    }.flow.cachedIn(viewModelScope)
        .combine(localNotes) { paging, local ->
            paging.map { pagingItem ->
                local.find { it.id == pagingItem.id } ?: pagingItem
            }
        }

    val hybridTimeline = Pager(PagingConfig(pageSize = PAGE_SIZE)) {
        NotesPagingSource(misskeyDataSource, NotesTimeline.HYBRID)
    }.flow.cachedIn(viewModelScope)
        .combine(localNotes) { paging, local ->
            paging.map { pagingItem ->
                local.find { it.id == pagingItem.id } ?: pagingItem
            }
        }

    val globalTimeline = Pager(PagingConfig(pageSize = PAGE_SIZE)) {
        NotesPagingSource(misskeyDataSource, NotesTimeline.GLOBAL)
    }.flow.cachedIn(viewModelScope)
        .combine(localNotes) { paging, local ->
            paging.map { pagingItem ->
                local.find { it.id == pagingItem.id } ?: pagingItem
            }
        }

    fun updateNote(note: Note) {
        localNotes.value = localNotes.value.filterNot { it.id == note.id } + note
    }
}

data class NotesUIState(
    val currentTimeline: NotesTimeline = NotesTimeline.GLOBAL,
    val loadingState: LoadingState = LoadingState.INIT,
    val errorMessage: String? = null,
)

enum class NotesTimeline {
    HOME,
    LOCAL,
    HYBRID,
    GLOBAL,
}