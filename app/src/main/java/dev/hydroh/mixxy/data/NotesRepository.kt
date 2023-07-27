package dev.hydroh.mixxy.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import dev.hydroh.mixxy.data.remote.MisskeyDataSource
import dev.hydroh.mixxy.data.remote.NotesPagingSource
import dev.hydroh.mixxy.ui.screen.notes.NotesTimeline
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotesRepository @Inject constructor(
    private val misskeyDataSource: MisskeyDataSource
) {
    companion object {
        const val PAGE_SIZE = 20
    }
    fun pagingFlow(timeline: NotesTimeline) =
        Pager(PagingConfig(pageSize = PAGE_SIZE)) {
            NotesPagingSource(misskeyDataSource, timeline)
        }.flow
}