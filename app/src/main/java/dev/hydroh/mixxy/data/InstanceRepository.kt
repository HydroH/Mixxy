package dev.hydroh.mixxy.data

import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.snapshots.SnapshotStateMap
import dev.hydroh.mixxy.data.local.dao.EmojiDao
import dev.hydroh.mixxy.data.local.model.EmojiData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InstanceRepository @Inject constructor(
    private val misskeyDataSource: MisskeyDataSource,
    private val emojiDao: EmojiDao,
) {
    val emojiMap: SnapshotStateMap<String, EmojiData> = mutableStateMapOf()

    suspend fun initEmojis() {
        val emojis = emojiDao.getEmojis(misskeyDataSource.client!!.host)
        emojiMap.putAll(emojis.map { Pair(it.name, it) })
    }

    suspend fun updateEmojis(emojis: List<String>) {
        emojis.forEach { name ->
            if (!emojiMap.contains(name)) {
                val newEmojis = misskeyDataSource.client!!.instance.emojis().emojis.map {
                    EmojiData(
                        name = it.name,
                        host = misskeyDataSource.client!!.host,
                        url = it.url,
                        category = it.category,
                        aliases = it.aliases?.joinToString(",") ?: ""
                    )
                }
                emojiDao.insertEmojis(newEmojis)
                emojiMap.putAll(newEmojis.map { Pair(it.name, it) })
                return
            }
        }
    }
}
