package dev.hydroh.mixxy.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import dev.hydroh.mixxy.data.remote.NotesService
import dev.hydroh.mixxy.data.remote.model.Note
import dev.hydroh.mixxy.data.remote.model.request.NotesReq
import dev.hydroh.mixxy.ui.screen.timeline.Timeline

class NotesPagingSource(
    private val notesService: NotesService,
    private val timeline: Timeline,
) : PagingSource<String, Note>() {
    override suspend fun load(params: LoadParams<String>): LoadResult<String, Note> {
        // Disable prepending
        if (params is LoadParams.Prepend) {
            return LoadResult.Page(
                data = listOf(),
                prevKey = null,
                nextKey = null,
            )
        }

        val untilId = params.key
        return when (timeline) {
            Timeline.HOME -> notesService.timeline(
                NotesReq.HomeTimeline(
                    untilId = untilId,
                    limit = params.loadSize
                )
            )

            Timeline.LOCAL -> notesService.localTimeline(
                NotesReq.Timeline(
                    untilId = untilId,
                    limit = params.loadSize
                )
            )

            Timeline.HYBRID -> notesService.hybridTimeline(
                NotesReq.Timeline(
                    untilId = untilId,
                    limit = params.loadSize
                )
            )

            Timeline.GLOBAL -> notesService.globalTimeline(
                NotesReq.Timeline(
                    untilId = untilId,
                    limit = params.loadSize
                )
            )
        }.fold({ e ->
            LoadResult.Error(e)
        }, { notes ->
            LoadResult.Page(
                data = notes,
                prevKey = null,
                nextKey = notes.lastOrNull()?.id,
            )
        })
    }

    override fun getRefreshKey(state: PagingState<String, Note>): String? = null
}
