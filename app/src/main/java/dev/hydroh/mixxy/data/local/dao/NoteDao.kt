package dev.hydroh.mixxy.data.local.dao

import androidx.paging.PagingSource
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

    @Query("DELETE FROM note_json WHERE id = (SELECT id FROM note_timeline WHERE timeline = :timeline)")
    suspend fun _deleteNoteJsons(timeline: Timeline)

    @Query("DELETE FROM note_timeline WHERE timeline = :timeline")
    suspend fun _deleteNoteTimelines(timeline: Timeline)

    @Update
    suspend fun updateNote(note: NoteJson)

    @Transaction
    suspend fun replaceNotes(noteJsons: List<NoteJson>, timeline: Timeline) {
        _deleteNoteJsons(timeline)
        _deleteNoteTimelines(timeline)
        _insertNoteJsons(noteJsons)
        _insertNoteTimelines(noteJsons.map { NoteTimeline(it.id, timeline.value) })
    }

    @Transaction
    suspend fun insertNotes(noteJsons: List<NoteJson>, timeline: Timeline) {
        _insertNoteJsons(noteJsons)
        _insertNoteTimelines(noteJsons.map { NoteTimeline(it.id, timeline.value) })
    }

    @Query("SELECT * FROM note_json " +
            "JOIN note_timeline " +
            "ON note_json.id = note_timeline.id " +
            "WHERE note_timeline.timeline = :timeline " +
            "ORDER BY note_json.id DESC")
    fun getNotes(timeline: Timeline): PagingSource<Int, NoteJson>
}
