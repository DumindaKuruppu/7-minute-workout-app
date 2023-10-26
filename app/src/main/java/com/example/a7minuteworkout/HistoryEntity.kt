package com.example.a7minuteworkout

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "workout-data-table")
data class HistoryEntity(
    @PrimaryKey
    val date: String
)
