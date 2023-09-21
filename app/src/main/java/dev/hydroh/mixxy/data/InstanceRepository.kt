package dev.hydroh.mixxy.data

import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.snapshots.SnapshotStateMap
import dev.hydroh.mixxy.data.local.dao.EmojiDao
import dev.hydroh.mixxy.data.local.model.EmojiData
import dev.hydroh.mixxy.data.remote.InstanceService
import dev.hydroh.mixxy.data.remote.adapter.HostSelectionInterceptor
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InstanceRepository @Inject constructor(
    private val hostSelectionInterceptor: HostSelectionInterceptor,
    private val instanceService: InstanceService,
    private val emojiDao: EmojiDao,
) {
    val emojiMap: SnapshotStateMap<String, EmojiData> = mutableStateMapOf()

    fun getEmojis() =
        emojiDao.getEmojis().map { it.associateBy { it.name } }

    suspend fun fetchEmojis() =
        instanceService.emojis().map {
            emojiDao.insertEmojis(it.emojis.map {
                EmojiData(
                    name = it.name,
                    host = hostSelectionInterceptor.host!!,
                    url = it.url,
                    category = it.category,
                    aliases = it.aliases?.joinToString(",") ?: ""
                )
            })
        }
}
