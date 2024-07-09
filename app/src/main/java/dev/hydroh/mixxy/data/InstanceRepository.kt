package dev.hydroh.mixxy.data

import arrow.core.raise.either
import dev.hydroh.mixxy.data.local.datastore.UserDataStoreManager
import dev.hydroh.mixxy.data.remote.InstanceService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InstanceRepository @Inject constructor(
    private val instanceService: InstanceService,
    private val userDataStoreManager: UserDataStoreManager,
) {
    val emojis = userDataStoreManager.emojis

    suspend fun fetchEmojis() = either {
        val emojis = instanceService.emojis().bind()
        userDataStoreManager.setEmojis(emojis)
    }
}
