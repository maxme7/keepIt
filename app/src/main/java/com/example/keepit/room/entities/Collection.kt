package com.example.keepit.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.keepit.enums.Language

@Entity data class Collection(
    var name: String,
    var sourceLang: Language,
    var targetLang: Language,
    @PrimaryKey(autoGenerate = true) var id: Long = 0,
)
