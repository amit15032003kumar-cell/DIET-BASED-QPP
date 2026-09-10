package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.model.FoodItem
import com.example.data.model.MealLogItem
import com.example.data.model.UserProfile
import com.example.data.model.WaterLog
import com.example.data.model.WeightLog

@Database(
  entities = [
    MealLogItem::class,
    FoodItem::class,
    WeightLog::class,
    WaterLog::class,
    UserProfile::class
  ],
  version = 1,
  exportSchema = false
)
abstract class NutriTrackDatabase : RoomDatabase() {
  abstract fun nutriTrackDao(): NutriTrackDao

  companion object {
    @Volatile
    private var INSTANCE: NutriTrackDatabase? = null

    fun getDatabase(context: Context): NutriTrackDatabase {
      return INSTANCE ?: synchronized(this) {
        val instance = Room.databaseBuilder(
          context.applicationContext,
          NutriTrackDatabase::class.java,
          "nutritrack_database"
        ).fallbackToDestructiveMigration().build()
        INSTANCE = instance
        instance
      }
    }
  }
}
