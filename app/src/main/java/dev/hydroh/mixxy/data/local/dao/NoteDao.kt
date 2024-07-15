package dev.hydroh.mixxy.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RawQuery
import androidx.room.Transaction
import androidx.room.Update
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery
import dev.hydroh.mixxy.data.local.model.Direction
import dev.hydroh.mixxy.data.local.model.NoteJson
import dev.hydroh.mixxy.data.local.model.NoteTimeline
import dev.hydroh.mixxy.data.local.model.Paging
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

    @RawQuery
    suspend fun _getNotes(query: SupportSQLiteQuery): List<NoteJson>

    suspend fun getNotes(timeline: Timeline, paging: Paging<Long>): List<NoteJson> {
        var queryStr =
            "SELECT * FROM note_json " +
            "INNER JOIN note_timeline " +
            "ON note_json.id = note_timeline.id " +
            "WHERE note_timeline.timeline = ? " +
            "ORDER BY note_json.created_at DESC "
        val args = mutableListOf(timeline.value.toString())
        paging.edge?.let {
            queryStr += "AND note_json.created_at ${if (paging.direction == Direction.PREV) ">" else "<"} ? "
            args.add(it.toString())
        }
        queryStr += "LIMIT ?"
        args.add(paging.limit.toString())
        val query = SimpleSQLiteQuery(queryStr, args.toTypedArray())
        return _getNotes(query)
    }
}
