package dev.hydroh.mixxy.data

import dev.hydroh.mixxy.data.local.dao.AccountInfoDao
import dev.hydroh.mixxy.data.local.model.AccountInfo
import dev.hydroh.mixxy.data.remote.AccountService
import dev.hydroh.mixxy.data.remote.adapter.ContextualTokenSerializer
import dev.hydroh.mixxy.data.remote.adapter.HostSelectionInterceptor
import kotlinx.coroutines.flow.first
import okhttp3.HttpUrl
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AccountRepository @Inject constructor(
    private val accountService: AccountService,
    private val accountInfoDao: AccountInfoDao,
) {
    private val PERMISSION_ALL =
        "read:account,write:account,read:blocks,write:blocks,read:drive,write:drive,read:favorites,write:favorites,read:following,write:following,read:messaging,write:messaging,read:mutes,write:mutes,write:notes,read:notifications,write:notifications,write:reactions,write:votes,read:pages,write:pages,write:page-likes,read:page-likes,write:gallery-likes,read:gallery-likes"

    private var authUrl: String = ""
    private var sessionId: String = ""
    private var host: String = ""

    fun newAuth(host: String): String {
        this.sessionId = UUID.randomUUID().toString()
        this.host = host
        authUrl = HttpUrl.Builder()
            .scheme("https")
            .host(host)
            .addPathSegments("miauth/$sessionId")
            .addQueryParameter("name", "Mixxy")
            .addQueryParameter("permission", PERMISSION_ALL)
            .build()
            .toString()
        return authUrl
    }

    suspend fun newAccount(): Boolean =
        accountService.check(sessionId).fold(
            { false },
            { auth ->
                accountInfoDao.insertAccountInfo(
                    AccountInfo(
                        username = auth.user.username,
                        active = true,
                        host = host,
                        accessToken = auth.token,
                        name = auth.user.name,
                        avatarUrl = auth.user.avatarUrl,
                    )
                )
                true
            }
        )

    suspend fun hasAccount(): Boolean = accountInfoDao.getActiveAccountInfo().first().isNotEmpty()
}
