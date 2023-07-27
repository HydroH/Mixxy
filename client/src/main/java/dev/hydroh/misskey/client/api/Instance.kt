package dev.hydroh.misskey.client.api

import dev.hydroh.misskey.client.client.MisskeyHttpClient
import dev.hydroh.misskey.client.entity.Emojis
import dev.hydroh.misskey.client.entity.request.EmptyReq

class Instance internal constructor(private val client: MisskeyHttpClient) {
    suspend fun emojis(): Emojis =
        client.request("emojis", EmptyReq())
}