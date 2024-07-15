package dev.hydroh.mixxy.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import dev.hydroh.mixxy.data.remote.model.Note
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Entity(tableName = "note_json", primaryKeys = ["id"])
data class NoteJson(
    @ColumnInfo(name = "id") val id: String,
    @ColumnInfo(name="created_at") val createdAt: Long,
    @ColumnInfo(name = "json") val json: String,
)

@Entity(tableName = "note_timeline", primaryKeys = ["id", "timeline"])
data class NoteTimeline(
    @ColumnInfo(name = "id") val id: String,
    @ColumnInfo(name = "timeline") val timeline: Int,
)

enum class Timeline(val value: Int) {
    HOME(0),
    LOCAL(1),
    HYBRID(2),
    GLOBAL(3),
}

fun NoteJson.toNote() =Json.decodeFromString<Note>(this.json)
fun Note.toNoteJson() = NoteJson(this.id, this.createdAt.epochSeconds, Json.encodeToString(this))
