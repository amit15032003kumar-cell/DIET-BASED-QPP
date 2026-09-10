package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "water_logs")
data class WaterLog(
  @PrimaryKey(autoGenerate = true)
  val id: Long = 0,
  val date: String, // "YYYY-MM-DD"
  val glasses: Int // e.g. 250ml per glass
)
