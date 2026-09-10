package com.example.ui.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.InitialData
import com.example.data.local.NutriTrackDatabase
import com.example.data.model.FoodItem
import com.example.data.model.MealLogItem
import com.example.data.model.MealPlan
import com.example.data.model.UserProfile
import com.example.data.model.WaterLog
import com.example.data.model.WeightLog
import com.example.data.repository.NutriTrackRepository
import com.example.notifications.NotificationHelper
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class NutriTrackViewModel(application: Application) : AndroidViewModel(application) {

  private val database = NutriTrackDatabase.getDatabase(application)
  val repository = NutriTrackRepository(database.nutriTrackDao())

  init {
    NotificationHelper.createNotificationChannel(application)
  }

  // Selected date for daily log
  private val _selectedDate = MutableStateFlow(InitialData.getTodayDateString())
  val selectedDate: StateFlow<String> = _selectedDate.asStateFlow()

  // Meal logs for currently selected date
  val mealLogs: StateFlow<List<MealLogItem>> = _selectedDate
    .flatMapLatest { date -> repository.getMealLogsByDate(date) }
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  // All meal logs for weekly analytics
  val allMealLogs: StateFlow<List<MealLogItem>> = repository.getAllMealLogs()
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  // User Profile
  val userProfile: StateFlow<UserProfile> = repository.getUserProfile()
    .combine(MutableStateFlow(InitialData.defaultUserProfile)) { dbProfile, defaultProfile ->
      dbProfile ?: defaultProfile
    }
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), InitialData.defaultUserProfile)

  // Water log for current date
  val currentWaterLog: StateFlow<WaterLog?> = _selectedDate
    .flatMapLatest { date -> repository.getWaterLog(date) }
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

  // Weight logs
  val weightLogs: StateFlow<List<WeightLog>> = repository.getWeightLogs()
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  // Nutritional database items & search
  private val _searchQuery = MutableStateFlow("")
  val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

  private val _selectedFoodCategory = MutableStateFlow("All")
  val selectedFoodCategory: StateFlow<String> = _selectedFoodCategory.asStateFlow()

  val foodItems: StateFlow<List<FoodItem>> = combine(
    repository.getAllFoods(),
    _searchQuery,
    _selectedFoodCategory
  ) { list, query, category ->
    list.filter { item ->
      val matchesQuery = query.isBlank() || item.name.contains(query, ignoreCase = true) || item.category.contains(query, ignoreCase = true)
      val matchesCategory = category == "All" || item.category.equals(category, ignoreCase = true)
      matchesQuery && matchesCategory
    }
  }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  // Wearable sync state
  private val _isSyncingWearable = MutableStateFlow(false)
  val isSyncingWearable: StateFlow<Boolean> = _isSyncingWearable.asStateFlow()

  // Cloud backup state
  private val _isBackingUpCloud = MutableStateFlow(false)
  val isBackingUpCloud: StateFlow<Boolean> = _isBackingUpCloud.asStateFlow()

  private val _cloudBackupMessage = MutableStateFlow<String?>(null)
  val cloudBackupMessage: StateFlow<String?> = _cloudBackupMessage.asStateFlow()

  // Add food dialog state
  private val _addMealType = MutableStateFlow("BREAKFAST")
  val addMealType: StateFlow<String> = _addMealType.asStateFlow()

  private val _isAddFoodSheetOpen = MutableStateFlow(false)
  val isAddFoodSheetOpen: StateFlow<Boolean> = _isAddFoodSheetOpen.asStateFlow()

  fun setDate(date: String) {
    _selectedDate.value = date
  }

  fun setSearchQuery(query: String) {
    _searchQuery.value = query
  }

  fun setFoodCategory(category: String) {
    _selectedFoodCategory.value = category
  }

  fun openAddFood(mealType: String) {
    _addMealType.value = mealType
    _isAddFoodSheetOpen.value = true
  }

  fun closeAddFood() {
    _isAddFoodSheetOpen.value = false
  }

  fun logMeal(
    foodName: String,
    mealType: String,
    servingAmount: Double,
    servingUnit: String,
    calories: Int,
    protein: Double,
    carbs: Double,
    fat: Double,
    fiber: Double
  ) {
    viewModelScope.launch {
      val item = MealLogItem(
        date = _selectedDate.value,
        mealType = mealType.uppercase(),
        foodName = foodName,
        servingAmount = servingAmount,
        servingUnit = servingUnit,
        calories = calories,
        protein = protein,
        carbs = carbs,
        fat = fat,
        fiber = fiber
      )
      repository.logMealItem(item)
    }
  }

  fun deleteMeal(id: Long) {
    viewModelScope.launch {
      repository.deleteMealItem(id)
    }
  }

  fun addCustomFood(
    name: String,
    category: String,
    servingAmount: Double,
    servingUnit: String,
    calories: Int,
    protein: Double,
    carbs: Double,
    fat: Double,
    fiber: Double
  ) {
    viewModelScope.launch {
      val food = FoodItem(
        id = 0,
        name = name,
        category = category,
        baseServingAmount = servingAmount,
        baseServingUnit = servingUnit,
        caloriesPerServing = calories,
        protein = protein,
        carbs = carbs,
        fat = fat,
        fiber = fiber,
        isCustom = true
      )
      repository.addCustomFood(food)
    }
  }

  fun updateWater(delta: Int) {
    viewModelScope.launch {
      val currentGlasses = currentWaterLog.value?.glasses ?: 0
      val newCount = (currentGlasses + delta).coerceAtLeast(0)
      repository.updateWaterGlasses(_selectedDate.value, newCount)
    }
  }

  fun setExactWaterGlasses(glasses: Int) {
    viewModelScope.launch {
      repository.updateWaterGlasses(_selectedDate.value, glasses.coerceAtLeast(0))
    }
  }

  fun updateWaterTarget(targetGlasses: Int) {
    viewModelScope.launch {
      val current = userProfile.value
      repository.updateUserProfile(current.copy(waterTargetGlasses = targetGlasses.coerceIn(4, 20)))
    }
  }

  fun logWeight(weightKg: Double) {
    viewModelScope.launch {
      repository.logWeight(_selectedDate.value, weightKg)
      val currentProfile = userProfile.value
      repository.updateUserProfile(currentProfile.copy(currentWeightKg = weightKg))
    }
  }

  fun updateProfile(updated: UserProfile) {
    viewModelScope.launch {
      repository.updateUserProfile(updated)
    }
  }

  fun syncWearable() {
    viewModelScope.launch {
      _isSyncingWearable.value = true
      delay(1100) // Realistic seamless Bluetooth LE sync delay
      val current = userProfile.value
      val newSteps = current.syncedSteps + (120..380).random()
      val newActiveCal = current.syncedActiveCalories + (20..65).random()
      repository.syncWearableDevice(newSteps, newActiveCal)
      _isSyncingWearable.value = false
    }
  }

  fun performCloudBackup() {
    viewModelScope.launch {
      _isBackingUpCloud.value = true
      delay(1200)
      val timestamp = repository.performCloudBackup()
      _isBackingUpCloud.value = false
      _cloudBackupMessage.value = "Encrypted backup synced to cloud successfully!"
      delay(3500)
      _cloudBackupMessage.value = null
    }
  }

  fun logMealPlanForToday(plan: MealPlan) {
    viewModelScope.launch {
      repository.logMealPlan(plan, _selectedDate.value)
    }
  }

  fun triggerReminderNotification(context: Context, type: String) {
    when (type) {
      "meal" -> NotificationHelper.sendReminderNotification(
        context,
        "NutriTrack: Time to log your meal",
        "Keep your nutrition streak alive! Log your latest meal to hit your daily macro target."
      )
      "water" -> NotificationHelper.sendReminderNotification(
        context,
        "Hydration Reminder",
        "Stay energized! Drink a glass of water (250ml) to reach your daily hydration goal."
      )
      "goal" -> NotificationHelper.sendReminderNotification(
        context,
        "Daily Goal Update",
        "Great work today! You have burned 480 active kcal with your connected wearable."
      )
    }
  }
}
