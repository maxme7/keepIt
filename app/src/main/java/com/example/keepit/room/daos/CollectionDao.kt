package com.example.keepit.room.daos

import androidx.room.*
import com.example.keepit.enums.Language
import com.example.keepit.room.entities.Collection
import com.example.keepit.room.entities.DictEntry

@Dao
interface CollectionDao {
    @Query("SELECT * FROM collection")
    suspend fun getAll(): List<Collection>

    @Insert
    suspend fun insertAll(vararg collections: Collection)

    @Delete
    suspend fun delete(collection: Collection)

    @Query("DELETE FROM collection")
    suspend fun deleteAll()

    @Query("Select * FROM collection WHERE sourceLang = :sourceLang AND targetLang = :targetLang")
    suspend fun getCollectionByLang(sourceLang: Language, targetLang: Language): List<Collection>

    @Query("Select * FROM dictentry WHERE dictentry.sourceLang = :sourceLang AND dictentry.targetLang = :targetLang EXCEPT " +
            "SELECT dictentry.* from dictentry, dictentrycollection, collection WHERE " +
            "dictentry.id = dictentrycollection.DictEntryId AND  dictentrycollection.CollectionId = collection.id")
    suspend fun getUnassignedDictEntries(sourceLang: Language, targetLang: Language): List<DictEntry>

}
