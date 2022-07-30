package com.example.keepit.room.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.keepit.room.entities.DictEntryCollection

@Dao
interface DictEntryCollectionDao {
    @Query("SELECT * FROM dictEntryCollection")
    suspend fun getAll(): List<DictEntryCollection>

    @Insert
    suspend fun insertAll(vararg dictEntryCollection: DictEntryCollection)

    @Delete
    suspend fun delete(dictEntryCollection: DictEntryCollection)

    @Query("DELETE FROM dictEntryCollection")
    suspend fun deleteAll()

}

//TODO rework

