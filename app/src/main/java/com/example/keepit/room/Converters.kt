package com.example.keepit.room

import androidx.room.TypeConverter
import java.util.*

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun fromArray(array: Array<String>): String {
        var concatString = ""
        array.forEach { concatString += "$it," }
        return concatString.substringBeforeLast(',')
    }

    @TypeConverter
    fun toArray(concatStrings: String): Array<String> {
        if(concatStrings == "") return emptyArray()
        return concatStrings.split(',').toTypedArray()
    }
}

// type converter: https://developer.android.com/training/data-storage/room/referencing-data
