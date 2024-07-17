package dev.hydroh.mixxy.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import dev.hydroh.mixxy.data.local.dao.NoteDao
import dev.hydroh.mixxy.data.local.model.NoteJson
import dev.hydroh.mixxy.data.local.model.Timeline
import dev.hydroh.mixxy.data.local.model.toNoteJson
import dev.hydroh.mixxy.data.remote.NoteService
import dev.hydroh.mixxy.data.remote.model.request.NotesReq

@OptIn(ExperimentalPagingApi::class)
class NotesRemoteMediator(
    val timeline: Timeline,
    private val noteDao: NoteDao,
    private val noteService: NoteService,
) : RemoteMediator<Int, NoteJson>() {
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, NoteJson>
    ): MediatorResult {
        return try {
            val loadKey = when (loadType) {
                LoadType.REFRESH -> null
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> state.lastItemOrNull()?.id
                    ?: return MediatorResult.Success(endOfPaginationReached = true)
            }
            val loadSize =
                if (loadType == LoadType.REFRESH) state.config.initialLoadSize
                else state.config.pageSize
            val notes = when (timeline) {
                Timeline.HOME -> noteService.timeline(
                    NotesReq.HomeTimeline(
                        untilId = loadKey,
                        limit = loadSize,
                    )
                )

                Timeline.LOCAL -> noteService.localTimeline(
                    NotesReq.Timeline(
                        untilId = loadKey,
                        limit = loadSize,
                    )
                )

                Timeline.HYBRID -> noteService.hybridTimeline(
                    NotesReq.Timeline(
                        untilId = loadKey,
                        limit = loadSize,
                    )
                )

                Timeline.GLOBAL -> noteService.globalTimeline(
                    NotesReq.Timeline(
                        untilId = loadKey,
                        limit = loadSize,
                    )
                )
            }.fold({ throw it }, { it })
            if (loadType == LoadType.REFRESH) {
                noteDao.replaceNotes(notes.map { it.toNoteJson() }, timeline)
            } else {
                noteDao.insertNotes(notes.map { it.toNoteJson() }, timeline)
            }
            MediatorResult.Success(endOfPaginationReached = notes.isEmpty())
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }
}