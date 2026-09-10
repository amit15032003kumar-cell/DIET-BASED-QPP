package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "food_items")
data class FoodItem(
  @PrimaryKey(autoGenerate = true)
  val id: Long = 0,
  val name: String,
  val category: String, // "Proteins", "Grains", "Vegetables", "Fruits", "Dairy", "Healthy Fats", "Snacks"
  val baseServingAmount: Double,
  val baseServingUnit: String, // "g", "ml", "cup", "slice", "piece"
  val caloriesPerServing: Int,
  val protein: Double,
  val carbs: Double,
  val fat: Double,
  val fiber: Double,
  val isCustom: Boolean = false
)
