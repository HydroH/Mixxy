package dev.hydroh.misskey.client.entity

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class DriveFile(
    val id: String,
    val createdAt: Instant,
    val name: String,
    val type: String,
    val md5: String,
    val size: Int,
    val isSensitive: Boolean,
    val blurHash: String?,
    val properties: DriveFilePropreties,
    val url: String?,
    val thumbnailUrl: String?,
    val comment: String?,
    val folderId: String?,
    // val folder: DriveFolder,
    val userId: String?,
    val user: UserLite?,
)

@Serializable
data class DriveFilePropreties(
    val width: Int?,
    val height: Int?,
    val orientation: Int?,
    val avgColor: String?,
)