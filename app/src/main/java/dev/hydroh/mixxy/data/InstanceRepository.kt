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
        emojiDao.getEmojis().map { it.associateBy { it.name }.toImmutableMap() }

    suspend fun fetchEmojis() =
        instanceService.emojis().map {
            emojiDao.insertEmojis(
                it.emojis.map {
                    EmojiData(
                        name = it.name,
                        host = hostSelectionInterceptor.host!!,
                        url = it.url,
                        category = it.category,
                        aliases = it.aliases?.joinToString(",") ?: ""
                    )
                }
            )
        }
}
