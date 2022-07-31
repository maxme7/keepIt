package com.example.keepit.room.daos

import androidx.room.*
import com.example.keepit.room.entities.CollectionCollectionGroup

@Dao
interface CollectionCollectionGroupDao {

    @Query("SELECT * FROM collectionCollectionGroup")
    suspend fun getAll(): List<CollectionCollectionGroup>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(vararg collectionCollectionGroup: CollectionCollectionGroup)

    @Delete
    suspend fun delete(collectionCollectionGroup: CollectionCollectionGroup)

    @Query("DELETE FROM collectionCollectionGroup")
    suspend fun deleteAll()

}

