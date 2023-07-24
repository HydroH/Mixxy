package dev.hydroh.mixxy.data.remote

import dev.hydroh.misskey.client.client.MisskeyClient
import dev.hydroh.mixxy.data.local.UserDataStore
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MisskeyDataSource @Inject constructor(private val userDataStore: UserDataStore) {
    var client: MisskeyClient? = null

    init {
        runBlocking {
            val host = userDataStore.getHost.firstOrNull()
            val accessToken = userDataStore.getAccessToken.firstOrNull()
            if (host != null && accessToken != null) {
                client = MisskeyClient(host, accessToken)
            }
        }
    }

    fun newClient(host: String) {
        client = MisskeyClient.Builder(host)
            .name("Mixxy")
            .build()
    }
}