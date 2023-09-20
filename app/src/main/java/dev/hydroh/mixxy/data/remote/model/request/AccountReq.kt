package dev.hydroh.mixxy.data.remote.model.request

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

class AccountReq {
    @Serializable
    data class Check(
        @Contextual val i: Token = Token(),
    )
}
