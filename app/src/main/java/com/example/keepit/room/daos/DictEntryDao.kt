package com.example.keepit.room.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.keepit.enums.Language
import com.example.keepit.room.entities.DictEntry

@Dao
interface DictEntryDao {
    @Query("SELECT * FROM dictentry")
    suspend fun getAll(): List<DictEntry>

    @Query("SELECT * FROM dictentry WHERE sourceLang = :sourceLang AND targetLang = :targetLang")
    suspend fun getEntriesByLang(sourceLang: Language, targetLang: Language): List<DictEntry>

//    @Query("SELECT * FROM dictentry WHERE cid IN (:dictEntryIds)")
//    suspend fun getByIds(dictEntryIds: IntArray): List<DictEntry>

    @Insert
    suspend fun insertAll(vararg dictEntries: DictEntry)

    @Delete
    suspend fun delete(dictEntries: DictEntry)

    @Query("DELETE FROM dictentry")
    suspend fun deleteAll()

    @Query(
        "Select * FROM dictentry WHERE " +
                "sourceLang = :sourceLang AND " +
                "targetLang = :targetLang AND " +
                "sourceWord = :sourceWord AND " +
                "targetWord = :targetWord AND " +
                "gram = :gram AND " +
                "phon = :phon AND " +
                "ind = :ind")
    suspend fun find(
        sourceLang: Language, targetLang: Language, sourceWord: String, targetWord: String, gram: String, phon: String,
        ind: String): List<DictEntry>

}

//TODO rework

