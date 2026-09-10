package com.example.data.local

import com.example.data.model.FoodAlternative
import com.example.data.model.FoodItem
import com.example.data.model.MealLogItem
import com.example.data.model.MealPlan
import com.example.data.model.MealPlanItem
import com.example.data.model.Milestone
import com.example.data.model.UserProfile
import com.example.data.model.WaterLog
import com.example.data.model.WeightLog
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

object InitialData {

  fun getTodayDateString(): String {
    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return sdf.format(Date())
  }

  fun getDateDaysAgo(daysAgo: Int): String {
    val cal = Calendar.getInstance()
    cal.add(Calendar.DAY_OF_YEAR, -daysAgo)
    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return sdf.format(cal.time)
  }

  val defaultUserProfile = UserProfile(
    id = 1,
    name = "Alex Rivera",
    avatarUri = null,
    targetWeightKg = 68.0,
    currentWeightKg = 72.4,
    heightCm = 176.0,
    dailyCalorieTarget = 2100,
    proteinTargetGrams = 140,
    carbsTargetGrams = 210,
    fatTargetGrams = 65,
    fiberTargetGrams = 32,
    waterTargetGlasses = 8,
    dietaryGoal = "Weight Loss",
    dietPreference = "Balanced",
    unitSystem = "METRIC",
    language = "en",
    themeMode = "SYSTEM",
    smartwatchConnected = true,
    smartwatchType = "Pixel Watch 3",
    lastSyncTimestamp = System.currentTimeMillis() - 1000 * 60 * 14,
    syncedSteps = 8640,
    syncedActiveCalories = 480,
    lastCloudBackupTimestamp = System.currentTimeMillis() - 1000 * 60 * 60 * 2
  )

  val preseededFoods = listOf(
    // Proteins
    FoodItem(0, "Grilled Chicken Breast", "Proteins", 100.0, "g", 165, 31.0, 0.0, 3.6, 0.0),
    FoodItem(0, "Wild Atlantic Salmon", "Proteins", 100.0, "g", 208, 20.4, 0.0, 13.4, 0.0),
    FoodItem(0, "Whole Large Egg", "Proteins", 1.0, "piece", 72, 6.3, 0.4, 4.8, 0.0),
    FoodItem(0, "Egg Whites", "Proteins", 100.0, "g", 52, 11.0, 0.7, 0.2, 0.0),
    FoodItem(0, "Non-Fat Greek Yogurt", "Dairy", 150.0, "g", 100, 15.0, 6.0, 0.0, 0.0),
    FoodItem(0, "Whey Protein Isolate", "Proteins", 30.0, "g", 115, 25.0, 1.5, 0.5, 0.0),
    FoodItem(0, "Firm Organic Tofu", "Proteins", 100.0, "g", 83, 10.0, 2.0, 5.0, 1.0),
    FoodItem(0, "Lean Ground Turkey (93%)", "Proteins", 100.0, "g", 150, 19.0, 0.0, 8.0, 0.0),
    FoodItem(0, "Canned Tuna in Water", "Proteins", 100.0, "g", 116, 26.0, 0.0, 1.0, 0.0),
    FoodItem(0, "Cottage Cheese 2%", "Dairy", 100.0, "g", 84, 11.0, 4.0, 2.3, 0.0),

    // Grains & Carbs
    FoodItem(0, "Rolled Oats", "Grains", 50.0, "g", 190, 6.5, 34.0, 3.5, 5.0),
    FoodItem(0, "Cooked Brown Rice", "Grains", 100.0, "g", 111, 2.6, 23.0, 0.9, 1.8),
    FoodItem(0, "Cooked Quinoa", "Grains", 100.0, "g", 120, 4.4, 21.3, 1.9, 2.8),
    FoodItem(0, "Baked Sweet Potato", "Vegetables", 150.0, "g", 135, 3.0, 31.0, 0.2, 4.5),
    FoodItem(0, "Whole Wheat Sourdough", "Grains", 1.0, "slice", 90, 4.0, 18.0, 1.0, 2.5),
    FoodItem(0, "Whole Grain Pasta", "Grains", 80.0, "g", 280, 12.0, 57.0, 1.5, 6.0),

    // Vegetables
    FoodItem(0, "Baby Spinach", "Vegetables", 85.0, "g", 20, 2.5, 3.0, 0.3, 2.0),
    FoodItem(0, "Steamed Broccoli", "Vegetables", 100.0, "g", 35, 2.8, 7.2, 0.4, 2.6),
    FoodItem(0, "Cauliflower Florets", "Vegetables", 100.0, "g", 25, 1.9, 5.0, 0.3, 2.0),
    FoodItem(0, "Cherry Tomatoes", "Vegetables", 100.0, "g", 18, 0.9, 3.9, 0.2, 1.2),
    FoodItem(0, "Sliced Cucumber", "Vegetables", 100.0, "g", 15, 0.7, 3.6, 0.1, 0.5),
    FoodItem(0, "Bell Pepper Mix", "Vegetables", 100.0, "g", 26, 1.0, 6.0, 0.3, 2.1),

    // Fruits
    FoodItem(0, "Crisp Honeycrisp Apple", "Fruits", 1.0, "piece", 95, 0.5, 25.0, 0.3, 4.4),
    FoodItem(0, "Ripe Banana", "Fruits", 1.0, "piece", 105, 1.3, 27.0, 0.4, 3.1),
    FoodItem(0, "Fresh Blueberries", "Fruits", 100.0, "g", 57, 0.7, 14.5, 0.3, 2.4),
    FoodItem(0, "Strawberries", "Fruits", 100.0, "g", 32, 0.7, 7.7, 0.3, 2.0),
    FoodItem(0, "Hass Avocado", "Healthy Fats", 50.0, "g", 80, 1.0, 4.0, 7.5, 3.4),

    // Healthy Fats & Dairy
    FoodItem(0, "Extra Virgin Olive Oil", "Healthy Fats", 15.0, "ml", 120, 0.0, 0.0, 13.5, 0.0),
    FoodItem(0, "Raw Almonds", "Healthy Fats", 30.0, "g", 170, 6.0, 6.0, 15.0, 3.5),
    FoodItem(0, "Chia Seeds", "Healthy Fats", 15.0, "g", 73, 2.5, 6.0, 4.5, 5.0),
    FoodItem(0, "Peanut Butter (Natural)", "Healthy Fats", 20.0, "g", 118, 5.0, 4.0, 10.0, 1.6),
    FoodItem(0, "Unsweetened Almond Milk", "Dairy", 240.0, "ml", 35, 1.0, 1.5, 2.5, 1.0),
    FoodItem(0, "Low-Fat Mozzarella", "Dairy", 30.0, "g", 75, 7.0, 1.0, 5.0, 0.0)
  )

  fun getInitialMealLogs(): List<MealLogItem> {
    val today = getTodayDateString()
    val yesterday = getDateDaysAgo(1)
    val twoDaysAgo = getDateDaysAgo(2)
    val threeDaysAgo = getDateDaysAgo(3)

    return listOf(
      // Today logs
      MealLogItem(0, today, "BREAKFAST", "Rolled Oats with Berries & Chia", 1.0, "bowl", 320, 12.0, 54.0, 8.0, 9.0),
      MealLogItem(0, today, "BREAKFAST", "Non-Fat Greek Yogurt", 150.0, "g", 100, 15.0, 6.0, 0.0, 0.0),
      MealLogItem(0, today, "LUNCH", "Grilled Chicken Salad with Avocado", 1.0, "plate", 440, 38.0, 18.0, 19.0, 8.0),
      MealLogItem(0, today, "SNACK", "Raw Almonds & Apple", 1.0, "serving", 265, 6.5, 31.0, 15.0, 7.9),
      MealLogItem(0, today, "DINNER", "Wild Salmon with Quinoa & Steamed Broccoli", 1.0, "plate", 510, 42.0, 41.0, 18.0, 8.5),

      // Yesterday logs
      MealLogItem(0, yesterday, "BREAKFAST", "Scrambled Eggs & Whole Wheat Toast", 1.0, "plate", 340, 18.0, 24.0, 14.0, 4.0),
      MealLogItem(0, yesterday, "LUNCH", "Turkey & Quinoa Grain Bowl", 1.0, "bowl", 480, 36.0, 48.0, 14.0, 7.0),
      MealLogItem(0, yesterday, "DINNER", "Baked Sweet Potato & Chicken Stir Fry", 1.0, "plate", 530, 40.0, 55.0, 12.0, 9.0),
      MealLogItem(0, yesterday, "SNACK", "Whey Protein Shake", 1.0, "shake", 150, 26.0, 3.0, 1.5, 1.0),

      // 2 days ago
      MealLogItem(0, twoDaysAgo, "BREAKFAST", "Greek Yogurt Parfait with Granola", 1.0, "cup", 310, 18.0, 42.0, 6.0, 4.0),
      MealLogItem(0, twoDaysAgo, "LUNCH", "Tuna Salad Whole Wheat Wrap", 1.0, "wrap", 420, 32.0, 38.0, 12.0, 5.0),
      MealLogItem(0, twoDaysAgo, "DINNER", "Lean Beef & Brown Rice Bowl", 1.0, "bowl", 560, 42.0, 52.0, 16.0, 6.0),

      // 3 days ago
      MealLogItem(0, threeDaysAgo, "BREAKFAST", "Avocado Toast with Poached Egg", 1.0, "serving", 330, 12.0, 28.0, 18.0, 6.0),
      MealLogItem(0, threeDaysAgo, "LUNCH", "Mediterranean Chickpea & Chicken Bowl", 1.0, "bowl", 490, 38.0, 45.0, 14.0, 8.0),
      MealLogItem(0, threeDaysAgo, "DINNER", "Grilled Cod with Asparagus", 1.0, "plate", 380, 35.0, 12.0, 8.0, 4.0)
    )
  }

  fun getInitialWeightLogs(): List<WeightLog> {
    return listOf(
      WeightLog(0, getDateDaysAgo(21), 74.2),
      WeightLog(0, getDateDaysAgo(14), 73.5),
      WeightLog(0, getDateDaysAgo(7), 72.9),
      WeightLog(0, getTodayDateString(), 72.4)
    )
  }

  fun getInitialWaterLog(): WaterLog {
    return WaterLog(0, getTodayDateString(), 6)
  }

  val foodAlternatives = listOf(
    FoodAlternative(
      id = "alt_1",
      originalFood = "Sour Cream (100g)",
      healthierAlternative = "Non-Fat Greek Yogurt (100g)",
      caloriesSavedPerServing = 135,
      benefitDescription = "Saves 135 kcal, drops 18g saturated fat, adds +12g gut-healthy protein.",
      originalCalories = 195,
      alternativeCalories = 60,
      alternativeProtein = 12.0,
      alternativeFiber = 0.0,
      category = "Dairy"
    ),
    FoodAlternative(
      id = "alt_2",
      originalFood = "White Steamed Rice (1 cup)",
      healthierAlternative = "Riced Cauliflower & Quinoa",
      caloriesSavedPerServing = 145,
      benefitDescription = "Triples micronutrients & fiber while cutting glycemic spike in half.",
      originalCalories = 205,
      alternativeCalories = 60,
      alternativeProtein = 4.0,
      alternativeFiber = 4.5,
      category = "Grains"
    ),
    FoodAlternative(
      id = "alt_3",
      originalFood = "Fried Potato Chips (50g)",
      healthierAlternative = "Air-Popped Salt & Vinegar Popcorn",
      caloriesSavedPerServing = 140,
      benefitDescription = "Saves 140 kcal, 100% whole grain with 4x higher fullness factor.",
      originalCalories = 270,
      alternativeCalories = 130,
      alternativeProtein = 3.5,
      alternativeFiber = 4.0,
      category = "Snacks"
    ),
    FoodAlternative(
      id = "alt_4",
      originalFood = "Full-Fat Mayonnaise (2 tbsp)",
      healthierAlternative = "Mashed Avocado + Dijon Mustard",
      caloriesSavedPerServing = 110,
      benefitDescription = "Replaces inflammatory seed oils with heart-healthy monounsaturated fats.",
      originalCalories = 190,
      alternativeCalories = 80,
      alternativeProtein = 1.0,
      alternativeFiber = 3.2,
      category = "Healthy Fats"
    ),
    FoodAlternative(
      id = "alt_5",
      originalFood = "Sugary Soda (1 can / 355ml)",
      healthierAlternative = "Sparkling Water with Fresh Lemon & Mint",
      caloriesSavedPerServing = 150,
      benefitDescription = "Eliminates 39g of high-fructose corn syrup, hydrates cells without insulin spikes.",
      originalCalories = 150,
      alternativeCalories = 2,
      alternativeProtein = 0.0,
      alternativeFiber = 0.2,
      category = "Beverages"
    ),
    FoodAlternative(
      id = "alt_6",
      originalFood = "Milk Chocolate Bar (50g)",
      healthierAlternative = "85% Extra Dark Chocolate (2 squares)",
      caloriesSavedPerServing = 120,
      benefitDescription = "High in polyphenol antioxidants and magnesium, cuts refined sugars by 75%.",
      originalCalories = 270,
      alternativeCalories = 150,
      alternativeProtein = 2.5,
      alternativeFiber = 3.0,
      category = "Snacks"
    )
  )

  val personalizedMealPlans = listOf(
    MealPlan(
      id = "plan_balanced",
      title = "Vitality Balanced Diet",
      dietType = "Balanced",
      targetCalories = 2050,
      targetProteinGrams = 135,
      targetCarbsGrams = 210,
      targetFatGrams = 65,
      description = "Nutrient-dense whole foods designed for sustained energy, gut health, and steady fat loss.",
      meals = listOf(
        MealPlanItem("Breakfast", "Superberry Rolled Oats Bowl", "Rolled oats, blueberries, chia seeds, scoop of whey protein", 410, 26.0, 56.0, 8.5, 9.0),
        MealPlanItem("Lunch", "Mediterranean Grilled Chicken & Quinoa", "Chicken breast, quinoa, cucumber, cherry tomatoes, kalamata olives, olive oil", 550, 44.0, 48.0, 16.0, 7.0),
        MealPlanItem("Snack", "Greek Yogurt & Raw Almonds", "Non-fat Greek yogurt with raw almonds and raw honey drizzle", 240, 18.0, 16.0, 11.0, 3.5),
        MealPlanItem("Dinner", "Pan-Seared Salmon & Roast Sweet Potato", "Wild Atlantic salmon, baked sweet potato, steamed asparagus in garlic herbs", 580, 42.0, 46.0, 19.0, 8.0)
      )
    ),
    MealPlan(
      id = "plan_high_protein",
      title = "Lean Muscle Fuel",
      dietType = "High-Protein",
      targetCalories = 2350,
      targetProteinGrams = 180,
      targetCarbsGrams = 220,
      targetFatGrams = 60,
      description = "Optimized protein pacing with leucine-rich meals to maximize protein synthesis and recovery.",
      meals = listOf(
        MealPlanItem("Breakfast", "Triple Egg White Omelet & Avocado Toast", "3 egg whites + 1 whole egg, spinach, whole wheat toast, 1/4 avocado", 430, 32.0, 28.0, 16.0, 5.0),
        MealPlanItem("Lunch", "Turkey & Brown Rice Power Bowl", "93% lean ground turkey, brown rice, bell peppers, black beans", 620, 50.0, 68.0, 14.0, 9.0),
        MealPlanItem("Snack", "Isolate Protein Shake & Banana", "Whey isolate, unsweetened almond milk, ripe banana", 280, 30.0, 32.0, 2.0, 3.5),
        MealPlanItem("Dinner", "Herb-Crusted Cod & Roasted Broccoli", "Cod loin, garlic roasted broccoli, baby red potatoes", 560, 48.0, 52.0, 11.0, 8.5)
      )
    ),
    MealPlan(
      id = "plan_keto",
      title = "Ketogenic Metabolic Plan",
      dietType = "Keto",
      targetCalories = 1850,
      targetProteinGrams = 110,
      targetCarbsGrams = 28,
      targetFatGrams = 145,
      description = "Ultra-low carbohydrate formula designed to trigger nutritional ketosis and appetite suppression.",
      meals = listOf(
        MealPlanItem("Breakfast", "Avocado, Bacon & Spinach Scramble", "2 pasture-raised eggs, pasture bacon, sliced Hass avocado, baby spinach", 480, 22.0, 4.0, 41.0, 4.0),
        MealPlanItem("Lunch", "Grilled Salmon Caesar Salad", "Crispy romaine, parmesan shavings, wild salmon fillet, avocado oil dressing", 520, 38.0, 3.0, 38.0, 2.5),
        MealPlanItem("Snack", "Macadamia Nuts & String Cheese", "Dry-roasted macadamia nuts with whole-milk mozzarella stick", 290, 8.0, 3.0, 28.0, 2.0),
        MealPlanItem("Dinner", "Grass-Fed Ribeye with Buttered Asparagus", "Seared steak, grass-fed herb butter, grilled asparagus", 560, 42.0, 4.0, 42.0, 3.0)
      )
    ),
    MealPlan(
      id = "plan_mediterranean",
      title = "Longevity Mediterranean",
      dietType = "Mediterranean",
      targetCalories = 1950,
      targetProteinGrams = 115,
      targetCarbsGrams = 205,
      targetFatGrams = 68,
      description = "Rich in polyphenols, omega-3s, and colorful vegetables for cardiovascular and cognitive longevity.",
      meals = listOf(
        MealPlanItem("Breakfast", "Greek Fig Parfait & Walnuts", "Thick Greek yogurt, fresh sliced figs, crushed walnuts, cinnamon", 360, 18.0, 36.0, 14.0, 4.5),
        MealPlanItem("Lunch", "Lentil, Feta & Herb Grain Salad", "French green lentils, farro, feta crumble, diced cucumber, EVOO lemon dressing", 520, 24.0, 64.0, 16.0, 11.0),
        MealPlanItem("Snack", "Roasted Rosemary Chickpeas & Citrus", "Crispy seasoned chickpeas, sweet mandarin orange", 220, 9.0, 34.0, 5.0, 6.0),
        MealPlanItem("Dinner", "Mediterranean Sea Bass & Ratatouille", "Pan-roasted white fish, zucchini, eggplant, tomato concassé, olive oil", 530, 40.0, 32.0, 22.0, 7.5)
      )
    )
  )

  val milestones = listOf(
    Milestone("m1", "7-Day Goal Streak", "Consistently hit daily calorie target within 5%", "Sep 7, 2026", "EmojiEvents", true),
    Milestone("m2", "Macro Master", "Balanced Protein, Carbs, and Fats within optimal ratios", "Sep 8, 2026", "FitnessCenter", true),
    Milestone("m3", "10,000 Step Milestone", "Wearable synced over 10,000 active steps", "Sep 9, 2026", "DirectionsWalk", true),
    Milestone("m4", "Hydration Hero", "Logged 8+ glasses of water 5 days in a row", "Sep 6, 2026", "WaterDrop", true),
    Milestone("m5", "NutriDatabase Explorer", "Logged 25 unique whole food ingredients", "Sep 4, 2026", "Restaurant", true),
    Milestone("m6", "30-Day Transformation", "Log consistently for 30 days straight", "In Progress", "MilitaryTech", false)
  )
}
