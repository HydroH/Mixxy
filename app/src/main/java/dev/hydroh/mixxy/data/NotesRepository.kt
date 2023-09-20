package dev.hydroh.mixxy.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import dev.hydroh.misskey.client.entity.request.NotesReq
import dev.hydroh.mixxy.data.remote.NotesPagingSource
import dev.hydroh.mixxy.ui.screen.timeline.Timeline
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotesRepository @Inject constructor(
    private val misskeyDataSource: MisskeyDataSource
) {
    companion object {
        const val PAGE_SIZE = 20
    }

    fun pagingFlow(timeline: Timeline) =
        Pager(PagingConfig(pageSize = PAGE_SIZE)) {
            NotesPagingSource(misskeyDataSource, timeline)
        }.flow

    suspend fun createReaction(noteId: String, reaction: String) =
        misskeyDataSource.client!!.notes.createReaction(NotesReq.CreateReaction(noteId, reaction))

    suspend fun deleteReaction(noteId: String) =
        misskeyDataSource.client!!.notes.deleteReaction(NotesReq.DeleteReaction(noteId))
}