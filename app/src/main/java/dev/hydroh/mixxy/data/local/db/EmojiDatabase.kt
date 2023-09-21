package dev.hydroh.mixxy.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import dev.hydroh.mixxy.data.local.dao.EmojiDao
import dev.hydroh.mixxy.data.local.model.AccountInfo
import dev.hydroh.mixxy.data.local.model.EmojiData

@Database(entities = [EmojiData::class, AccountInfo::class], version = 1, exportSchema = false)
abstract class EmojiDatabase : RoomDatabase() {
    abstract fun emojiDao(): EmojiDao
}
