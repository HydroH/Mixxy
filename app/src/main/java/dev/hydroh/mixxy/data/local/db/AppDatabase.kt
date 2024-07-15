package dev.hydroh.mixxy.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import dev.hydroh.mixxy.data.local.dao.AccountInfoDao
import dev.hydroh.mixxy.data.local.dao.NoteDao
import dev.hydroh.mixxy.data.local.model.AccountInfo
import dev.hydroh.mixxy.data.local.model.NoteJson
import dev.hydroh.mixxy.data.local.model.NoteTimeline

@Database(
    entities = [
        AccountInfo::class,
        NoteJson::class,
        NoteTimeline::class,
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun accountInfoDao(): AccountInfoDao
    abstract fun noteDao(): NoteDao
}
