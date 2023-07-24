package dev.hydroh.mixxy.data.remote

import dev.hydroh.misskey.client.client.MisskeyClient
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MisskeyDataSource @Inject constructor() {
    var client: MisskeyClient? = null
    fun newClient(host: String) {
        client = MisskeyClient.Builder(host)
            .name("Mixxy")
            .build()
    }

    fun newClient(host: String, accessToken: String) {
        client = MisskeyClient(host, accessToken)
    }
}