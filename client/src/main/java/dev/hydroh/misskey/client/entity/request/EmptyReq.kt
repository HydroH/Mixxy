package dev.hydroh.misskey.client.entity.request

import kotlinx.serialization.Serializable

@Serializable
data class EmptyReq(
    val s: String? = null
) : Authable()