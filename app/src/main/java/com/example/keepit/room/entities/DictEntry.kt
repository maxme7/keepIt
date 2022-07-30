package com.example.keepit.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.keepit.enums.Language
import java.util.*

@Entity
data class DictEntry(
//    @ColumnInfo(name = "customerName") var name: String?,
//    @ColumnInfo(name = "customerAddress") var address: String?,

    var url: String,
    var date: Date,
    var sourceLang: Language,
    var targetLang: Language,
    var sourceWord: String,
    var targetWord: String,

    // TODO potentially rename; just adopted from langenscheidt
    var gram: String?,
    var phon: String?,
    var ind: String?,

    var categories: Array<String>,
    @PrimaryKey(autoGenerate = true) var id: Long = 0,
)

// room entities: https://developer.android.com/training/data-storage/room/defining-data