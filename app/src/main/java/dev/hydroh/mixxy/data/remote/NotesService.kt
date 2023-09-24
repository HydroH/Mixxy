package dev.hydroh.mixxy.data.remote

import arrow.core.Either
import dev.hydroh.mixxy.data.remote.model.CreatedNote
import dev.hydroh.mixxy.data.remote.model.Note
import dev.hydroh.mixxy.data.remote.model.request.NotesReq
import retrofit2.http.Body
import retrofit2.http.POST

interface NotesService {
    @POST("api/notes")
    suspend fun list(@Body body: NotesReq.List): Either<Throwable, List<Note>>

    @POST("api/notes/show")
    suspend fun show(@Body body: NotesReq.Id): Either<Throwable, Note>

    @POST("api/notes/create")
    suspend fun create(@Body body: NotesReq.Create): Either<Throwable, CreatedNote>

    @POST("api/notes/delete")
    suspend fun delete(@Body body: NotesReq.Id): Either<Throwable, Unit>

    @POST("api/notes/replies")
    suspend fun replies(@Body body: NotesReq.Replies): Either<Throwable, List<Note>>

    @POST("api/notes/children")
    suspend fun children(@Body body: NotesReq.Children): Either<Throwable, List<Note>>

    @POST("api/notes/conversation")
    suspend fun conversation(@Body body: NotesReq.Conversation): Either<Throwable, List<Note>>

    @POST("api/notes/timeline")
    suspend fun timeline(@Body body: NotesReq.HomeTimeline): Either<Throwable, List<Note>>

    @POST("api/notes/local-timeline")
    suspend fun localTimeline(@Body body: NotesReq.Timeline): Either<Throwable, List<Note>>

    @POST("api/notes/hybrid-timeline")
    suspend fun hybridTimeline(@Body body: NotesReq.Timeline): Either<Throwable, List<Note>>

    @POST("api/notes/global-timeline")
    suspend fun globalTimeline(@Body body: NotesReq.Timeline): Either<Throwable, List<Note>>

    @POST("api/notes/featured")
    suspend fun featured(@Body body: NotesReq.Page): Either<Throwable, List<Note>>

    @POST("api/notes/reactions/create")
    suspend fun createReaction(@Body body: NotesReq.CreateReaction): Either<Throwable, Unit>

    @POST("api/notes/reactions/delete")
    suspend fun deleteReaction(@Body body: NotesReq.DeleteReaction): Either<Throwable, Unit>
}
