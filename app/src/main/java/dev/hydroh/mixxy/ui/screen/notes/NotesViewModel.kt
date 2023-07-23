package dev.hydroh.mixxy.ui.screen.notes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.hydroh.mixxy.data.local.UserDataStore
import dev.hydroh.mixxy.data.remote.MisskeyDataSource
import dev.hydroh.mixxy.data.remote.NotesPagingSource
import dev.hydroh.mixxy.ui.components.LoadingState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor(
    private val misskeyDataSource: MisskeyDataSource,
    private val userDataStore: UserDataStore
) : ViewModel() {
    private val _uiState = MutableStateFlow(NotesUIState())
    val uiState = _uiState.asStateFlow()

    companion object {
        val PAGE_SIZE = 20
    }

    val homeTimeline = Pager(PagingConfig(pageSize = PAGE_SIZE)) {
        NotesPagingSource(misskeyDataSource, NotesTimeline.HOME)
    }.flow.cachedIn(viewModelScope)

    val localTimeline = Pager(PagingConfig(pageSize = PAGE_SIZE)) {
        NotesPagingSource(misskeyDataSource, NotesTimeline.LOCAL)
    }.flow.cachedIn(viewModelScope)

    val hybridTimeline = Pager(PagingConfig(pageSize = PAGE_SIZE)) {
        NotesPagingSource(misskeyDataSource, NotesTimeline.HYBRID)
    }.flow.cachedIn(viewModelScope)

    val globalTimeline = Pager(PagingConfig(pageSize = PAGE_SIZE)) {
        NotesPagingSource(misskeyDataSource, NotesTimeline.GLOBAL)
    }.flow.cachedIn(viewModelScope)
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