package dev.hydroh.mixxy.data

import arrow.core.raise.either
import dev.hydroh.mixxy.data.local.dao.AccountInfoDao
import dev.hydroh.mixxy.data.local.dao.EmojiDao
import dev.hydroh.mixxy.data.local.model.EmojiData
import dev.hydroh.mixxy.data.remote.InstanceService
import kotlinx.collections.immutable.toImmutableMap
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InstanceRepository @Inject constructor(
    private val instanceService: InstanceService,
    private val accountInfoDao: AccountInfoDao,
    private val emojiDao: EmojiDao,
) {
    fun getEmojis() =
        emojiDao.getEmojis().map { it.associateBy { emoji -> emoji.name }.toImmutableMap() }

    suspend fun fetchEmojis() = either<Throwable, Unit> {
        val host = accountInfoDao.getActiveAccountInfo().first().firstOrNull()?.host
            ?: raise(Error("No active account"))
        return instanceService.emojis().map {
            emojiDao.insertEmojis(
                it.emojis.map { emoji ->
                    EmojiData(
                        name = emoji.name,
                        host = host,
                        url = emoji.url,
                        category = emoji.category,
                        aliases = emoji.aliases?.joinToString(",") ?: ""
                    )
                }
            )
        }
    }
}

