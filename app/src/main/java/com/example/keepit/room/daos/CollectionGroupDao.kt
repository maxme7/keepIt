package com.example.keepit.room.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.keepit.room.entities.CollectionGroup

@Dao
interface CollectionGroupDao {
    @Query("SELECT * FROM collectionGroup")
    suspend fun getAll(): List<CollectionGroup>

    @Insert
    suspend fun insertAll(vararg collectionGroup: CollectionGroup)

    @Delete
    suspend fun delete(collectionGroup: CollectionGroup)

    @Query("DELETE FROM collectionGroup")
    suspend fun deleteAll()
}

//TODO rework

