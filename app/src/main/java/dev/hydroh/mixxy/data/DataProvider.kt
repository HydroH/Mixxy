package dev.hydroh.mixxy.data

import dev.hydroh.misskey.client.client.MisskeyClient

object DataProvider {
    lateinit var misskeyClient: MisskeyClient

    fun init() {
        misskeyClient = MisskeyClient("nya.one")
    }
}