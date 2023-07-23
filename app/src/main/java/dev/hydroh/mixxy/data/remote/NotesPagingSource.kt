package dev.hydroh.mixxy.data.remote

import androidx.paging.PagingSource
import androidx.paging.PagingState
import dev.hydroh.misskey.client.entity.Note
import dev.hydroh.misskey.client.entity.request.NotesReq
import dev.hydroh.mixxy.ui.screen.notes.NotesTimeline

class NotesPagingSource(
    private val misskeyDataSource: MisskeyDataSource,
    private val timeline: NotesTimeline,
) : PagingSource<String, Note>() {
    override suspend fun load(params: LoadParams<String>): LoadResult<String, Note> {
        val sinceId = params.key
        try {
            val notes = when (timeline) {
                NotesTimeline.HOME -> misskeyDataSource.client!!.notes.timeline(
                    NotesReq.HomeTimeline(
                        sinceId = sinceId,
                        limit = params.loadSize
                    )
                )

                NotesTimeline.LOCAL -> misskeyDataSource.client!!.notes.localTimeline(
                    NotesReq.Timeline(
                        sinceId = sinceId,
                        limit = params.loadSize
                    )
                )

                NotesTimeline.HYBRID -> misskeyDataSource.client!!.notes.hybridTimeline(
                    NotesReq.Timeline(
                        sinceId = sinceId,
                        limit = params.loadSize
                    )
                )

                NotesTimeline.GLOBAL -> misskeyDataSource.client!!.notes.globalTimeline(
                    NotesReq.Timeline(
                        sinceId = sinceId,
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