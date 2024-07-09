package dev.hydroh.mixxy.data.local.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import dev.hydroh.mixxy.data.remote.model.Emojis
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class UserDataStoreManager(
    private val dataStore: DataStore<Preferences>,
) {
    private companion object {
        val EMOJI_KEY = stringPreferencesKey("emojis")
    }

    val emojis = dataStore.data.map {
        it[EMOJI_KEY]?.let { Json.decodeFromString<Emojis>(it) } ?: Emojis(listOf())
    }

    suspend fun setEmojis(emojis: Emojis) {
        dataStore.edit {
            it[EMOJI_KEY] = Json.encodeToString(emojis)
        }
    }
}