package dev.hydroh.mixxy.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import dev.hydroh.mixxy.data.local.dao.AccountInfoDao
import dev.hydroh.mixxy.data.local.model.AccountInfo

@Database(entities = [AccountInfo::class], version = 1, exportSchema = false)
abstract class AccountInfoDatabase : RoomDatabase() {
    abstract fun accountInfoDao(): AccountInfoDao
}
