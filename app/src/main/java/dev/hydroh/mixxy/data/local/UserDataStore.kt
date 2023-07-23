package dev.hydroh.mixxy.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserDataStore @Inject constructor(@ApplicationContext private val ctx: Context) {
    companion object {
        private val Context.datastore: DataStore<Preferences> by preferencesDataStore("user")
        val HOST_KEY = stringPreferencesKey("host")
        val ACCESS_TOKEN_KEY = stringPreferencesKey("access_token")
    }

    val getHost: Flow<String?> = ctx.datastore.data.map { it[HOST_KEY] }
    suspend fun updateHost(host: String?) {
        ctx.datastore.edit {
            if (host != null) {
                it[HOST_KEY] = host
            } else {
                it.remove(HOST_KEY)
            }
        }
    }

    val getAccessToken: Flow<String?> = ctx.datastore.data.map { it[ACCESS_TOKEN_KEY] }
    suspend fun updateAccessToken(accessToken: String) {
        ctx.datastore.edit { it[ACCESS_TOKEN_KEY] = accessToken }
    }
}