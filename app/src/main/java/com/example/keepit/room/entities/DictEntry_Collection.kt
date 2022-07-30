package com.example.keepit.room.entities

import androidx.room.Entity

@Entity(primaryKeys = ["DictEntryId", "CollectionId"])
data class DictEntry_Collection(
    var DictEntryId: Long,
    var CollectionId: Long,
)
