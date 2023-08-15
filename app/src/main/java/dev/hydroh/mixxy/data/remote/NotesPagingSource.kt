package dev.hydroh.mixxy.data.remote

import androidx.paging.PagingSource
import androidx.paging.PagingState
import dev.hydroh.misskey.client.entity.Note
import dev.hydroh.misskey.client.entity.request.NotesReq
import dev.hydroh.mixxy.ui.screen.timeline.Timeline

class NotesPagingSource(
    private val misskeyDataSource: MisskeyDataSource,
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
        try {
            val notes = when (timeline) {
                Timeline.HOME -> misskeyDataSource.client!!.notes.timeline(
                    NotesReq.HomeTimeline(
                        untilId = untilId,
                        limit = params.loadSize
                    )
                )

                Timeline.LOCAL -> misskeyDataSource.client!!.notes.localTimeline(
                    NotesReq.Timeline(
                        untilId = untilId,
                        limit = params.loadSize
                    )
                )

                Timeline.HYBRID -> misskeyDataSource.client!!.notes.hybridTimeline(
                    NotesReq.Timeline(
                        untilId = untilId,
                        limit = params.loadSize
                    )
                )

                Timeline.GLOBAL -> misskeyDataSource.client!!.notes.globalTimeline(
                    NotesReq.Timeline(
                        untilId = untilId,
                        limit = params.loadSize
                    )
                )
            }
            return LoadResult.Page(
                data = notes,
                prevKey = null,
                nextKey = notes.lastOrNull()?.id,
            )
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<String, Note>): String? = null
}