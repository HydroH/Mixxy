package dev.hydroh.mixxy.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class Emoji(
    val aliases: List<String>?,
    val name: String,
    val category: String?,
    val url: String,
)

@Serializable
data class Emojis(
    val emojis: List<Emoji>
)