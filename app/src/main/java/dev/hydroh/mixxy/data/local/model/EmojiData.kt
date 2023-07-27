package dev.hydroh.mixxy.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "emoji_data", primaryKeys = ["name", "host"])
data class EmojiData(
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "host") val host: String,
    @ColumnInfo(name = "url") val url: String,
    @ColumnInfo(name = "category") val category: String?,
    @ColumnInfo(name = "aliases") val aliases: String
)