package dev.hydroh.mixxy.data.remote.model.request

import dev.hydroh.mixxy.data.remote.model.Note
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

class NotesReq {
    @Serializable
    data class Id(
        val noteId: String,
        @Contextual val i: Token = Token(),
    )

    @Serializable
    data class Page(
        val limit: Int? = null,
        val offset: Int? = null,
        @Contextual val i: Token = Token(),
    )

    @Serializable
    data class Create(
        val visibility: Note.Visibility? = null,
        val visibleUserIds: ArrayList<String>? = null,
        val text: String? = null,
        val cw: String? = null,
        val localOnly: Boolean? = null,
        val noExtractMentions: Boolean? = null,
        val noExtractHashtags: Boolean? = null,
        val noExtractEmojis: Boolean? = null,
        val fileIds: ArrayList<String>? = null,
        val mediaIds: ArrayList<String>? = null,
        val replyId: String? = null,
        val renoteId: String? = null,
        val channelId: String? = null,
        // TODO: poll
        @Contextual val i: Token = Token(),
    )

    @Serializable
    data class List(
        val local: Boolean? = null,
        val reply: Boolean? = null,
        val renote: Boolean? = null,
        val withFiles: Boolean? = null,
        val limit: Int? = null,
        val sinceId: String? = null,
        val untilId: String? = null,
        @Contextual val i: Token = Token(),
    )

    @Serializable
    data class Replies(
        val noteId: String,
        val sinceId: String? = null,
        val untilId: String? = null,
        val limit: Int? = null,
        @Contextual val i: Token = Token(),
    )

    @Serializable
    data class Children(
        val noteId: String,
        val limit: Int? = null,
        val sinceId: String? = null,
        val untilId: String? = null,
        @Contextual val i: Token = Token(),
    )

    @Serializable
    data class Conversation(
        val noteId: String,
        val limit: Int? = null,
        val offset: Int? = null,
        @Contextual val i: Token = Token(),
    )

    @Serializable
    data class HomeTimeline(
        val limit: Int? = null,
        val sinceId: String? = null,
        val untilId: String? = null,
        val sinceDate: Int? = null,
        val untilDate: Int? = null,
        val includeMyRenotes: Boolean? = null,
        val includeRenotedMyNotes: Boolean? = null,
        val includeLocalRenotes: Boolean? = null,
        val withFiles: Boolean? = null,
        @Contextual val i: Token = Token(),
    )

    @Serializable
    data class Timeline(
        val withFiles: Boolean? = null,
        val limit: Int? = null,
        val sinceId: String? = null,
        val untilId: String? = null,
        val sinceDate: Int? = null,
        val untilDate: Int? = null,
        @Contextual val i: Token = Token(),
    )

    @Serializable
    data class CreateReaction(
        val noteId: String,
        val reaction: String,
        @Contextual val i: Token = Token(),
    )

    @Serializable
    data class DeleteReaction(
        val noteId: String,
        @Contextual val i: Token = Token(),
    )
}
