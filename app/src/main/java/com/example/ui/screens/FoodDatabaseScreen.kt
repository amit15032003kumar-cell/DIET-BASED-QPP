package com.example.ui.screens

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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.FoodItem
import com.example.ui.theme.CarbsColor
import com.example.ui.theme.Emerald500
import com.example.ui.theme.Emerald600
import com.example.ui.theme.FatColor
import com.example.ui.theme.FiberColor
import com.example.ui.theme.ProteinColor
import com.example.ui.viewmodel.NutriTrackViewModel

@Composable
fun FoodDatabaseScreen(
  viewModel: NutriTrackViewModel,
  modifier: Modifier = Modifier
) {
  val searchQuery by viewModel.searchQuery.collectAsState()
  val selectedCategory by viewModel.selectedFoodCategory.collectAsState()
  val foodItems by viewModel.foodItems.collectAsState()
  val addMealType by viewModel.addMealType.collectAsState()

  var showCustomFoodDialog by remember { mutableStateOf(false) }
  var selectedFoodForLog by remember { mutableStateOf<FoodItem?>(null) }
  var currentMealTypeChoice by remember { mutableStateOf(addMealType) }

  val categories = listOf("All", "Proteins", "Grains", "Vegetables", "Fruits", "Dairy", "Healthy Fats", "Snacks")

  Column(
    modifier = modifier
      .fillMaxSize()
      .padding(horizontal = 16.dp)
  ) {
    Spacer(modifier = Modifier.height(12.dp))

    // Header & Add Custom Food Button
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Column {
        Text(
          text = "Nutritional Database",
          style = MaterialTheme.typography.titleLarge,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.onSurface
        )
        Text(
          text = "Accurate ingredient lookup & custom food logger",
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }

      Button(
        onClick = { showCustomFoodDialog = true },
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Emerald600),
        modifier = Modifier.testTag("add_custom_food_btn")
      ) {
        Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
        Spacer(modifier = Modifier.width(4.dp))
        Text("Custom Food", fontSize = 12.sp, fontWeight = FontWeight.Bold)
      }
    }

    Spacer(modifier = Modifier.height(12.dp))

    // Search Input Field
    OutlinedTextField(
      value = searchQuery,
      onValueChange = { viewModel.setSearchQuery(it) },
      modifier = Modifier
        .fillMaxWidth()
        .testTag("food_search_input"),
      placeholder = { Text("Search ingredients (e.g. Avocado, Salmon, Oats)") },
      leadingIcon = {
        Icon(Icons.Default.Search, contentDescription = "Search", tint = Emerald600)
      },
      trailingIcon = {
        if (searchQuery.isNotEmpty()) {
          IconButton(onClick = { viewModel.setSearchQuery("") }) {
            Icon(Icons.Default.Close, contentDescription = "Clear search")
          }
        }
      },
      shape = RoundedCornerShape(16.dp),
      singleLine = true
    )

    Spacer(modifier = Modifier.height(10.dp))

    // Horizontal category chips
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .horizontalScroll(rememberScrollState()),
      horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      categories.forEach { category ->
        val isSelected = selectedCategory == category
        FilterChip(
          selected = isSelected,
          onClick = { viewModel.setFoodCategory(category) },
          label = { Text(category, fontSize = 12.sp) },
          colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = Emerald500.copy(alpha = 0.2f),
            selectedLabelColor = Emerald600
          ),
          shape = RoundedCornerShape(10.dp)
        )
      }
    }

    Spacer(modifier = Modifier.height(12.dp))

    // Food Items List
    if (foodItems.isEmpty()) {
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .weight(1f),
        contentAlignment = Alignment.Center
      ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
          Icon(
            imageVector = Icons.Default.Restaurant,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f),
            modifier = Modifier.size(54.dp)
          )
          Spacer(modifier = Modifier.height(12.dp))
          Text(
            text = "No ingredients found",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
          )
          Text(
            text = "Try a different search term or add a custom food item.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
      }
    } else {
      LazyColumn(
        modifier = Modifier.weight(1f),
        verticalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        items(foodItems) { food ->
          FoodItemCard(
            food = food,
            onQuickLog = {
              selectedFoodForLog = food
            }
          )
        }
        item {
          Spacer(modifier = Modifier.height(72.dp))
        }
      }
    }
  }

  // Portion Sizing & Meal Logging Sheet / Dialog
  selectedFoodForLog?.let { food ->
    LogPortionDialog(
      food = food,
      initialMealType = currentMealTypeChoice,
      onDismiss = { selectedFoodForLog = null },
      onConfirm = { servingMultiplier, chosenMeal ->
        val cal = (food.caloriesPerServing * servingMultiplier).toInt()
        val p = food.protein * servingMultiplier
        val c = food.carbs * servingMultiplier
        val f = food.fat * servingMultiplier
        val fib = food.fiber * servingMultiplier
        val amt = food.baseServingAmount * servingMultiplier

        viewModel.logMeal(
          foodName = food.name,
          mealType = chosenMeal,
          servingAmount = amt,
          servingUnit = food.baseServingUnit,
          calories = cal,
          protein = p,
          carbs = c,
          fat = f,
          fiber = fib
        )
        selectedFoodForLog = null
      }
    )
  }

  // Custom Food Creation Dialog
  if (showCustomFoodDialog) {
    AddCustomFoodDialog(
      onDismiss = { showCustomFoodDialog = false },
      onSave = { name, category, amount, unit, cals, p, c, f, fib ->
        viewModel.addCustomFood(name, category, amount, unit, cals, p, c, f, fib)
        showCustomFoodDialog = false
      }
    )
  }
}

@Composable
fun FoodItemCard(
  food: FoodItem,
  onQuickLog: () -> Unit
) {
  Surface(
    shape = RoundedCornerShape(18.dp),
    color = MaterialTheme.colorScheme.surface,
    tonalElevation = 1.dp,
    modifier = Modifier.fillMaxWidth()
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(14.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Column(modifier = Modifier.weight(1f)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Text(
            text = food.name,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
          )
          if (food.isCustom) {
            Spacer(modifier = Modifier.width(6.dp))
            Surface(
              shape = CircleShape,
              color = Emerald500.copy(alpha = 0.15f)
            ) {
              Text(
                text = "Custom",
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = Emerald600,
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(4.dp))
        Text(
          text = "Per ${food.baseServingAmount.toInt()} ${food.baseServingUnit} • ${food.category}",
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(6.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
          Text("P: ${food.protein}g", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = ProteinColor)
          Text("C: ${food.carbs}g", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = CarbsColor)
          Text("F: ${food.fat}g", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = FatColor)
          Text("Fib: ${food.fiber}g", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = FiberColor)
        }
      }

      Column(
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.Center
      ) {
        Text(
          text = "${food.caloriesPerServing} kcal",
          style = MaterialTheme.typography.titleMedium,
          fontWeight = FontWeight.ExtraBold,
          color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(6.dp))
        Button(
          onClick = onQuickLog,
          shape = RoundedCornerShape(10.dp),
          colors = ButtonDefaults.buttonColors(containerColor = Emerald600),
          modifier = Modifier.testTag("log_food_${food.name.replace(" ", "_")}")
        ) {
          Text("+ Log", fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }
      }
    }
  }
}

@Composable
fun LogPortionDialog(
  food: FoodItem,
  initialMealType: String,
  onDismiss: () -> Unit,
  onConfirm: (multiplier: Double, mealType: String) -> Unit
) {
  var multiplier by remember { mutableDoubleStateOf(1.0) }
  var chosenMeal by remember { mutableStateOf(initialMealType) }

  val computedCals = (food.caloriesPerServing * multiplier).toInt()
  val computedProtein = food.protein * multiplier
  val computedCarbs = food.carbs * multiplier
  val computedFat = food.fat * multiplier
  val computedFiber = food.fiber * multiplier

  AlertDialog(
    onDismissRequest = onDismiss,
    title = {
      Text(text = "Log ${food.name}", fontWeight = FontWeight.Bold)
    },
    text = {
      Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
          text = "Select Meal Type:",
          style = MaterialTheme.typography.labelMedium,
          fontWeight = FontWeight.Bold
        )

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          val meals = listOf("BREAKFAST", "LUNCH", "DINNER", "SNACK")
          meals.forEach { meal ->
            val isSel = chosenMeal.equals(meal, ignoreCase = true)
            Surface(
              shape = RoundedCornerShape(8.dp),
              color = if (isSel) Emerald600 else MaterialTheme.colorScheme.surfaceVariant,
              modifier = Modifier
                .weight(1f)
                .clickable { chosenMeal = meal }
            ) {
              Text(
                text = meal.take(5),
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = if (isSel) androidx.compose.ui.graphics.Color.White else MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(vertical = 8.dp),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(4.dp))
        Text(
          text = "Adjust Portion Multiplier: ${"%.1f".format(multiplier)}x",
          style = MaterialTheme.typography.labelMedium,
          fontWeight = FontWeight.Bold
        )

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          listOf(0.5, 1.0, 1.5, 2.0).forEach { factor ->
            OutlinedButton(
              onClick = { multiplier = factor },
              shape = RoundedCornerShape(8.dp),
              modifier = Modifier.weight(1f),
              colors = ButtonDefaults.outlinedButtonColors(
                containerColor = if (multiplier == factor) Emerald500.copy(alpha = 0.2f) else androidx.compose.ui.graphics.Color.Transparent
              )
            ) {
              Text("${factor}x", fontSize = 12.sp)
            }
          }
        }

        // Computed Nutrition Preview
        Surface(
          shape = RoundedCornerShape(12.dp),
          color = MaterialTheme.colorScheme.surfaceVariant,
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(12.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Text("Total Energy:", fontWeight = FontWeight.SemiBold)
              Text("$computedCals kcal", fontWeight = FontWeight.ExtraBold, color = Emerald600)
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
              text = "P: ${computedProtein.toInt()}g • C: ${computedCarbs.toInt()}g • F: ${computedFat.toInt()}g • Fib: ${computedFiber.toInt()}g",
              fontSize = 12.sp,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
        }
      }
    },
    confirmButton = {
      Button(
        onClick = { onConfirm(multiplier, chosenMeal) },
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Emerald600)
      ) {
        Text("Save & Log")
      }
    },
    dismissButton = {
      TextButton(onClick = onDismiss) {
        Text("Cancel")
      }
    }
  )
}

@Composable
fun AddCustomFoodDialog(
  onDismiss: () -> Unit,
  onSave: (name: String, category: String, amount: Double, unit: String, cals: Int, p: Double, c: Double, f: Double, fib: Double) -> Unit
) {
  var name by remember { mutableStateOf("") }
  var category by remember { mutableStateOf("Proteins") }
  var servingAmount by remember { mutableStateOf("100") }
  var servingUnit by remember { mutableStateOf("g") }
  var calories by remember { mutableStateOf("150") }
  var protein by remember { mutableStateOf("15") }
  var carbs by remember { mutableStateOf("10") }
  var fat by remember { mutableStateOf("3") }
  var fiber by remember { mutableStateOf("2") }

  AlertDialog(
    onDismissRequest = onDismiss,
    title = { Text("Create Custom Ingredient", fontWeight = FontWeight.Bold) },
    text = {
      Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        OutlinedTextField(
          value = name,
          onValueChange = { name = it },
          label = { Text("Food Name") },
          modifier = Modifier.fillMaxWidth()
        )

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
          OutlinedTextField(
            value = servingAmount,
            onValueChange = { servingAmount = it },
            label = { Text("Serving") },
            modifier = Modifier.weight(1f)
          )
          OutlinedTextField(
            value = servingUnit,
            onValueChange = { servingUnit = it },
            label = { Text("Unit (g, ml)") },
            modifier = Modifier.weight(1f)
          )
        }

        OutlinedTextField(
          value = calories,
          onValueChange = { calories = it },
          label = { Text("Calories (kcal)") },
          modifier = Modifier.fillMaxWidth()
        )

        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
          OutlinedTextField(
            value = protein,
            onValueChange = { protein = it },
            label = { Text("Prot(g)") },
            modifier = Modifier.weight(1f)
          )
          OutlinedTextField(
            value = carbs,
            onValueChange = { carbs = it },
            label = { Text("Carb(g)") },
            modifier = Modifier.weight(1f)
          )
          OutlinedTextField(
            value = fat,
            onValueChange = { fat = it },
            label = { Text("Fat(g)") },
            modifier = Modifier.weight(1f)
          )
          OutlinedTextField(
            value = fiber,
            onValueChange = { fiber = it },
            label = { Text("Fib(g)") },
            modifier = Modifier.weight(1f)
          )
        }
      }
    },
    confirmButton = {
      Button(
        onClick = {
          if (name.isNotBlank()) {
            onSave(
              name.trim(),
              category,
              servingAmount.toDoubleOrNull() ?: 100.0,
              servingUnit.trim().ifBlank { "g" },
              calories.toIntOrNull() ?: 0,
              protein.toDoubleOrNull() ?: 0.0,
              carbs.toDoubleOrNull() ?: 0.0,
              fat.toDoubleOrNull() ?: 0.0,
              fiber.toDoubleOrNull() ?: 0.0
            )
          }
        },
        enabled = name.isNotBlank(),
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Emerald600)
      ) {
        Text("Save to Database")
      }
    },
    dismissButton = {
      TextButton(onClick = onDismiss) { Text("Cancel") }
    }
  )
}
