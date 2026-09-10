package com.example.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CarbsColor
import com.example.ui.theme.FatColor
import com.example.ui.theme.FiberColor
import com.example.ui.theme.ProteinColor

@Composable
fun MacroNutrientBar(
  proteinConsumed: Double,
  proteinTarget: Int,
  carbsConsumed: Double,
  carbsTarget: Int,
  fatConsumed: Double,
  fatTarget: Int,
  fiberConsumed: Double,
  fiberTarget: Int,
  modifier: Modifier = Modifier
) {
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
        Text(
          text = "Macro Nutrition Targets",
          style = MaterialTheme.typography.titleMedium,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.onSurface
        )

        // Calculate macro calories & percentages
        val pCals = proteinConsumed * 4
        val cCals = carbsConsumed * 4
        val fCals = fatConsumed * 9
        val totalMacroCals = (pCals + cCals + fCals).coerceAtLeast(1.0)
        val pPct = (pCals / totalMacroCals * 100).toInt()
        val cPct = (cCals / totalMacroCals * 100).toInt()
        val fPct = (fCals / totalMacroCals * 100).toInt()

        Text(
          text = "$pPct% P • $cPct% C • $fPct% F",
          style = MaterialTheme.typography.labelSmall,
          fontWeight = FontWeight.SemiBold,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }

      Spacer(modifier = Modifier.height(14.dp))

      // Multi-color distribution strip
      val pWeight = (proteinConsumed * 4).toFloat().coerceAtLeast(0.1f)
      val cWeight = (carbsConsumed * 4).toFloat().coerceAtLeast(0.1f)
      val fWeight = (fatConsumed * 9).toFloat().coerceAtLeast(0.1f)

      Row(
        modifier = Modifier
          .fillMaxWidth()
          .height(10.dp)
          .clip(RoundedCornerShape(6.dp))
          .background(MaterialTheme.colorScheme.surfaceVariant)
      ) {
        Box(modifier = Modifier.weight(pWeight).fillMaxHeight().background(ProteinColor))
        Spacer(modifier = Modifier.width(2.dp))
        Box(modifier = Modifier.weight(cWeight).fillMaxHeight().background(CarbsColor))
        Spacer(modifier = Modifier.width(2.dp))
        Box(modifier = Modifier.weight(fWeight).fillMaxHeight().background(FatColor))
      }

      Spacer(modifier = Modifier.height(16.dp))

      // 4 Individual Macro Trackers
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        MacroPillCard(
          title = "Protein",
          consumed = proteinConsumed,
          target = proteinTarget,
          accentColor = ProteinColor,
          modifier = Modifier.weight(1f)
        )
        MacroPillCard(
          title = "Carbs",
          consumed = carbsConsumed,
          target = carbsTarget,
          accentColor = CarbsColor,
          modifier = Modifier.weight(1f)
        )
        MacroPillCard(
          title = "Fat",
          consumed = fatConsumed,
          target = fatTarget,
          accentColor = FatColor,
          modifier = Modifier.weight(1f)
        )
        MacroPillCard(
          title = "Fiber",
          consumed = fiberConsumed,
          target = fiberTarget,
          accentColor = FiberColor,
          modifier = Modifier.weight(1f)
        )
      }
    }
  }
}

@Composable
private fun MacroPillCard(
  title: String,
  consumed: Double,
  target: Int,
  accentColor: Color,
  modifier: Modifier = Modifier
) {
  val progress = if (target > 0) (consumed.toFloat() / target.toFloat()).coerceIn(0f, 1f) else 0f
  val animatedProgress by animateFloatAsState(
    targetValue = progress,
    animationSpec = tween(600),
    label = "macro_anim"
  )

  Surface(
    shape = RoundedCornerShape(16.dp),
    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
    modifier = modifier
  ) {
    Column(
      modifier = Modifier.padding(10.dp),
      horizontalAlignment = Alignment.Start
    ) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
          modifier = Modifier
            .size(8.dp)
            .background(accentColor, CircleShape)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
          text = title,
          style = MaterialTheme.typography.labelSmall,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.onSurface
        )
      }

      Spacer(modifier = Modifier.height(6.dp))

      Text(
        text = "${consumed.toInt()}g",
        style = MaterialTheme.typography.titleSmall,
        fontWeight = FontWeight.ExtraBold,
        color = MaterialTheme.colorScheme.onSurface
      )
      Text(
        text = "/ $target g",
        fontSize = 11.sp,
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )

      Spacer(modifier = Modifier.height(8.dp))

      // Mini Progress bar
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .height(5.dp)
          .clip(RoundedCornerShape(3.dp))
          .background(MaterialTheme.colorScheme.surfaceVariant)
      ) {
        Box(
          modifier = Modifier
            .fillMaxWidth(animatedProgress)
            .fillMaxHeight()
            .background(accentColor)
        )
      }
    }
  }
}
