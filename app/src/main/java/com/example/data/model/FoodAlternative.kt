package com.example.data.model

data class FoodAlternative(
  val id: String,
  val originalFood: String,
  val healthierAlternative: String,
  val caloriesSavedPerServing: Int,
  val benefitDescription: String,
  val originalCalories: Int,
  val alternativeCalories: Int,
  val alternativeProtein: Double,
  val alternativeFiber: Double,
  val category: String
)

data class MealPlan(
  val id: String,
  val title: String,
  val dietType: String, // "Balanced", "High-Protein", "Keto", "Mediterranean", "Vegan"
  val targetCalories: Int,
  val targetProteinGrams: Int,
  val targetCarbsGrams: Int,
  val targetFatGrams: Int,
  val description: String,
  val meals: List<MealPlanItem>
)

data class MealPlanItem(
  val mealType: String, // Breakfast, Lunch, Dinner, Snack
  val title: String,
  val ingredients: String,
  val calories: Int,
  val protein: Double,
  val carbs: Double,
  val fat: Double,
  val fiber: Double
)

data class Milestone(
  val id: String,
  val title: String,
  val subtitle: String,
  val dateAchieved: String,
  val iconName: String,
  val isUnlocked: Boolean
)
