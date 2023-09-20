package dev.hydroh.mixxy.data.remote

import arrow.core.Either
import dev.hydroh.mixxy.data.remote.model.Emojis
import retrofit2.http.POST

interface InstanceService {
    @POST("api/emojis")
    suspend fun emojis(): Either<Throwable, Emojis>
}
