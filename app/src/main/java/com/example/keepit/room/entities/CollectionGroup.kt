package com.example.keepit.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.keepit.enums.Language
import java.util.*

@Entity
data class CollectionGroup(
    var name: String,
    @PrimaryKey(autoGenerate = true) var id: Long = 0,
)
