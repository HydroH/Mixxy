package dev.hydroh.mixxy.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.hydroh.mixxy.data.local.model.EmojiData
import kotlinx.coroutines.flow.Flow

@Dao
interface EmojiDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEmojis(emojis: List<EmojiData>)

    @Query(
        "SELECT * FROM emoji_data " +
            "INNER JOIN account_info ON emoji_data.host = account_info.host " +
            "WHERE account_info.active = 1"
    )
    fun getEmojis(): Flow<List<EmojiData>>
}
