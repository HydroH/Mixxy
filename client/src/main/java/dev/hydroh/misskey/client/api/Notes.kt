package dev.hydroh.misskey.client.api

import dev.hydroh.misskey.client.client.MisskeyHttpClient
import dev.hydroh.misskey.client.entity.CreatedNote
import dev.hydroh.misskey.client.entity.Note
import dev.hydroh.misskey.client.entity.request.NotesReq

class Notes internal constructor(private val client: MisskeyHttpClient) {

    suspend fun list(req: NotesReq.List): ArrayList<Note> =
        client.request("notes", req)

    suspend fun show(req: NotesReq.Id): Note =
        client.request("notes/show", req)

    suspend fun create(req: NotesReq.Create): CreatedNote =
        client.request("notes/create", req)

    suspend fun delete(req: NotesReq.Id): Unit =
        client.request("notes/delete", req)

    suspend fun replies(req: NotesReq.Replies): ArrayList<Note> =
        client.request("notes/replies", req)

    suspend fun children(req: NotesReq.Children): ArrayList<Note> =
        client.request("notes/children", req)

    suspend fun conversation(req: NotesReq.Conversation): ArrayList<Note> =
        client.request("notes/conversation", req)

    suspend fun timeline(req: NotesReq.HomeTimeline): ArrayList<Note> =
        client.request("notes/timeline", req)

    suspend fun localTimeline(req: NotesReq.Timeline): ArrayList<Note> =
        client.request("notes/local-timeline", req)

    suspend fun hybridTimeline(req: NotesReq.Timeline): ArrayList<Note> =
        client.request("notes/hybrid-timeline", req)

    suspend fun globalTimeline(req: NotesReq.Timeline): ArrayList<Note> =
        client.request("notes/global-timeline", req)

    suspend fun featured(req: NotesReq.Page): ArrayList<Note> =
        client.request("notes/featured", req)
}