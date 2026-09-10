package com.example.ui.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material.icons.filled.CloudSync
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.DirectionsWalk
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Scale
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Watch
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import com.example.data.model.Milestone
import com.example.data.model.UserProfile
import com.example.localization.StringsLocalization
import com.example.ui.theme.CarbsColor
import com.example.ui.theme.Emerald500
import com.example.ui.theme.Emerald600
import com.example.ui.theme.ProteinColor
import com.example.ui.viewmodel.NutriTrackViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ProfileScreen(
  viewModel: NutriTrackViewModel,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  val profile by viewModel.userProfile.collectAsState()
  val isBackingUp by viewModel.isBackingUpCloud.collectAsState()
  val backupMsg by viewModel.cloudBackupMessage.collectAsState()

  var showEditGoalsDialog by remember { mutableStateOf(false) }
  var showLanguageDialog by remember { mutableStateOf(false) }

  val photoPickerLauncher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.PickVisualMedia()
  ) { uri: Uri? ->
    if (uri != null) {
      viewModel.updateProfile(profile.copy(avatarUri = uri.toString()))
      Toast.makeText(context, "Profile picture updated!", Toast.LENGTH_SHORT).show()
    }
  }

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .padding(horizontal = 16.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    item {
      Spacer(modifier = Modifier.height(12.dp))
      Text(
        text = "User Profile & Preferences",
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurface
      )
      Text(
        text = "Personalized targets, unit systems, wearable sync & cloud backup",
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )
    }

    // User Avatar & Name Card
    item {
      Surface(
        shape = RoundedCornerShape(24.dp),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 2.dp,
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(
          modifier = Modifier.padding(20.dp),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          Box(
            modifier = Modifier
              .size(90.dp)
              .clip(CircleShape)
              .background(Emerald500.copy(alpha = 0.15f))
              .border(3.dp, Emerald500, CircleShape)
              .clickable {
                photoPickerLauncher.launch(
                  PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                )
              }
              .testTag("profile_avatar_picker"),
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
                contentDescription = "User photo",
                tint = Emerald600,
                modifier = Modifier.size(52.dp)
              )
            }
          }

          Spacer(modifier = Modifier.height(10.dp))

          Text(
            text = profile.name,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
          )
          Text(
            text = "Tap picture above to update custom photo",
            fontSize = 12.sp,
            color = Emerald600
          )

          Spacer(modifier = Modifier.height(16.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
          ) {
            ProfileMetricPill("Goal", profile.dietaryGoal)
            ProfileMetricPill("Preference", profile.dietPreference)
            ProfileMetricPill("Daily Target", "${profile.dailyCalorieTarget} kcal")
          }
        }
      }
    }

    // Daily Goals & Macro Targets Customizer
    item {
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
                text = "Daily Nutrition & Hydration Goals",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
              )
              Text(
                text = "P: ${profile.proteinTargetGrams}g • C: ${profile.carbsTargetGrams}g • F: ${profile.fatTargetGrams}g • Water: ${profile.waterTargetGlasses} gl (${profile.waterTargetGlasses * 250}ml)",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }

            IconButton(onClick = { showEditGoalsDialog = true }) {
              Icon(Icons.Default.Edit, contentDescription = "Edit Goals", tint = Emerald600)
            }
          }
        }
      }
    }

    // Unit System Toggle (Metric vs Imperial)
    item {
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
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(Icons.Default.Scale, contentDescription = null, tint = Emerald600)
              Spacer(modifier = Modifier.width(10.dp))
              Column {
                Text(
                  text = "Measurement Units",
                  fontWeight = FontWeight.Bold,
                  color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                  text = if (profile.unitSystem == "METRIC") "Metric (kg, cm, ml)" else "Imperial (lbs, in, oz)",
                  style = MaterialTheme.typography.bodySmall,
                  color = MaterialTheme.colorScheme.onSurfaceVariant
                )
              }
            }

            Row {
              OutlinedButton(
                onClick = { viewModel.updateProfile(profile.copy(unitSystem = "METRIC")) },
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                  containerColor = if (profile.unitSystem == "METRIC") Emerald500.copy(alpha = 0.2f) else Color.Transparent
                )
              ) {
                Text("Metric")
              }
              Spacer(modifier = Modifier.width(6.dp))
              OutlinedButton(
                onClick = { viewModel.updateProfile(profile.copy(unitSystem = "IMPERIAL")) },
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                  containerColor = if (profile.unitSystem == "IMPERIAL") Emerald500.copy(alpha = 0.2f) else Color.Transparent
                )
              ) {
                Text("Imperial")
              }
            }
          }
        }
      }
    }

    // Multi-Language & Dark Mode
    item {
      Surface(
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 2.dp,
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          // Language Row
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .clickable { showLanguageDialog = true }
              .padding(vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(Icons.Default.Language, contentDescription = null, tint = Emerald600)
              Spacer(modifier = Modifier.width(10.dp))
              Column {
                Text("Language / Idioma", fontWeight = FontWeight.Bold)
                val langName = StringsLocalization.availableLanguages.firstOrNull { it.code == profile.language }?.name ?: "English"
                Text(langName, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
              }
            }
            Text("Change", fontSize = 12.sp, color = Emerald600, fontWeight = FontWeight.Bold)
          }

          Divider(modifier = Modifier.padding(vertical = 12.dp), color = MaterialTheme.colorScheme.surfaceVariant)

          // Dark Theme Selector
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(Icons.Default.DarkMode, contentDescription = null, tint = Emerald600)
              Spacer(modifier = Modifier.width(10.dp))
              Column {
                Text("Theme Display Mode", fontWeight = FontWeight.Bold)
                Text(profile.themeMode, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
              }
            }

            Row {
              listOf("SYSTEM", "LIGHT", "DARK").forEach { mode ->
                Surface(
                  shape = RoundedCornerShape(8.dp),
                  color = if (profile.themeMode == mode) Emerald600 else MaterialTheme.colorScheme.surfaceVariant,
                  modifier = Modifier
                    .padding(horizontal = 2.dp)
                    .clickable { viewModel.updateProfile(profile.copy(themeMode = mode)) }
                ) {
                  Text(
                    text = mode.take(3),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (profile.themeMode == mode) Color.White else MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                  )
                }
              }
            }
          }
        }
      }
    }

    // Wearable Device Settings
    item {
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
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(Icons.Default.Watch, contentDescription = null, tint = Emerald600)
              Spacer(modifier = Modifier.width(10.dp))
              Column {
                Text("Smartwatch Connection", fontWeight = FontWeight.Bold)
                Text(
                  text = "${profile.smartwatchType} (${if (profile.smartwatchConnected) "Connected" else "Disconnected"})",
                  style = MaterialTheme.typography.bodySmall,
                  color = MaterialTheme.colorScheme.onSurfaceVariant
                )
              }
            }

            Switch(
              checked = profile.smartwatchConnected,
              onCheckedChange = { isChecked ->
                viewModel.updateProfile(profile.copy(smartwatchConnected = isChecked))
              },
              colors = SwitchDefaults.colors(checkedThumbColor = Emerald600)
            )
          }
        }
      }
    }

    // Cloud Backup & Offline Synchronization
    item {
      val lastBackupDate = SimpleDateFormat("MMM d, yyyy HH:mm", Locale.getDefault()).format(Date(profile.lastCloudBackupTimestamp))

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
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(Icons.Default.CloudSync, contentDescription = null, tint = Emerald600)
              Spacer(modifier = Modifier.width(10.dp))
              Column {
                Text("Cloud Backup & Sync", fontWeight = FontWeight.Bold)
                Text("Last backup: $lastBackupDate", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
              }
            }

            Button(
              onClick = { viewModel.performCloudBackup() },
              enabled = !isBackingUp,
              shape = RoundedCornerShape(10.dp),
              colors = ButtonDefaults.buttonColors(containerColor = Emerald600),
              modifier = Modifier.testTag("backup_now_button")
            ) {
              if (isBackingUp) {
                CircularProgressIndicator(modifier = Modifier.size(16.dp), strokeWidth = 2.dp, color = Color.White)
              } else {
                Text("Backup", fontSize = 12.sp, fontWeight = FontWeight.Bold)
              }
            }
          }

          backupMsg?.let { msg ->
            Spacer(modifier = Modifier.height(8.dp))
            Surface(
              shape = RoundedCornerShape(8.dp),
              color = Emerald500.copy(alpha = 0.15f),
              modifier = Modifier.fillMaxWidth()
            ) {
              Text(
                text = msg,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = Emerald600,
                modifier = Modifier.padding(8.dp)
              )
            }
          }
        }
      }
    }

    // Milestones & Social Media Sharing
    item {
      Text(
        text = "Milestones & Achievements",
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurface
      )
    }

    items(InitialData.milestones) { milestone ->
      MilestoneCard(
        milestone = milestone,
        onShare = {
          val shareText = "🏆 I just achieved '${milestone.title}' on NutriTrack! Logged my personalized macros and healthy meal habits. #NutriTrack #HealthyLiving"
          val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, shareText)
            type = "text/plain"
          }
          val shareIntent = Intent.createChooser(sendIntent, "Share Milestone to Friends")
          context.startActivity(shareIntent)
        }
      )
    }

    item {
      Spacer(modifier = Modifier.height(72.dp))
    }
  }

  // Edit Goals Dialog
  if (showEditGoalsDialog) {
    EditGoalsDialog(
      profile = profile,
      onDismiss = { showEditGoalsDialog = false },
      onSave = { cals, p, c, f, fib, water ->
        viewModel.updateProfile(
          profile.copy(
            dailyCalorieTarget = cals,
            proteinTargetGrams = p,
            carbsTargetGrams = c,
            fatTargetGrams = f,
            fiberTargetGrams = fib,
            waterTargetGlasses = water
          )
        )
        showEditGoalsDialog = false
      }
    )
  }

  // Language Selection Dialog
  if (showLanguageDialog) {
    AlertDialog(
      onDismissRequest = { showLanguageDialog = false },
      title = { Text("Select Language", fontWeight = FontWeight.Bold) },
      text = {
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
          StringsLocalization.availableLanguages.forEach { langItem ->
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .clickable {
                  viewModel.updateProfile(profile.copy(language = langItem.code))
                  showLanguageDialog = false
                }
                .padding(vertical = 10.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              RadioButton(
                selected = profile.language == langItem.code,
                onClick = {
                  viewModel.updateProfile(profile.copy(language = langItem.code))
                  showLanguageDialog = false
                },
                colors = RadioButtonDefaults.colors(selectedColor = Emerald600)
              )
              Spacer(modifier = Modifier.width(10.dp))
              Text(langItem.name, fontWeight = FontWeight.SemiBold)
            }
          }
        }
      },
      confirmButton = {
        TextButton(onClick = { showLanguageDialog = false }) { Text("Close") }
      }
    )
  }
}

@Composable
fun ProfileMetricPill(label: String, value: String) {
  Column(horizontalAlignment = Alignment.CenterHorizontally) {
    Text(text = label, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
    Text(text = value, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
  }
}

@Composable
fun MilestoneCard(
  milestone: Milestone,
  onShare: () -> Unit
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
      Row(
        modifier = Modifier.weight(1f),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Box(
          modifier = Modifier
            .size(40.dp)
            .background(if (milestone.isUnlocked) Emerald500.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surfaceVariant, CircleShape),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.Default.EmojiEvents,
            contentDescription = null,
            tint = if (milestone.isUnlocked) Emerald600 else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
            modifier = Modifier.size(22.dp)
          )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column {
          Text(
            text = milestone.title,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
          )
          Text(
            text = milestone.subtitle,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
          Text(
            text = "Achieved: ${milestone.dateAchieved}",
            fontSize = 11.sp,
            color = Emerald600,
            fontWeight = FontWeight.Medium
          )
        }
      }

      if (milestone.isUnlocked) {
        IconButton(
          onClick = onShare,
          modifier = Modifier.testTag("share_milestone_${milestone.id}")
        ) {
          Icon(
            imageVector = Icons.Default.Share,
            contentDescription = "Share milestone with friends",
            tint = Emerald600
          )
        }
      }
    }
  }
}

@Composable
fun EditGoalsDialog(
  profile: UserProfile,
  onDismiss: () -> Unit,
  onSave: (cals: Int, p: Int, c: Int, f: Int, fib: Int, water: Int) -> Unit
) {
  var cals by remember { mutableStateOf(profile.dailyCalorieTarget.toString()) }
  var protein by remember { mutableStateOf(profile.proteinTargetGrams.toString()) }
  var carbs by remember { mutableStateOf(profile.carbsTargetGrams.toString()) }
  var fat by remember { mutableStateOf(profile.fatTargetGrams.toString()) }
  var fiber by remember { mutableStateOf(profile.fiberTargetGrams.toString()) }
  var water by remember { mutableStateOf(profile.waterTargetGlasses.toString()) }

  AlertDialog(
    onDismissRequest = onDismiss,
    title = { Text("Customize Daily Goals", fontWeight = FontWeight.Bold) },
    text = {
      Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        OutlinedTextField(
          value = cals,
          onValueChange = { cals = it },
          label = { Text("Daily Calorie Target (kcal)") },
          modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
          value = water,
          onValueChange = { water = it },
          label = { Text("Daily Water Goal (glasses @ 250ml)") },
          modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
          value = protein,
          onValueChange = { protein = it },
          label = { Text("Protein Target (g)") },
          modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
          value = carbs,
          onValueChange = { carbs = it },
          label = { Text("Carbs Target (g)") },
          modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
          value = fat,
          onValueChange = { fat = it },
          label = { Text("Fat Target (g)") },
          modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
          value = fiber,
          onValueChange = { fiber = it },
          label = { Text("Fiber Target (g)") },
          modifier = Modifier.fillMaxWidth()
        )
      }
    },
    confirmButton = {
      Button(
        onClick = {
          onSave(
            cals.toIntOrNull() ?: 2000,
            protein.toIntOrNull() ?: 140,
            carbs.toIntOrNull() ?: 200,
            fat.toIntOrNull() ?: 65,
            fiber.toIntOrNull() ?: 30,
            water.toIntOrNull() ?: 8
          )
        },
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Emerald600)
      ) {
        Text("Save Goals")
      }
    },
    dismissButton = {
      TextButton(onClick = onDismiss) { Text("Cancel") }
    }
  )
}
