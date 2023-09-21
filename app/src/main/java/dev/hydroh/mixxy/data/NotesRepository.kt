package dev.hydroh.mixxy.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import dev.hydroh.mixxy.data.remote.NotesService
import dev.hydroh.mixxy.data.remote.model.request.NotesReq
import dev.hydroh.mixxy.ui.screen.timeline.Timeline
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotesRepository @Inject constructor(
    private val notesService: NotesService,
) {
    companion object {
        const val PAGE_SIZE = 20
    }

    fun pagingFlow(timeline: Timeline) =
        Pager(PagingConfig(pageSize = PAGE_SIZE)) {
            NotesPagingSource(notesService, timeline)
        }.flow

    suspend fun createReaction(noteId: String, reaction: String) =
        notesService.createReaction(NotesReq.CreateReaction(noteId, reaction))

    suspend fun deleteReaction(noteId: String) =
        notesService.deleteReaction(NotesReq.DeleteReaction(noteId))
}
