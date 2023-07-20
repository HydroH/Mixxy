package dev.hydroh.misskey.client.entity.request

import kotlinx.serialization.Serializable

@Serializable
sealed class Authable {
    internal var i: String? = null
}