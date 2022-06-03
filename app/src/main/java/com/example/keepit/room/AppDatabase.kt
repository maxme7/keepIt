package com.example.keepit.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [DictEntry::class], version = 1) //update array when new entity classes created
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun dictEntryDao(): DictEntryDao
}