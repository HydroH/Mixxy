package dev.hydroh.misskey.client.entity

import kotlinx.serialization.Serializable

@Serializable
data class Instance(
    val name: String,
    val softwareName: String,
    val softwareVersion: String,
    val iconUrl: String,
    val faviconUrl: String,
    val themeColor: String?,
)