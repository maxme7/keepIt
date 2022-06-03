package com.example.keepit.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.keepit.enums.Language
import java.util.*

@Entity(primaryKeys = ["sourceWord", "targetWord"])
data class DictEntry(
//    @PrimaryKey(autoGenerate = true) var id: Int,
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

    var categories: Array<String>
)

// room entities: https://developer.android.com/training/data-storage/room/defining-data