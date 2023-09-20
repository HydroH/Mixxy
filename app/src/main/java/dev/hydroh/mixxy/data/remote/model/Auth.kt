package dev.hydroh.mixxy.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class Auth(
    val token: String,
    val user: UserLite,
)
