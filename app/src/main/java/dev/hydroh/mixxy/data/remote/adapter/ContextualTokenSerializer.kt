package dev.hydroh.mixxy.data.remote.adapter

import dev.hydroh.mixxy.data.remote.model.request.Token
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ContextualTokenSerializer @Inject constructor() : KSerializer<Token> {
    var token: String? = null

    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("Token", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Token) = encoder.encodeString(token ?: "")
    override fun deserialize(decoder: Decoder): Token = Token()
}
