package dev.hydroh.mixxy.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import dev.hydroh.mixxy.data.local.model.AccountInfo
import kotlinx.coroutines.flow.Flow

@Dao
interface AccountInfoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAccountInfo(accountInfo: AccountInfo)

    @Update
    suspend fun updateAccountInfo(accountInfo: AccountInfo)

    @Delete
    suspend fun deleteAccountInfo(accountInfo: AccountInfo)

    @Query("SELECT * FROM account_info")
    fun getAccountInfos(): Flow<List<AccountInfo>>

    @Query("SELECT * FROM account_info WHERE active = 1")
    suspend fun getActiveAccountInfo(): List<AccountInfo>

    @Query("UPDATE account_info SET active = 0")
    suspend fun deactivateAccounts()
}