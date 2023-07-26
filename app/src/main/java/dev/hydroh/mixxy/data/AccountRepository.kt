package dev.hydroh.mixxy.data

import dev.hydroh.mixxy.data.local.dao.AccountInfoDao
import dev.hydroh.mixxy.data.local.model.AccountInfo
import dev.hydroh.mixxy.data.remote.MisskeyDataSource
import javax.inject.Inject

class AccountRepository @Inject constructor(
    private val misskeyDataSource: MisskeyDataSource,
    private val accountInfoDao: AccountInfoDao
) {
    fun getAuthUrl(host: String): String {
        misskeyDataSource.newClient(host)
        return misskeyDataSource.client!!.authUrl!!
    }

    suspend fun newAccount(): Boolean {
        val auth = misskeyDataSource.client!!.auth() ?: return false
        accountInfoDao.deactivateAccounts()
        accountInfoDao.insertAccountInfo(
            AccountInfo(
                username = auth.user.username,
                active = true,
                host = misskeyDataSource.client!!.host,
                accessToken = auth.token,
                name = auth.user.name,
                avatarUrl = auth.user.avatarUrl,
            )
        )
        return true
    }

    suspend fun loadAccount(): Boolean {
        val accountInfos = accountInfoDao.getActiveAccountInfo()
        if (accountInfos.isEmpty()) return false
        val accountInfo = accountInfos.first()
        misskeyDataSource.newClient(accountInfo.host, accountInfo.accessToken)
        return true
    }
}