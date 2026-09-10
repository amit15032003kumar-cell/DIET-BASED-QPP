package com.example.data.repository

import com.example.data.local.InitialData
import com.example.data.local.NutriTrackDao
import com.example.data.model.FoodItem
import com.example.data.model.MealLogItem
import com.example.data.model.MealPlan
import com.example.data.model.UserProfile
import com.example.data.model.WaterLog
import com.example.data.model.WeightLog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NutriTrackRepository(private val dao: NutriTrackDao) {

  init {
    CoroutineScope(Dispatchers.IO).launch {
      initializeDatabaseIfNeeded()
    }
  }

  private suspend fun initializeDatabaseIfNeeded() {
    val count = dao.getFoodItemCount()
    if (count == 0) {
      dao.insertAllFoodItems(InitialData.preseededFoods)
      dao.insertAllMealLogs(InitialData.getInitialMealLogs())
      for (weight in InitialData.getInitialWeightLogs()) {
        dao.insertWeightLog(weight)
      }
      dao.insertOrUpdateWaterLog(InitialData.getInitialWaterLog())
    }
    val profile = dao.getUserProfile().firstOrNull()
    if (profile == null) {
      dao.insertOrUpdateUserProfile(InitialData.defaultUserProfile)
    }
  }

  fun getMealLogsByDate(date: String): Flow<List<MealLogItem>> = dao.getMealLogsByDate(date)

  fun getAllMealLogs(): Flow<List<MealLogItem>> = dao.getAllMealLogs()

  suspend fun logMealItem(item: MealLogItem): Long = withContext(Dispatchers.IO) {
    dao.insertMealLog(item)
  }

  suspend fun deleteMealItem(id: Long) = withContext(Dispatchers.IO) {
    dao.deleteMealLog(id)
  }

  fun getAllFoods(): Flow<List<FoodItem>> = dao.getAllFoodItems()

  fun searchFoods(query: String): Flow<List<FoodItem>> = dao.searchFoodItems(query)

  suspend fun addCustomFood(food: FoodItem): Long = withContext(Dispatchers.IO) {
    dao.insertFoodItem(food.copy(isCustom = true))
  }

  fun getUserProfile(): Flow<UserProfile?> = dao.getUserProfile()

  suspend fun updateUserProfile(profile: UserProfile) = withContext(Dispatchers.IO) {
    dao.insertOrUpdateUserProfile(profile)
  }

  fun getWaterLog(date: String): Flow<WaterLog?> = dao.getWaterLogByDate(date)

  suspend fun updateWaterGlasses(date: String, newCount: Int) = withContext(Dispatchers.IO) {
    val count = if (newCount < 0) 0 else newCount
    dao.insertOrUpdateWaterLog(WaterLog(date = date, glasses = count))
  }

  fun getWeightLogs(): Flow<List<WeightLog>> = dao.getAllWeightLogs()

  suspend fun logWeight(date: String, weightKg: Double) = withContext(Dispatchers.IO) {
    dao.insertWeightLog(WeightLog(date = date, weightKg = weightKg))
  }

  suspend fun syncWearableDevice(steps: Int, activeCalories: Int) = withContext(Dispatchers.IO) {
    val current = dao.getUserProfile().firstOrNull() ?: InitialData.defaultUserProfile
    val updated = current.copy(
      lastSyncTimestamp = System.currentTimeMillis(),
      syncedSteps = steps,
      syncedActiveCalories = activeCalories
    )
    dao.insertOrUpdateUserProfile(updated)
  }

  suspend fun performCloudBackup(): Long = withContext(Dispatchers.IO) {
    val now = System.currentTimeMillis()
    val current = dao.getUserProfile().firstOrNull() ?: InitialData.defaultUserProfile
    dao.insertOrUpdateUserProfile(current.copy(lastCloudBackupTimestamp = now))
    now
  }

  suspend fun logMealPlan(plan: MealPlan, date: String) = withContext(Dispatchers.IO) {
    val logs = plan.meals.map { meal ->
      MealLogItem(
        date = date,
        mealType = meal.mealType.uppercase(),
        foodName = meal.title,
        servingAmount = 1.0,
        servingUnit = "serving",
        calories = meal.calories,
        protein = meal.protein,
        carbs = meal.carbs,
        fat = meal.fat,
        fiber = meal.fiber
      )
    }
    dao.insertAllMealLogs(logs)
  }
}
