package com.example.keepit.room

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.keepit.room.daos.*
import com.example.keepit.room.entities.Collection
import com.example.keepit.room.entities.CollectionGroup
import com.example.keepit.room.entities.Collection_CollectionGroup
import com.example.keepit.room.entities.DictEntry
import com.example.keepit.room.entities.DictEntry_Collection

@Database(entities = [DictEntry::class, Collection::class, DictEntry_Collection::class, CollectionGroup::class, Collection_CollectionGroup::class],
    version = 2
) //update array when new entity classes created
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun dictEntryDao(): DictEntryDao
    abstract fun collectionDao(): CollectionDao
    abstract fun collectionGroupDao(): CollectionGroupDao
    abstract fun collectionCollectionGroupDao(): Collection_CollectionGroupDao
    abstract fun dictEntryCollectionDao(): DictEntry_CollectionDao
}
