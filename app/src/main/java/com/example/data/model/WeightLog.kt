package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weight_logs")
data class WeightLog(
  @PrimaryKey(autoGenerate = true)
  val id: Long = 0,
  val date: String, // "YYYY-MM-DD"
  val weightKg: Double,
  val timestamp: Long = System.currentTimeMillis()
)
