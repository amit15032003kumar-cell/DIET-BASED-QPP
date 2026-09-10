package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile")
data class UserProfile(
  @PrimaryKey
  val id: Int = 1,
  val name: String = "Alex Rivera",
  val avatarUri: String? = null,
  val targetWeightKg: Double = 68.0,
  val currentWeightKg: Double = 72.5,
  val heightCm: Double = 175.0,
  val dailyCalorieTarget: Int = 2100,
  val proteinTargetGrams: Int = 140,
  val carbsTargetGrams: Int = 210,
  val fatTargetGrams: Int = 65,
  val fiberTargetGrams: Int = 30,
  val waterTargetGlasses: Int = 8,
  val dietaryGoal: String = "Weight Loss", // "Weight Loss", "Muscle Gain", "Maintenance", "Heart Health"
  val dietPreference: String = "Balanced", // "Balanced", "High-Protein", "Keto", "Mediterranean", "Vegan"
  val unitSystem: String = "METRIC", // "METRIC" (kg, cm) or "IMPERIAL" (lbs, in)
  val language: String = "en", // "en", "es", "fr", "de", "hi"
  val themeMode: String = "SYSTEM", // "SYSTEM", "LIGHT", "DARK"
  val smartwatchConnected: Boolean = true,
  val smartwatchType: String = "Pixel Watch 3",
  val lastSyncTimestamp: Long = System.currentTimeMillis() - 1000 * 60 * 18,
  val syncedSteps: Int = 8420,
  val syncedActiveCalories: Int = 460,
  val lastCloudBackupTimestamp: Long = System.currentTimeMillis() - 1000 * 60 * 60 * 4
)
