package com.example.keepit.room.entities

import androidx.room.Entity

@Entity(primaryKeys = ["collectionId", "collectionGroupId"])
data class CollectionCollectionGroup(
    var collectionId: Long,
    var collectionGroupId: Long,
)
