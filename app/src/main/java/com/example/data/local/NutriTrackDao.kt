package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.FoodItem
import com.example.data.model.MealLogItem
import com.example.data.model.UserProfile
import com.example.data.model.WaterLog
import com.example.data.model.WeightLog
import kotlinx.coroutines.flow.Flow

@Dao
interface NutriTrackDao {

  // Meal Logs
  @Query("SELECT * FROM meal_logs WHERE date = :date ORDER BY timestamp ASC")
  fun getMealLogsByDate(date: String): Flow<List<MealLogItem>>

  @Query("SELECT * FROM meal_logs ORDER BY date DESC, timestamp DESC")
  fun getAllMealLogs(): Flow<List<MealLogItem>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertMealLog(item: MealLogItem): Long

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertAllMealLogs(items: List<MealLogItem>)

  @Query("DELETE FROM meal_logs WHERE id = :id")
  suspend fun deleteMealLog(id: Long)

  // Food Items (Nutritional Database)
  @Query("SELECT * FROM food_items ORDER BY isCustom DESC, name ASC")
  fun getAllFoodItems(): Flow<List<FoodItem>>

  @Query("SELECT * FROM food_items WHERE name LIKE '%' || :query || '%' OR category LIKE '%' || :query || '%' ORDER BY name ASC")
  fun searchFoodItems(query: String): Flow<List<FoodItem>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertFoodItem(item: FoodItem): Long

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertAllFoodItems(items: List<FoodItem>)

  @Query("SELECT COUNT(*) FROM food_items")
  suspend fun getFoodItemCount(): Int

  // Weight Logs
  @Query("SELECT * FROM weight_logs ORDER BY date ASC")
  fun getAllWeightLogs(): Flow<List<WeightLog>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertWeightLog(log: WeightLog): Long

  // Water Logs
  @Query("SELECT * FROM water_logs WHERE date = :date LIMIT 1")
  fun getWaterLogByDate(date: String): Flow<WaterLog?>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertOrUpdateWaterLog(log: WaterLog): Long

  // User Profile
  @Query("SELECT * FROM user_profile WHERE id = 1 LIMIT 1")
  fun getUserProfile(): Flow<UserProfile?>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertOrUpdateUserProfile(profile: UserProfile)
}
