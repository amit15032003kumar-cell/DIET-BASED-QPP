package com.example.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Analytics
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.Dashboard
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.localization.StringsLocalization
import com.example.ui.screens.AnalyticsScreen
import com.example.ui.screens.DashboardScreen
import com.example.ui.screens.FoodDatabaseScreen
import com.example.ui.screens.MealPlansScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.theme.Emerald600
import com.example.ui.theme.NutriTrackTheme
import com.example.ui.viewmodel.NutriTrackViewModel

enum class MainDestination(
  val labelKey: String,
  val selectedIcon: ImageVector,
  val unselectedIcon: ImageVector
) {
  DASHBOARD("nav_dashboard", Icons.Filled.Dashboard, Icons.Outlined.Dashboard),
  FOOD_DB("nav_log", Icons.Filled.Search, Icons.Outlined.Search),
  ANALYTICS("nav_analytics", Icons.Filled.Analytics, Icons.Outlined.Analytics),
  MEAL_PLANS("nav_plans", Icons.Filled.AutoAwesome, Icons.Outlined.AutoAwesome),
  PROFILE("nav_profile", Icons.Filled.Person, Icons.Outlined.Person)
}

@Composable
fun NutriTrackApp(viewModel: NutriTrackViewModel) {
  val profile by viewModel.userProfile.collectAsState()
  var currentDestination by remember { mutableStateOf(MainDestination.DASHBOARD) }

  val isDark = when (profile.themeMode) {
    "DARK" -> true
    "LIGHT" -> false
    else -> isSystemInDarkTheme()
  }

  val configuration = LocalConfiguration.current
  val isExpandedTablet = configuration.screenWidthDp >= 600
  val lang = profile.language

  NutriTrackTheme(darkTheme = isDark) {
    if (isExpandedTablet) {
      // Tablet / Foldable layout with Navigation Rail
      Row(modifier = Modifier.fillMaxSize()) {
        NavigationRail(
          containerColor = MaterialTheme.colorScheme.surface,
          contentColor = MaterialTheme.colorScheme.onSurface
        ) {
          MainDestination.entries.forEach { destination ->
            val isSelected = currentDestination == destination
            NavigationRailItem(
              selected = isSelected,
              onClick = { currentDestination = destination },
              icon = {
                Icon(
                  imageVector = if (isSelected) destination.selectedIcon else destination.unselectedIcon,
                  contentDescription = StringsLocalization.get(destination.labelKey, lang)
                )
              },
              label = {
                Text(StringsLocalization.get(destination.labelKey, lang), fontSize = 11.sp)
              },
              colors = NavigationRailItemDefaults.colors(
                indicatorColor = Emerald600.copy(alpha = 0.2f),
                selectedIconColor = Emerald600,
                selectedTextColor = Emerald600
              ),
              modifier = Modifier.testTag("nav_rail_${destination.name.lowercase()}")
            )
          }
        }

        Box(modifier = Modifier.weight(1f)) {
          when (currentDestination) {
            MainDestination.DASHBOARD -> DashboardScreen(
              viewModel = viewModel,
              onNavigateToProfile = { currentDestination = MainDestination.PROFILE }
            )
            MainDestination.FOOD_DB -> FoodDatabaseScreen(viewModel = viewModel)
            MainDestination.ANALYTICS -> AnalyticsScreen(viewModel = viewModel)
            MainDestination.MEAL_PLANS -> MealPlansScreen(viewModel = viewModel)
            MainDestination.PROFILE -> ProfileScreen(viewModel = viewModel)
          }
        }
      }
    } else {
      // Mobile layout with M3 Bottom Navigation Bar
      Scaffold(
        bottomBar = {
          NavigationBar(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
          ) {
            MainDestination.entries.forEach { destination ->
              val isSelected = currentDestination == destination
              NavigationBarItem(
                selected = isSelected,
                onClick = { currentDestination = destination },
                icon = {
                  Icon(
                    imageVector = if (isSelected) destination.selectedIcon else destination.unselectedIcon,
                    contentDescription = StringsLocalization.get(destination.labelKey, lang),
                    modifier = Modifier.size(24.dp)
                  )
                },
                label = {
                  Text(
                    text = StringsLocalization.get(destination.labelKey, lang),
                    fontSize = 10.sp,
                    maxLines = 1
                  )
                },
                colors = NavigationBarItemDefaults.colors(
                  indicatorColor = Emerald600.copy(alpha = 0.2f),
                  selectedIconColor = Emerald600,
                  selectedTextColor = Emerald600
                ),
                modifier = Modifier.testTag("nav_bottom_${destination.name.lowercase()}")
              )
            }
          }
        }
      ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
          when (currentDestination) {
            MainDestination.DASHBOARD -> DashboardScreen(
              viewModel = viewModel,
              onNavigateToProfile = { currentDestination = MainDestination.PROFILE }
            )
            MainDestination.FOOD_DB -> FoodDatabaseScreen(viewModel = viewModel)
            MainDestination.ANALYTICS -> AnalyticsScreen(viewModel = viewModel)
            MainDestination.MEAL_PLANS -> MealPlansScreen(viewModel = viewModel)
            MainDestination.PROFILE -> ProfileScreen(viewModel = viewModel)
          }
        }
      }
    }
  }
}
