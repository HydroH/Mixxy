package dev.hydroh.mixxy.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.hydroh.mixxy.data.local.AccountInfoDatabase
import dev.hydroh.mixxy.data.local.dao.AccountInfoDao
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {
    @Provides
    @Singleton
    fun provideAccountInfoDatabase(@ApplicationContext ctx: Context): AccountInfoDatabase {
        return Room.databaseBuilder(ctx, AccountInfoDatabase::class.java, "account_info")
            .build()
    }

    @Provides
    fun provideAccountInfoDao(accountInfoDatabase: AccountInfoDatabase): AccountInfoDao {
        return accountInfoDatabase.accountInfoDao()
    }
}