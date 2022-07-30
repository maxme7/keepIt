package com.example.keepit.room.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.keepit.room.entities.CollectionCollectionGroup

@Dao
interface CollectionCollectionGroupDao {

    @Query("SELECT * FROM collectionCollectionGroup")
    suspend fun getAll(): List<CollectionCollectionGroup>

    @Insert
    suspend fun insertAll(vararg collectionCollectionGroup: CollectionCollectionGroup)

    @Delete
    suspend fun delete(collectionCollectionGroup: CollectionCollectionGroup)

    @Query("DELETE FROM collectionCollectionGroup")
    suspend fun deleteAll()

}

