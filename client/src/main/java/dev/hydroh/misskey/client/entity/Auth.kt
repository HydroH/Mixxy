package dev.hydroh.misskey.client.entity

import kotlinx.serialization.Serializable

@Serializable
data class Auth(val token: String)