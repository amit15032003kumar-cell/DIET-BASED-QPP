package com.example.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Watch
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CarbsColor
import com.example.ui.theme.Emerald500
import com.example.ui.theme.Emerald600

@Composable
fun CalorieProgressRing(
  consumedCalories: Int,
  burnedCalories: Int,
  dailyTarget: Int,
  unitSystem: String = "METRIC",
  modifier: Modifier = Modifier
) {
  val netCalories = (consumedCalories - burnedCalories).coerceAtLeast(0)
  val remainingCalories = (dailyTarget - netCalories).coerceAtLeast(0)
  val progress = if (dailyTarget > 0) {
    (consumedCalories.toFloat() / dailyTarget.toFloat()).coerceIn(0f, 1f)
  } else 0f

  val animatedProgress by animateFloatAsState(
    targetValue = progress,
    animationSpec = tween(durationMillis = 800),
    label = "calorie_ring_anim"
  )

  val energyUnit = if (unitSystem == "IMPERIAL") "kcal" else "kcal"

  Surface(
    shape = RoundedCornerShape(24.dp),
    color = MaterialTheme.colorScheme.surface,
    tonalElevation = 2.dp,
    modifier = modifier.fillMaxWidth()
  ) {
    Column(
      modifier = Modifier.padding(20.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(
            text = "Daily Calorie Budget",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
          )
          Text(
            text = "Target: $dailyTarget $energyUnit",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }

        // Live status pill
        Surface(
          shape = CircleShape,
          color = if (remainingCalories > 0) Emerald500.copy(alpha = 0.15f) else MaterialTheme.colorScheme.error.copy(alpha = 0.15f)
        ) {
          Text(
            text = if (remainingCalories > 0) "$remainingCalories $energyUnit left" else "Goal Exceeded",
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.SemiBold,
            color = if (remainingCalories > 0) Emerald600 else MaterialTheme.colorScheme.error,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
          )
        }
      }

      Spacer(modifier = Modifier.height(20.dp))

      // Central Ring Visual
      Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
          .size(190.dp)
          .semantics {
            contentDescription = "Calorie progress circle. $consumedCalories consumed of $dailyTarget $energyUnit target."
          }
      ) {
        val ringBgColor = MaterialTheme.colorScheme.surfaceVariant
        val strokeWidth = 14.dp

        Canvas(modifier = Modifier.size(180.dp)) {
          val stroke = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round)
          // Background track
          drawCircle(
            color = ringBgColor,
            style = stroke
          )
          // Foreground progress gradient arc
          drawArc(
            brush = Brush.sweepGradient(
              listOf(Emerald500, CarbsColor, Emerald600)
            ),
            startAngle = -90f,
            sweepAngle = animatedProgress * 360f,
            useCenter = false,
            style = stroke
          )
        }

        // Inner center stats
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
          Text(
            text = "$remainingCalories",
            fontSize = 38.sp,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.onSurface,
            letterSpacing = (-0.5).sp
          )
          Text(
            text = "REMAINING KCAL",
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            letterSpacing = 1.sp
          )
        }
      }

      Spacer(modifier = Modifier.height(20.dp))

      // 3-Column Energy Summary
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
      ) {
        EnergyMetricItem(
          icon = Icons.Default.LocalFireDepartment,
          iconColor = CarbsColor,
          label = "Base Target",
          value = "$dailyTarget"
        )
        EnergyMetricItem(
          icon = Icons.Default.Restaurant,
          iconColor = Emerald500,
          label = "Food Eaten",
          value = "$consumedCalories"
        )
        EnergyMetricItem(
          icon = Icons.Default.Watch,
          iconColor = Color(0xFF0EA5E9),
          label = "Wearable Burned",
          value = "-$burnedCalories"
        )
      }
    }
  }
}

@Composable
private fun EnergyMetricItem(
  icon: androidx.compose.ui.graphics.vector.ImageVector,
  iconColor: Color,
  label: String,
  value: String
) {
  Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center
  ) {
    Row(verticalAlignment = Alignment.CenterVertically) {
      Box(
        modifier = Modifier
          .size(26.dp)
          .background(iconColor.copy(alpha = 0.15f), CircleShape),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = icon,
          contentDescription = null,
          tint = iconColor,
          modifier = Modifier.size(16.dp)
        )
      }
      Spacer(modifier = Modifier.width(6.dp))
      Text(
        text = value,
        style = MaterialTheme.typography.titleSmall,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurface
      )
    }
    Spacer(modifier = Modifier.height(2.dp))
    Text(
      text = label,
      style = MaterialTheme.typography.labelSmall,
      color = MaterialTheme.colorScheme.onSurfaceVariant
    )
  }
}
