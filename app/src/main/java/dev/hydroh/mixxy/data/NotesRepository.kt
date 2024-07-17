package dev.hydroh.mixxy.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.map
import dev.hydroh.mixxy.data.local.dao.NoteDao
import dev.hydroh.mixxy.data.local.model.Timeline
import dev.hydroh.mixxy.data.local.model.toNote
import dev.hydroh.mixxy.data.remote.NoteService
import dev.hydroh.mixxy.data.remote.model.request.NotesReq
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotesRepository @Inject constructor(
    private val noteDao: NoteDao,
    private val noteService: NoteService,
) {
    companion object {
        const val PAGE_SIZE = 20
    }

    @OptIn(ExperimentalPagingApi::class)
    fun pager(timeline: Timeline) =
        Pager(
            config = PagingConfig(pageSize = PAGE_SIZE),
            remoteMediator = NotesRemoteMediator(timeline, noteDao, noteService),
        ) {
            noteDao.getNotes(timeline)
        }.flow.map {
            it.map { it.toNote() }
        }

    suspend fun createReaction(noteId: String, reaction: String) =
        noteService.createReaction(NotesReq.CreateReaction(noteId, reaction))

    suspend fun deleteReaction(noteId: String) =
        noteService.deleteReaction(NotesReq.DeleteReaction(noteId))

    suspend fun createNote(text: String?, replyId: String? = null, renoteId: String? = null) =
        noteService.create(NotesReq.Create(text = text, replyId = replyId, renoteId = renoteId))
}
