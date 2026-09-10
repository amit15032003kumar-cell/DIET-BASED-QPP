package com.example.ui.screens

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Opacity
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.local.InitialData
import com.example.data.model.MealLogItem
import com.example.localization.StringsLocalization
import com.example.ui.components.CalorieProgressRing
import com.example.ui.components.MacroNutrientBar
import com.example.ui.components.SmartwatchSyncCard
import com.example.ui.components.WaterIntakeCard
import com.example.ui.theme.CarbsColor
import com.example.ui.theme.Emerald500
import com.example.ui.theme.Emerald600
import com.example.ui.theme.ProteinColor
import com.example.ui.theme.WaterColor
import com.example.ui.viewmodel.NutriTrackViewModel

@Composable
fun DashboardScreen(
  viewModel: NutriTrackViewModel,
  onNavigateToProfile: () -> Unit,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  val profile by viewModel.userProfile.collectAsState()
  val selectedDate by viewModel.selectedDate.collectAsState()
  val mealLogs by viewModel.mealLogs.collectAsState()
  val waterLog by viewModel.currentWaterLog.collectAsState()
  val isSyncing by viewModel.isSyncingWearable.collectAsState()

  val totalCalories = remember(mealLogs) { mealLogs.sumOf { it.calories } }
  val totalProtein = remember(mealLogs) { mealLogs.sumOf { it.protein } }
  val totalCarbs = remember(mealLogs) { mealLogs.sumOf { it.carbs } }
  val totalFat = remember(mealLogs) { mealLogs.sumOf { it.fat } }
  val totalFiber = remember(mealLogs) { mealLogs.sumOf { it.fiber } }

  val photoPickerLauncher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.PickVisualMedia()
  ) { uri: Uri? ->
    if (uri != null) {
      viewModel.updateProfile(profile.copy(avatarUri = uri.toString()))
    }
  }

  val lang = profile.language

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .padding(horizontal = 16.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    item {
      Spacer(modifier = Modifier.height(8.dp))
      // Top User Header & Status Row
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          modifier = Modifier.clickable { onNavigateToProfile() }
        ) {
          // User Avatar with Photo Picker tap
          Box(
            modifier = Modifier
              .size(54.dp)
              .clip(CircleShape)
              .background(Emerald500.copy(alpha = 0.15f))
              .border(2.dp, Emerald500, CircleShape)
              .clickable {
                photoPickerLauncher.launch(
                  PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                )
              }
              .testTag("user_avatar_button"),
            contentAlignment = Alignment.Center
          ) {
            if (profile.avatarUri != null) {
              AsyncImage(
                model = profile.avatarUri,
                contentDescription = "User profile picture",
                modifier = Modifier.fillMaxSize().clip(CircleShape),
                contentScale = ContentScale.Crop
              )
            } else {
              Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "User profile photo",
                tint = Emerald600,
                modifier = Modifier.size(32.dp)
              )
            }
          }

          Spacer(modifier = Modifier.width(12.dp))

          Column {
            Text(
              text = profile.name,
              style = MaterialTheme.typography.titleMedium,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.onSurface
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
              Text(
                text = profile.dietaryGoal,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
              Text(
                text = " • ${profile.dietPreference}",
                style = MaterialTheme.typography.bodySmall,
                color = Emerald600,
                fontWeight = FontWeight.Medium
              )
            }
          }
        }

        // Notification & Offline Indicators
        Row(verticalAlignment = Alignment.CenterVertically) {
          // Offline Active Pill
          Surface(
            shape = CircleShape,
            color = MaterialTheme.colorScheme.surfaceVariant,
            modifier = Modifier.padding(end = 6.dp)
          ) {
            Row(
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Icon(
                imageVector = Icons.Default.CloudDone,
                contentDescription = "Offline cached and cloud backed up",
                tint = Emerald600,
                modifier = Modifier.size(14.dp)
              )
              Spacer(modifier = Modifier.width(4.dp))
              Text("Offline Ready", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
          }

          // Trigger test notification
          IconButton(
            onClick = {
              viewModel.triggerReminderNotification(context, "meal")
            },
            modifier = Modifier.size(36.dp).testTag("reminder_button")
          ) {
            Icon(
              imageVector = Icons.Default.NotificationsActive,
              contentDescription = "Meal reminder notification",
              tint = MaterialTheme.colorScheme.onSurfaceVariant,
              modifier = Modifier.size(20.dp)
            )
          }
        }
      }
    }

    // Date Navigation Row
    item {
      Surface(
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 1.dp,
        modifier = Modifier.fillMaxWidth()
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 6.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          IconButton(
            onClick = {
              viewModel.setDate(InitialData.getDateDaysAgo(1))
            }
          ) {
            Icon(Icons.Default.ChevronLeft, contentDescription = "Previous Day")
          }

          Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
              text = if (selectedDate == InitialData.getTodayDateString()) "Today" else selectedDate,
              style = MaterialTheme.typography.titleSmall,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.onSurface
            )
            Text(
              text = selectedDate,
              style = MaterialTheme.typography.labelSmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }

          IconButton(
            onClick = {
              viewModel.setDate(InitialData.getTodayDateString())
            }
          ) {
            Icon(Icons.Default.ChevronRight, contentDescription = "Next Day")
          }
        }
      }
    }

    // Calorie Progress Ring
    item {
      CalorieProgressRing(
        consumedCalories = totalCalories,
        burnedCalories = if (profile.smartwatchConnected) profile.syncedActiveCalories else 0,
        dailyTarget = profile.dailyCalorieTarget,
        unitSystem = profile.unitSystem
      )
    }

    // Connected Smartwatch Sync Card
    item {
      if (profile.smartwatchConnected) {
        SmartwatchSyncCard(
          deviceName = profile.smartwatchType,
          steps = profile.syncedSteps,
          activeCaloriesBurned = profile.syncedActiveCalories,
          isSyncing = isSyncing,
          lastSyncTimestamp = profile.lastSyncTimestamp,
          onSyncClicked = { viewModel.syncWearable() }
        )
      }
    }

    // Macro Nutrient Targets
    item {
      MacroNutrientBar(
        proteinConsumed = totalProtein,
        proteinTarget = profile.proteinTargetGrams,
        carbsConsumed = totalCarbs,
        carbsTarget = profile.carbsTargetGrams,
        fatConsumed = totalFat,
        fatTarget = profile.fatTargetGrams,
        fiberConsumed = totalFiber,
        fiberTarget = profile.fiberTargetGrams
      )
    }

    // Daily Hydration Water Tracker Card
    item {
      val glasses = waterLog?.glasses ?: 0
      val target = profile.waterTargetGlasses
      val context = LocalContext.current
      WaterIntakeCard(
        currentGlasses = glasses,
        targetGlasses = target,
        unitSystem = profile.unitSystem,
        onUpdateGlasses = { delta -> viewModel.updateWater(delta) },
        onSetExactGlasses = { count -> viewModel.setExactWaterGlasses(count) },
        onReminderClick = { viewModel.triggerReminderNotification(context, "water") }
      )
    }

    // Daily Meal Sections
    val mealTypes = listOf("BREAKFAST", "LUNCH", "DINNER", "SNACK")
    items(mealTypes, key = { it }) { mealType ->
      val itemsForMeal = remember(mealLogs, mealType) {
        mealLogs.filter { it.mealType.equals(mealType, ignoreCase = true) }
      }
      MealSectionCard(
        mealType = mealType,
        items = itemsForMeal,
        onAddFoodClicked = { viewModel.openAddFood(mealType) },
        onDeleteItem = { id -> viewModel.deleteMeal(id) }
      )
    }

    item {
      Spacer(modifier = Modifier.height(72.dp)) // Nav bar clearance
    }
  }
}

@Composable
fun MealSectionCard(
  mealType: String,
  items: List<MealLogItem>,
  onAddFoodClicked: () -> Unit,
  onDeleteItem: (Long) -> Unit
) {
  val title = when (mealType.uppercase()) {
    "BREAKFAST" -> "Breakfast"
    "LUNCH" -> "Lunch"
    "DINNER" -> "Dinner"
    else -> "Snacks & Treats"
  }

  val totalMealCalories = items.sumOf { it.calories }

  Surface(
    shape = RoundedCornerShape(20.dp),
    color = MaterialTheme.colorScheme.surface,
    tonalElevation = 2.dp,
    modifier = Modifier.fillMaxWidth()
  ) {
    Column(modifier = Modifier.padding(16.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
          )
          Text(
            text = "$totalMealCalories kcal total",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }

        OutlinedButton(
          onClick = onAddFoodClicked,
          shape = RoundedCornerShape(12.dp),
          colors = ButtonDefaults.outlinedButtonColors(contentColor = Emerald600),
          modifier = Modifier.testTag("add_meal_${mealType.lowercase()}_button")
        ) {
          Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
          Spacer(modifier = Modifier.width(4.dp))
          Text("Add Food", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
        }
      }

      if (items.isNotEmpty()) {
        Spacer(modifier = Modifier.height(12.dp))
        Divider(color = MaterialTheme.colorScheme.surfaceVariant)
        Spacer(modifier = Modifier.height(8.dp))

        items.forEach { item ->
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column(modifier = Modifier.weight(1f)) {
              Text(
                text = item.foodName,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
              )
              Text(
                text = "${item.servingAmount} ${item.servingUnit} • P: ${item.protein.toInt()}g  C: ${item.carbs.toInt()}g  F: ${item.fat.toInt()}g",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
              Text(
                text = "${item.calories} kcal",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
              )
              IconButton(
                onClick = { onDeleteItem(item.id) },
                modifier = Modifier.size(36.dp).testTag("delete_meal_${item.id}")
              ) {
                Icon(
                  imageVector = Icons.Default.Delete,
                  contentDescription = "Delete item",
                  tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                  modifier = Modifier.size(18.dp)
                )
              }
            }
          }
        }
      }
    }
  }
}
