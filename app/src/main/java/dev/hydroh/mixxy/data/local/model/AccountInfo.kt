package dev.hydroh.mixxy.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "account_info", primaryKeys = ["username", "host"])
data class AccountInfo(
    @ColumnInfo(name = "username") val username: String,
    @ColumnInfo(name = "host") val host: String,
    @ColumnInfo(name = "access_token") val accessToken: String,
    @ColumnInfo(name = "active") val active: Boolean,
    @ColumnInfo(name = "name") val name: String?,
    @ColumnInfo(name = "avatar_url") val avatarUrl: String?,
)
