package dev.hydroh.mixxy.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import dev.hydroh.mixxy.data.local.model.NoteJson
import dev.hydroh.mixxy.data.local.model.NoteTimeline
import dev.hydroh.mixxy.data.local.model.Timeline

@Dao
interface NoteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun _insertNoteJsons(noteJsons: List<NoteJson>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun _insertNoteTimelines(noteTimelines: List<NoteTimeline>)

    @Update
    suspend fun updateNote(note: NoteJson)

    @Transaction
    suspend fun insertNotes(noteJsons: List<NoteJson>, timeline: Timeline) {
        _insertNoteJsons(noteJsons)
        _insertNoteTimelines(noteJsons.map { NoteTimeline(it.id, timeline.value) })
    }

    @Query("SELECT * FROM note_json " +
            "LEFT JOIN note_timeline " +
            "ON note_json.id = note_timeline.id " +
            "WHERE note_timeline.timeline = :timeline " +
            "ORDER BY note_json.created_at DESC " +
            "LIMIT :limit " +
            "OFFSET :offset")
    suspend fun _getNotes(timeline: Int, limit: Int, offset: Int): List<NoteJson>

    suspend fun getNotes(timeline: Timeline, limit: Int, offset: Int) =
        _getNotes(timeline.value, limit, offset)
}
