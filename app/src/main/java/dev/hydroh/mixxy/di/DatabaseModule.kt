package dev.hydroh.mixxy.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.hydroh.mixxy.data.local.datastore.UserDataStoreManager
import dev.hydroh.mixxy.data.local.db.AppDatabase
import javax.inject.Singleton

val Context.userDataStore: DataStore<Preferences> by preferencesDataStore(name = "user")

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext ctx: Context) =
        Room.databaseBuilder(ctx, AppDatabase::class.java, "app")
            .build()

    @Provides
    fun provideAccountInfoDao(appDatabase: AppDatabase) =
        appDatabase.accountInfoDao()

    @Provides
    fun provideNoteDao(appDatabase: AppDatabase) =
        appDatabase.noteDao()

    @Provides
    @Singleton
    @UserDataStore
    fun provideUserDataStore(@ApplicationContext ctx: Context) =
        ctx.userDataStore

    @Provides
    @Singleton
    fun provideUserDataStoreManager(@UserDataStore userDataStore: DataStore<Preferences>) =
        UserDataStoreManager(userDataStore)
}
