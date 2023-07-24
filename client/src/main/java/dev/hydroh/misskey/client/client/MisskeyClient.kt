package dev.hydroh.misskey.client.client

import dev.hydroh.misskey.client.api.Notes
import io.ktor.client.HttpClient
import io.ktor.http.URLBuilder
import io.ktor.http.URLProtocol
import io.ktor.http.appendPathSegments
import java.util.UUID

class MisskeyClient(
    val host: String,
    accessToken: String? = null,
    httpClient: HttpClient? = null,
) {
    private val client = MisskeyHttpClient(host, accessToken, httpClient)

    private var sessionId: String? = null
    var authUrl: String? = null
        private set
    var isAuthed = accessToken != null
        private set

    internal constructor(
        host: String,
        sessionId: String,
        authUrl: String,
        httpClient: HttpClient? = null
    ) : this(
        host = host,
        httpClient = httpClient
    ) {
        this.sessionId = sessionId
        this.authUrl = authUrl
    }

    suspend fun auth(): String? {
        try {
            val accessToken = client.auth(sessionId!!)
            isAuthed = true
            return accessToken
        } catch (_: Exception) {
        }
        return null
    }

    val notes = Notes(client)

    class Builder(val host: String) {
        // TODO: Permission control
        private val PERMISSION_ALL =
            "read:account,write:account,read:blocks,write:blocks,read:drive,write:drive,read:favorites,write:favorites,read:following,write:following,read:messaging,write:messaging,read:mutes,write:mutes,write:notes,read:notifications,write:notifications,write:reactions,write:votes,read:pages,write:pages,write:page-likes,read:page-likes,write:gallery-likes,read:gallery-likes"

        private val sessionId: String = UUID.randomUUID().toString()
        private var httpClient: HttpClient? = null
        private val url = URLBuilder().apply {
            protocol = URLProtocol.HTTPS
            host = this@Builder.host
            appendPathSegments("miauth", sessionId)
            parameters.append("permission", PERMISSION_ALL)
        }

        fun httpClient(httpClient: HttpClient): Builder {
            this.httpClient = httpClient
            return this
        }

        fun name(name: String): Builder {
            url.apply {
                parameters.append("name", name)
            }
            return this
        }

        fun icon(iconUrl: String): Builder {
            url.apply {
                parameters.append("icon", iconUrl)
            }
            return this
        }

        fun callbackUrl(callbackUrl: String): Builder {
            url.apply {
                parameters.append("callback", callbackUrl)
            }
            return this
        }

        fun build(): MisskeyClient = MisskeyClient(
            host = host,
            sessionId = sessionId,
            authUrl = url.buildString(),
            httpClient = httpClient,
        )
    }
}