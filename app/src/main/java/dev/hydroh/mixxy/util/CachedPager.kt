package dev.hydroh.mixxy.util

import androidx.paging.PagingData
import androidx.paging.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine

class CachedPager<T : Any>(prevPager: Flow<PagingData<T>>, private val key: (T) -> String) {
    private val localCache = MutableStateFlow(listOf<T>())
    val pager = prevPager.combine(localCache) { paging, local ->
        paging.map { pagingItem ->
            local.find { key(it) == key(pagingItem) } ?: pagingItem
        }
    }

    fun update(item: T) {
        localCache.value = localCache.value.filterNot { key(it) == key(item) } + item
    }

    fun invalidate() {
        localCache.value = listOf()
    }
}

fun <T : Any> Flow<PagingData<T>>.cachedPager(key: (T) -> String) =
    CachedPager(this, key)