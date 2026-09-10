package com.example.ui.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.RestaurantMenu
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.InitialData
import com.example.data.model.FoodAlternative
import com.example.data.model.MealPlan
import com.example.ui.theme.CarbsColor
import com.example.ui.theme.Emerald500
import com.example.ui.theme.Emerald600
import com.example.ui.theme.FatColor
import com.example.ui.theme.ProteinColor
import com.example.ui.viewmodel.NutriTrackViewModel

@Composable
fun MealPlansScreen(
  viewModel: NutriTrackViewModel,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  val profile by viewModel.userProfile.collectAsState()
  var selectedTab by remember { mutableIntStateOf(0) } // 0: Meal Plans, 1: Food Alternatives
  var selectedDietFilter by remember { mutableStateOf("All") }

  val dietFilters = listOf("All", "Balanced", "High-Protein", "Keto", "Mediterranean")

  val filteredPlans = if (selectedDietFilter == "All") {
    InitialData.personalizedMealPlans
  } else {
    InitialData.personalizedMealPlans.filter { it.dietType.equals(selectedDietFilter, ignoreCase = true) }
  }

  Column(
    modifier = modifier
      .fillMaxSize()
      .padding(horizontal = 16.dp)
  ) {
    Spacer(modifier = Modifier.height(12.dp))

    Text(
      text = "Nutrition Plans & Smart Swaps",
      style = MaterialTheme.typography.titleLarge,
      fontWeight = FontWeight.Bold,
      color = MaterialTheme.colorScheme.onSurface
    )
    Text(
      text = "Personalized diet templates and healthier alternatives",
      style = MaterialTheme.typography.bodySmall,
      color = MaterialTheme.colorScheme.onSurfaceVariant
    )

    Spacer(modifier = Modifier.height(14.dp))

    // Top Tabs: Meal Plans vs Healthier Food Swaps
    TabRow(
      selectedTabIndex = selectedTab,
      containerColor = MaterialTheme.colorScheme.surfaceVariant,
      modifier = Modifier.background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(14.dp))
    ) {
      Tab(
        selected = selectedTab == 0,
        onClick = { selectedTab = 0 },
        text = { Text("Meal Plans", fontWeight = FontWeight.Bold) },
        modifier = Modifier.testTag("tab_meal_plans")
      )
      Tab(
        selected = selectedTab == 1,
        onClick = { selectedTab = 1 },
        text = { Text("Healthier Alternatives", fontWeight = FontWeight.Bold) },
        modifier = Modifier.testTag("tab_food_swaps")
      )
    }

    Spacer(modifier = Modifier.height(12.dp))

    if (selectedTab == 0) {
      // Filter Chips
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        dietFilters.forEach { filter ->
          FilterChip(
            selected = selectedDietFilter == filter,
            onClick = { selectedDietFilter = filter },
            label = { Text(filter, fontSize = 12.sp) },
            colors = FilterChipDefaults.filterChipColors(
              selectedContainerColor = Emerald500.copy(alpha = 0.2f),
              selectedLabelColor = Emerald600
            ),
            shape = RoundedCornerShape(10.dp)
          )
        }
      }

      Spacer(modifier = Modifier.height(12.dp))

      LazyColumn(
        verticalArrangement = Arrangement.spacedBy(14.dp),
        modifier = Modifier.weight(1f)
      ) {
        items(filteredPlans) { plan ->
          MealPlanCard(
            plan = plan,
            onLogPlan = {
              viewModel.logMealPlanForToday(plan)
              Toast.makeText(context, "Logged ${plan.title} for today!", Toast.LENGTH_SHORT).show()
            }
          )
        }
        item {
          Spacer(modifier = Modifier.height(72.dp))
        }
      }
    } else {
      // Healthier Food Swaps Tab
      LazyColumn(
        verticalArrangement = Arrangement.spacedBy(14.dp),
        modifier = Modifier.weight(1f)
      ) {
        item {
          Surface(
            shape = RoundedCornerShape(16.dp),
            color = Emerald500.copy(alpha = 0.12f),
            modifier = Modifier.fillMaxWidth()
          ) {
            Row(
              modifier = Modifier.padding(14.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Box(
                modifier = Modifier
                  .size(36.dp)
                  .background(Emerald600, CircleShape),
                contentAlignment = Alignment.Center
              ) {
                Icon(Icons.Default.SwapHoriz, contentDescription = null, tint = androidx.compose.ui.graphics.Color.White)
              }
              Spacer(modifier = Modifier.width(12.dp))
              Text(
                text = "Substitute calorie-dense staples with high-satiety, nutrient-dense swaps to achieve effortless deficit.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface
              )
            }
          }
        }

        items(InitialData.foodAlternatives) { alt ->
          FoodAlternativeCard(
            alternative = alt,
            onLogSwap = {
              viewModel.logMeal(
                foodName = alt.healthierAlternative,
                mealType = "SNACK",
                servingAmount = 1.0,
                servingUnit = "portion",
                calories = alt.alternativeCalories,
                protein = alt.alternativeProtein,
                carbs = 10.0,
                fat = 2.0,
                fiber = alt.alternativeFiber
              )
              Toast.makeText(context, "Logged ${alt.healthierAlternative}!", Toast.LENGTH_SHORT).show()
            }
          )
        }

        item {
          Spacer(modifier = Modifier.height(72.dp))
        }
      }
    }
  }
}

@Composable
fun MealPlanCard(
  plan: MealPlan,
  onLogPlan: () -> Unit
) {
  var isExpanded by remember { mutableStateOf(false) }

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
        verticalAlignment = Alignment.Top
      ) {
        Column(modifier = Modifier.weight(1f)) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
              text = plan.title,
              style = MaterialTheme.typography.titleMedium,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.width(8.dp))
            Surface(
              shape = CircleShape,
              color = Emerald500.copy(alpha = 0.15f)
            ) {
              Text(
                text = plan.dietType,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = Emerald600,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
              )
            }
          }

          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text = "${plan.targetCalories} kcal • P: ${plan.targetProteinGrams}g  C: ${plan.targetCarbsGrams}g  F: ${plan.targetFatGrams}g",
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }

        IconButton(onClick = { isExpanded = !isExpanded }) {
          Icon(
            imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
            contentDescription = "Expand plan details"
          )
        }
      }

      Spacer(modifier = Modifier.height(8.dp))
      Text(
        text = plan.description,
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )

      AnimatedVisibility(visible = isExpanded) {
        Column(modifier = Modifier.padding(top = 12.dp)) {
          Divider(color = MaterialTheme.colorScheme.surfaceVariant)
          Spacer(modifier = Modifier.height(8.dp))

          plan.meals.forEach { meal ->
            Column(modifier = Modifier.padding(vertical = 4.dp)) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
              ) {
                Text(
                  text = "${meal.mealType}: ${meal.title}",
                  fontWeight = FontWeight.Bold,
                  fontSize = 13.sp,
                  color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                  text = "${meal.calories} kcal",
                  fontWeight = FontWeight.Bold,
                  fontSize = 13.sp,
                  color = Emerald600
                )
              }
              Text(
                text = meal.ingredients,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }
          }

          Spacer(modifier = Modifier.height(12.dp))
          Button(
            onClick = onLogPlan,
            modifier = Modifier.fillMaxWidth().testTag("log_plan_button"),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Emerald600)
          ) {
            Icon(Icons.Default.RestaurantMenu, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("Log This Full Day Menu", fontWeight = FontWeight.Bold)
          }
        }
      }
    }
  }
}

@Composable
fun FoodAlternativeCard(
  alternative: FoodAlternative,
  onLogSwap: () -> Unit
) {
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
        Surface(
          shape = CircleShape,
          color = MaterialTheme.colorScheme.surfaceVariant
        ) {
          Text(
            text = alternative.category,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
          )
        }

        Surface(
          shape = CircleShape,
          color = Emerald500.copy(alpha = 0.15f)
        ) {
          Text(
            text = "Save -${alternative.caloriesSavedPerServing} kcal",
            fontSize = 12.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Emerald600,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
          )
        }
      }

      Spacer(modifier = Modifier.height(12.dp))

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        // Original Food
        Column(modifier = Modifier.weight(1f)) {
          Text("Standard Option", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
          Text(alternative.originalFood, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
          Text("${alternative.originalCalories} kcal", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }

        Icon(
          imageVector = Icons.Default.SwapHoriz,
          contentDescription = "Swap with",
          tint = Emerald600,
          modifier = Modifier.size(24.dp).padding(horizontal = 4.dp)
        )

        // Healthier Swap
        Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.End) {
          Text("Smart Swap", style = MaterialTheme.typography.labelSmall, color = Emerald600, fontWeight = FontWeight.Bold)
          Text(alternative.healthierAlternative, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, color = Emerald600)
          Text("${alternative.alternativeCalories} kcal", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Emerald600)
        }
      }

      Spacer(modifier = Modifier.height(10.dp))
      Text(
        text = alternative.benefitDescription,
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )

      Spacer(modifier = Modifier.height(12.dp))
      OutlinedButton(
        onClick = onLogSwap,
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.outlinedButtonColors(contentColor = Emerald600),
        modifier = Modifier.fillMaxWidth().testTag("log_swap_button")
      ) {
        Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp))
        Spacer(modifier = Modifier.width(6.dp))
        Text("Log Healthy Alternative Now", fontSize = 12.sp, fontWeight = FontWeight.Bold)
      }
    }
  }
}
