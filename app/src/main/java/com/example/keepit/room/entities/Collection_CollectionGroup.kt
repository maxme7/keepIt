package com.example.keepit.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.keepit.enums.Language
import java.util.*

@Entity(primaryKeys = ["collectionId", "collectionGroupId"])
data class Collection_CollectionGroup(
    var collectionId: Long,
    var collectionGroupId: Long,
)
