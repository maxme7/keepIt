package com.example.keepit.room.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.keepit.enums.Language
import com.example.keepit.room.entities.Collection

@Dao
interface CollectionDao {
    @Query("SELECT * FROM collection")
    suspend fun getAll(): List<Collection>

    @Insert
    suspend fun insertAll(vararg collection: Collection)

    @Delete
    suspend fun delete(collection: Collection)

    @Query("DELETE FROM collection")
    suspend fun deleteAll()

    @Query("Select * FROM collection WHERE sourceLang = :sourceLang and targetLang = :targetLang")
    suspend fun getCollectionByLang(sourceLang: Language, targetLang: Language): List<Collection>

}