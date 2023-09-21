package dev.hydroh.mixxy.data

import dev.hydroh.mixxy.data.local.dao.EmojiDao
import dev.hydroh.mixxy.data.local.model.EmojiData
import dev.hydroh.mixxy.data.remote.InstanceService
import dev.hydroh.mixxy.data.remote.adapter.HostSelectionInterceptor
import kotlinx.collections.immutable.toImmutableMap
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InstanceRepository @Inject constructor(
    private val hostSelectionInterceptor: HostSelectionInterceptor,
    private val instanceService: InstanceService,
    private val emojiDao: EmojiDao,
) {
    fun getEmojis() =
        emojiDao.getEmojis().map { it.associateBy { emoji -> emoji.name }.toImmutableMap() }

    suspend fun fetchEmojis() =
        instanceService.emojis().map {
            emojiDao.insertEmojis(
                it.emojis.map { emoji ->
                    EmojiData(
                        name = emoji.name,
                        host = hostSelectionInterceptor.host!!,
                        url = emoji.url,
                        category = emoji.category,
                        aliases = emoji.aliases?.joinToString(",") ?: ""
                    )
                }
            )
        }
}
