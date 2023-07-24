package dev.hydroh.misskey.client.client

import dev.hydroh.misskey.client.entity.Auth
import dev.hydroh.misskey.client.entity.request.Authable
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.cache.HttpCache
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.SIMPLE
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.URLProtocol
import io.ktor.http.appendPathSegments
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
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

    suspend inline fun auth(sessionId: String): String {
        val auth = client.post {
            url {
                appendPathSegments("api", "miauth", sessionId, "check")
            }
            contentType(ContentType.Application.Json)
            setBody(HashMap<Int, Int>())
        }.body<Auth>()
        accessToken = auth.token
        return auth.token
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