package dev.hydroh.mixxy.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.hydroh.mixxy.data.local.dao.AccountInfoDao
import dev.hydroh.mixxy.data.local.dao.EmojiDao
import dev.hydroh.mixxy.data.local.db.AppDatabase
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext ctx: Context): AppDatabase {
        return Room.databaseBuilder(ctx, AppDatabase::class.java, "app")
            .build()
    }

    @Provides
    fun provideAccountInfoDao(appDatabase: AppDatabase): AccountInfoDao {
        return appDatabase.accountInfoDao()
    }

    @Provides
    fun provideEmojiDao(appDatabase: AppDatabase): EmojiDao {
        return appDatabase.emojiDao()
    }
}
