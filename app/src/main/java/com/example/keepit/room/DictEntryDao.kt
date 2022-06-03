package com.example.keepit.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

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
}

//TODO rework

