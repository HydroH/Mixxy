package dev.hydroh.mixxy.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.hydroh.mixxy.data.local.model.EmojiData

@Dao
interface EmojiDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEmojis(emojis: List<EmojiData>)

    @Query("SELECT * FROM emoji_data WHERE host = :host")
    suspend fun getEmojis(host: String): List<EmojiData>

    @Query("SELECT * FROM emoji_data WHERE host = :host AND name IN (:names)")
    suspend fun getEmojis(host: String, names: List<String>): List<EmojiData>
}