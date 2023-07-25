package dev.hydroh.mixxy.ui.screen.notes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.hydroh.mixxy.data.remote.MisskeyDataSource
import dev.hydroh.mixxy.data.remote.NotesPagingSource
import dev.hydroh.mixxy.ui.components.LoadingState
import dev.hydroh.mixxy.util.cachedPager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
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

    val homeTimeline = Pager(PagingConfig(pageSize = PAGE_SIZE)) {
        NotesPagingSource(misskeyDataSource, NotesTimeline.HOME)
    }.flow.cachedIn(viewModelScope).cachedPager { it.id }

    val localTimeline = Pager(PagingConfig(pageSize = PAGE_SIZE)) {
        NotesPagingSource(misskeyDataSource, NotesTimeline.LOCAL)
    }.flow.cachedIn(viewModelScope).cachedPager { it.id }

    val hybridTimeline = Pager(PagingConfig(pageSize = PAGE_SIZE)) {
        NotesPagingSource(misskeyDataSource, NotesTimeline.HYBRID)
    }.flow.cachedIn(viewModelScope).cachedPager { it.id }

    val globalTimeline = Pager(PagingConfig(pageSize = PAGE_SIZE)) {
        NotesPagingSource(misskeyDataSource, NotesTimeline.GLOBAL)
    }.flow.cachedIn(viewModelScope).cachedPager { it.id }

    val tabs = listOf(
        TabInfo(timeline = NotesTimeline.HOME, title = "Home"),
        TabInfo(timeline = NotesTimeline.LOCAL, title = "Local"),
        TabInfo(timeline = NotesTimeline.HYBRID, title = "Hybrid"),
        TabInfo(timeline = NotesTimeline.GLOBAL, title = "Global"),
    )

    fun updateTabIndex(index: Int) {
        _uiState.update {
            it.copy(timelineIndex = index)
        }
    }

    data class TabInfo(
        val timeline: NotesTimeline,
        val title: String,
    )
}

data class NotesUIState(
    val timelineIndex: Int = 0,
    val loadingState: LoadingState = LoadingState.INIT,
    val errorMessage: String? = null,
)

enum class NotesTimeline {
    HOME,
    LOCAL,
    HYBRID,
    GLOBAL,
}