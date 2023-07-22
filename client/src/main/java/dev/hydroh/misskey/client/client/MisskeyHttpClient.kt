package dev.hydroh.misskey.client.client

import dev.hydroh.misskey.client.entity.Auth
import dev.hydroh.misskey.client.entity.request.Authable
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.cache.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json

@OptIn(ExperimentalSerializationApi::class)
internal class MisskeyHttpClient(
    private val host: String,
    private var accessToken: String? = null,
    httpClient: HttpClient? = null,
    logging: Boolean = true,
) {
    private var client = httpClient ?: HttpClient()

    init {
        client = client.config {
            expectSuccess = true
            defaultRequest {
                url {
                    protocol = URLProtocol.HTTPS
                    host = this@MisskeyHttpClient.host
                }
            }
            install(ContentNegotiation) {
                json(Json {
                    encodeDefaults = true
                    explicitNulls = false
                    ignoreUnknownKeys = true
                    classDiscriminator = "#class"
                })
            }
            install(HttpCache)
            if (logging) {
                install(Logging) {
                    logger = Logger.SIMPLE
                    level = LogLevel.ALL
                }
            }
        }
    }

    suspend inline fun auth(sessionId: String) {
        val auth = client.post {
            url {
                appendPathSegments("api", "miauth", sessionId, "check")
            }
        }.body<Auth>()
        accessToken = auth.token
    }

    suspend inline fun <reified T> request(path: String, body: Authable): T {
        body.i = accessToken
        return client.post {
            url {
                appendPathSegments("api", path)
            }
            contentType(ContentType.Application.Json)
            setBody(body)
        }.body()
    }
}