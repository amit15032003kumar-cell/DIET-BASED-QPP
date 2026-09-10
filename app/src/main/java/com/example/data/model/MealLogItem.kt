package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "meal_logs")
data class MealLogItem(
  @PrimaryKey(autoGenerate = true)
  val id: Long = 0,
  val date: String, // "YYYY-MM-DD"
  val mealType: String, // "BREAKFAST", "LUNCH", "DINNER", "SNACK"
  val foodName: String,
  val servingAmount: Double,
  val servingUnit: String,
  val calories: Int,
  val protein: Double,
  val carbs: Double,
  val fat: Double,
  val fiber: Double,
  val timestamp: Long = System.currentTimeMillis()
)
