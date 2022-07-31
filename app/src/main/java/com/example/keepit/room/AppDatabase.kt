package com.example.keepit.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.keepit.room.daos.*
import com.example.keepit.room.entities.Collection
import com.example.keepit.room.entities.CollectionGroup
import com.example.keepit.room.entities.CollectionCollectionGroup
import com.example.keepit.room.entities.DictEntry
import com.example.keepit.room.entities.DictEntryCollection
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Database(entities = [DictEntry::class, Collection::class, DictEntryCollection::class, CollectionGroup::class, CollectionCollectionGroup::class],
    version = 3
) //update array when new entity classes created
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun dictEntryDao(): DictEntryDao
    abstract fun collectionDao(): CollectionDao
    abstract fun collectionGroupDao(): CollectionGroupDao
    abstract fun collectionCollectionGroupDao(): CollectionCollectionGroupDao
    abstract fun dictEntryCollectionDao(): DictEntryCollectionDao
}

fun getDb(context: Context): AppDatabase {
    return Room.databaseBuilder(context, AppDatabase::class.java, "db").build()
}

suspend fun clearDb(context: Context) {
    withContext(Dispatchers.Default) {
        getDb(context).clearAllTables()
    }
}


//coroutines: https://www.youtube.com/watch?v=2QInrEaXyMo