package com.example.keepit.room.entities

import androidx.room.Entity

@Entity(primaryKeys = ["DictEntryId", "CollectionId"])
data class DictEntryCollection(
    var DictEntryId: Long,
    var CollectionId: Long,
)
