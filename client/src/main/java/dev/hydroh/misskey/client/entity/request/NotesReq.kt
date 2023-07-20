package dev.hydroh.misskey.client.entity.request

import dev.hydroh.misskey.client.entity.Note
import kotlinx.serialization.Serializable

class NotesReq {
    @Serializable
    data class Id(
        val noteId: String,
    ) : Authable()

    @Serializable
    data class Page(
        val limit: Int? = null,
        val offset: Int? = null,
    ) : Authable()

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
    ) : Authable()

    @Serializable
    data class List(
        val local: Boolean? = null,
        val reply: Boolean? = null,
        val renote: Boolean? = null,
        val withFiles: Boolean? = null,
        val limit: Int? = null,
        val sinceId: String? = null,
        val untilId: String? = null,
    ) : Authable()

    @Serializable
    data class Replies(
        val noteId: String,
        val sinceId: String? = null,
        val untilId: String? = null,
        val limit: Int? = null,
    ) : Authable()

    @Serializable
    data class Children(
        val noteId: String,
        val limit: Int? = null,
        val sinceId: String? = null,
        val untilId: String? = null,
    ) : Authable()

    @Serializable
    data class Conversation(
        val noteId: String,
        val limit: Int? = null,
        val offset: Int? = null,
    ) : Authable()

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
    ) : Authable()

    @Serializable
    data class Timeline(
        val withFiles: Boolean? = null,
        val limit: Int? = null,
        val sinceId: String? = null,
        val untilId: String? = null,
        val sinceDate: Int? = null,
        val untilDate: Int? = null,
    ) : Authable()
}