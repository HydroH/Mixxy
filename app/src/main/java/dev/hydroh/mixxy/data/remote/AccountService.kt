package dev.hydroh.mixxy.data.remote

import arrow.core.Either
import dev.hydroh.mixxy.data.remote.model.Auth
import retrofit2.http.POST
import retrofit2.http.Path

interface AccountService {
    @POST("api/miauth/{sessionId}/check")
    suspend fun check(@Path("sessionId") sessionId: String): Either<Throwable, Auth>
}
