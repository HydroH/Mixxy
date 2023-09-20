package dev.hydroh.mixxy.data.remote

import arrow.core.Either
import dev.hydroh.mixxy.data.remote.model.Auth
import dev.hydroh.mixxy.data.remote.model.request.AccountReq
import retrofit2.http.Body
import retrofit2.http.POST

interface AccountService {
    @POST("api/miauth/{sessionId}/check")
    suspend fun check(sessionId: String, @Body body: AccountReq.Check): Either<Throwable, Auth>
}
