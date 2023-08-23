package dev.hydroh.mixxy.util

import androidx.paging.PagingData
import androidx.paging.map
import dev.hydroh.misskey.client.entity.Note
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine

class NotesCachedPager(prevPager: Flow<PagingData<Note>>) {
    private val localCache = MutableStateFlow(listOf<Note>())
    val pager = prevPager.combine(localCache) { paging, local ->
        paging.map { pagingItem ->
            local.find { it.id == pagingItem.id } ?: pagingItem.copy(
                renote = local.find { it.id == pagingItem.renoteId } ?: pagingItem.renote,
                reply = local.find { it.id == pagingItem.replyId } ?: pagingItem.reply,
            )
        }
    }

    fun update(item: Note) {
        localCache.value = localCache.value.filterNot { it.id == item.id } + item
    }

    fun invalidate() {
        localCache.value = listOf()
    }
}

fun Flow<PagingData<Note>>.cachedPager(): NotesCachedPager =
    NotesCachedPager(this)