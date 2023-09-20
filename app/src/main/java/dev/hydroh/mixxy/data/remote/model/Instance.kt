package dev.hydroh.mixxy.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class Instance(
    val name: String?,
    val softwareName: String,
    val softwareVersion: String,
    val iconUrl: String,
    val faviconUrl: String,
    val themeColor: String?,
)