package dev.hydroh.misskey.client.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: String,
)

@Serializable
data class UserLite(
    val id: String,
    val name: String?,
    val username: String,
    val host: String?,
    val avatarUrl: String,
    val avatarBlurhash: String?,
    val isBot: Boolean,
    val isCat: Boolean,
    val instance: Instance?,
    val emojis: HashMap<String, String>,
    val onlineStatus: OnlineStatus,
    // val badgeRoles
) {
    enum class OnlineStatus {
        @SerialName("unknown")
        UNKNOWN,

        @SerialName("online")
        ONLINE,

        @SerialName("active")
        ACTIVE,

        @SerialName("offline")
        OFFLINE,
    }
}