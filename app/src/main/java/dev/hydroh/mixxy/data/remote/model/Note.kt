package dev.hydroh.mixxy.data.remote.model

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Note(
    val id: String,
    val createdAt: Instant,
    val userId: String,
    val user: UserLite,
    val text: String?,
    val cw: String?,
    val visibility: Visibility,
    val localOnly: Boolean,
    // val reactionAcceptance: Unknown,
    val renoteCount: Int,
    val repliesCount: Int,
    val reactions: Map<String, Int>,
    val reactionEmojis: Map<String, String>?,
    val myReaction: String?,
    val emojis: Map<String, String>?,
    val fileIds: ArrayList<String>,
    val files: ArrayList<DriveFile>,
    val replyId: String?,
    val renoteId: String?,
    val mentions: ArrayList<String>?,
    val uri: String?,
    val url: String?,
    val reply: Note?,
    val renote: Note?,
    val visibleUserIds: ArrayList<String>?,
    val tags: ArrayList<String>?,
    // val poll: Unknown,
    // val channelId: String?,
    // val channel: Unknown,
) {
    enum class Visibility {
        @SerialName("public")
        PUBLIC,

        @SerialName("home")
        HOME,

        @SerialName("followers")
        FOLLOWERS,

        @SerialName("specified")
        SPECIFIED,
    }
}

@Serializable
data class CreatedNote(
    val createdNote: Note,
)