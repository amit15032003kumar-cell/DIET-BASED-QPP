package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.MealLogItem
import com.example.ui.theme.Emerald500
import com.example.ui.theme.Emerald600

data class DayTrend(
  val dayLabel: String,
  val date: String,
  val calories: Int,
  val target: Int
)

@Composable
fun HealthTrendChart(
  dayTrends: List<DayTrend>,
  dailyTarget: Int,
  modifier: Modifier = Modifier
) {
  val maxVal = ((dayTrends.maxOfOrNull { it.calories } ?: dailyTarget) * 1.15f).coerceAtLeast(2500f)

  Surface(
    shape = RoundedCornerShape(24.dp),
    color = MaterialTheme.colorScheme.surface,
    tonalElevation = 2.dp,
    modifier = modifier.fillMaxWidth()
  ) {
    Column(modifier = Modifier.padding(20.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(
            text = "7-Day Calorie Intake Trend",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
          )
          Text(
            text = "Intake vs Daily Budget ($dailyTarget kcal)",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
          Box(modifier = Modifier.size(8.dp).background(Emerald500, CircleShape))
          Spacer(modifier = Modifier.width(4.dp))
          Text("Target", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
      }

      Spacer(modifier = Modifier.height(20.dp))

      // Canvas Chart
      val surfaceVariant = MaterialTheme.colorScheme.surfaceVariant
      val outlineColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)
      val barColor = Emerald500
      val highBarColor = Color(0xFFF59E0B)

      Box(
        modifier = Modifier
          .fillMaxWidth()
          .height(180.dp)
      ) {
        Canvas(modifier = Modifier.matchParentSize()) {
          val canvasWidth = size.width
          val canvasHeight = size.height - 30.dp.toPx() // Leave space for bottom labels
          val barCount = dayTrends.size.coerceAtLeast(1)
          val totalBarSpacing = canvasWidth / barCount
          val barWidth = (totalBarSpacing * 0.45f).coerceAtMost(36.dp.toPx())

          // Draw target reference dashed line
          val targetY = canvasHeight - ((dailyTarget / maxVal) * canvasHeight)
          drawLine(
            color = outlineColor,
            start = Offset(0f, targetY),
            end = Offset(canvasWidth, targetY),
            strokeWidth = 2.dp.toPx(),
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
          )

          // Draw Bars
          dayTrends.forEachIndexed { index, trend ->
            val barHeight = (trend.calories / maxVal) * canvasHeight
            val left = (index * totalBarSpacing) + (totalBarSpacing - barWidth) / 2f
            val top = canvasHeight - barHeight

            // Bar background slot
            drawRoundRect(
              color = surfaceVariant.copy(alpha = 0.5f),
              topLeft = Offset(left, 0f),
              size = Size(barWidth, canvasHeight),
              cornerRadius = CornerRadius(8.dp.toPx(), 8.dp.toPx())
            )

            // Active bar
            val currentBarColor = if (trend.calories <= trend.target + 100) barColor else highBarColor
            drawRoundRect(
              color = currentBarColor,
              topLeft = Offset(left, top),
              size = Size(barWidth, barHeight),
              cornerRadius = CornerRadius(8.dp.toPx(), 8.dp.toPx())
            )
          }
        }

        // Bottom labels row
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .align(Alignment.BottomCenter),
          horizontalArrangement = Arrangement.SpaceAround
        ) {
          dayTrends.forEach { trend ->
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
              Text(
                text = "${trend.calories}",
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
              Text(
                text = trend.dayLabel,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
              )
            }
          }
        }
      }
    }
  }
}
