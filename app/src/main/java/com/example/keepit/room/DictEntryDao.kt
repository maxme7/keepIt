package com.example.keepit.room

import android.database.Cursor
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.keepit.enums.Language

@Dao
interface DictEntryDao {
    @Query("SELECT * FROM dictentry")
    suspend fun getAll(): List<DictEntry>

//    @Query("SELECT * FROM dictentry WHERE cid IN (:dictEntryIds)")
//    suspend fun getByIds(dictEntryIds: IntArray): List<DictEntry>

    @Insert
    suspend fun insertAll(vararg dictEntry: DictEntry)

    @Delete
    suspend fun delete(dictEntry: DictEntry)

    @Query("DELETE FROM dictentry")
    suspend fun deleteAll()

    @Query(
        "Select * FROM dictentry WHERE " +
                "sourceLang = :sourceLang and " +
                "targetLang = :targetLang and " +
                "sourceWord = :sourceWord and " +
                "targetWord = :targetWord and " +
                "gram = :gram and " +
                "phon = :phon and " +
                "ind = :ind")
    suspend fun find(
        sourceLang: Language, targetLang: Language, sourceWord: String, targetWord: String, gram: String, phon: String,
        ind: String): List<DictEntry>

}

//TODO rework

