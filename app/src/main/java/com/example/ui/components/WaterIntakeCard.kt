package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.LocalDrink
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Opacity
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.Emerald500
import com.example.ui.theme.Emerald600
import com.example.ui.theme.WaterColor
import kotlin.math.sin

/**
 * High-performance, smooth interactive Water Intake Logging component
 * featuring:
 * - Fluid wave animation with hardware-accelerated drawScope
 * - Dynamic goal progress ring and fill level
 * - Quick-log portion buttons (+150ml, +250ml glass, +500ml bottle, +750ml tumbler)
 * - Visual celebratory state upon reaching 100% hydration
 * - Custom goal editor and reset options
 */
@Composable
fun WaterIntakeCard(
  currentGlasses: Int,
  targetGlasses: Int,
  unitSystem: String = "METRIC",
  onUpdateGlasses: (Int) -> Unit,
  onSetExactGlasses: (Int) -> Unit,
  onReminderClick: () -> Unit = {},
  modifier: Modifier = Modifier
) {
  val glassSizeMl = 250
  val currentMl = currentGlasses * glassSizeMl
  val targetMl = (if (targetGlasses > 0) targetGlasses else 8) * glassSizeMl

  val currentOz = (currentMl * 0.033814).toInt()
  val targetOz = (targetMl * 0.033814).toInt()

  val isImperial = unitSystem.equals("IMPERIAL", ignoreCase = true)
  val currentDisplay = if (isImperial) "$currentOz fl oz" else "$currentMl ml"
  val targetDisplay = if (isImperial) "$targetOz fl oz" else "$targetMl ml"

  val progressFraction = remember(currentGlasses, targetGlasses) {
    val t = if (targetGlasses > 0) targetGlasses.toFloat() else 8f
    (currentGlasses.toFloat() / t).coerceIn(0f, 1.25f)
  }

  val isGoalMet by remember(progressFraction) {
    derivedStateOf { progressFraction >= 1.0f }
  }

  // Smooth progress animation
  val animatedFillFraction by animateFloatAsState(
    targetValue = (progressFraction).coerceIn(0f, 1f),
    animationSpec = tween(durationMillis = 650, easing = FastOutSlowInEasing),
    label = "water_fill_animation"
  )

  Surface(
    shape = RoundedCornerShape(24.dp),
    color = MaterialTheme.colorScheme.surface,
    tonalElevation = 2.dp,
    modifier = modifier.fillMaxWidth()
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(18.dp)
    ) {
      // Header: Title, Reminder, Quick Reset
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Box(
            modifier = Modifier
              .size(40.dp)
              .background(WaterColor.copy(alpha = 0.15f), CircleShape),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.Opacity,
              contentDescription = "Water intake",
              tint = WaterColor,
              modifier = Modifier.size(22.dp)
            )
          }
          Spacer(modifier = Modifier.width(12.dp))
          Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Text(
                text = "Daily Hydration",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
              )
              if (isGoalMet) {
                Spacer(modifier = Modifier.width(6.dp))
                Icon(
                  imageVector = Icons.Default.CheckCircle,
                  contentDescription = "Hydration goal reached",
                  tint = Emerald500,
                  modifier = Modifier.size(16.dp)
                )
              }
            }
            Text(
              text = if (isGoalMet) "Goal Achieved! Hydration optimal 🎉" else "Target: $targetDisplay ($targetGlasses glasses)",
              style = MaterialTheme.typography.bodySmall,
              color = if (isGoalMet) Emerald600 else MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
          IconButton(
            onClick = onReminderClick,
            modifier = Modifier
              .size(36.dp)
              .testTag("water_reminder_button")
          ) {
            Icon(
              imageVector = Icons.Default.NotificationsActive,
              contentDescription = "Hydration reminder",
              tint = WaterColor,
              modifier = Modifier.size(20.dp)
            )
          }

          if (currentGlasses > 0) {
            IconButton(
              onClick = { onSetExactGlasses(0) },
              modifier = Modifier
                .size(36.dp)
                .testTag("reset_water_button")
            ) {
              Icon(
                imageVector = Icons.Default.RestartAlt,
                contentDescription = "Reset hydration log",
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(18.dp)
              )
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(18.dp))

      // Centerpiece: Fluid Hydration Visualizer
      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        // Interactive Animated Water Bottle / Vessel with isolated wave drawing
        WaterReservoirVisualizer(
          animatedFillFraction = animatedFillFraction,
          currentDisplay = currentDisplay,
          progressFraction = progressFraction,
          modifier = Modifier
            .weight(1f)
            .height(130.dp)
        )

        Spacer(modifier = Modifier.width(16.dp))

        // Right side: Glass Counter & Primary Increments
        Column(
          horizontalAlignment = Alignment.CenterHorizontally,
          modifier = Modifier.width(110.dp)
        ) {
          Text(
            text = "Glasses",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.padding(vertical = 4.dp)
          ) {
            IconButton(
              onClick = { onUpdateGlasses(-1) },
              enabled = currentGlasses > 0,
              modifier = Modifier
                .size(34.dp)
                .background(MaterialTheme.colorScheme.surfaceVariant, CircleShape)
                .testTag("decrease_water_button")
            ) {
              Icon(
                imageVector = Icons.Default.Remove,
                contentDescription = "Decrease water by 1 glass",
                tint = if (currentGlasses > 0) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f),
                modifier = Modifier.size(16.dp)
              )
            }

            Text(
              text = "$currentGlasses",
              style = MaterialTheme.typography.headlineMedium,
              fontWeight = FontWeight.Black,
              color = MaterialTheme.colorScheme.onSurface,
              modifier = Modifier.padding(horizontal = 10.dp)
            )

            IconButton(
              onClick = { onUpdateGlasses(1) },
              modifier = Modifier
                .size(34.dp)
                .background(WaterColor, CircleShape)
                .testTag("increase_water_button")
            ) {
              Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add 1 glass of water",
                tint = Color.White,
                modifier = Modifier.size(18.dp)
              )
            }
          }

          Text(
            text = "/ $targetGlasses glasses",
            fontSize = 11.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Quick-log Portion Chips (+150ml, +250ml glass, +500ml bottle)
      Text(
        text = "Quick Hydration Log",
        style = MaterialTheme.typography.labelMedium,
        fontWeight = FontWeight.SemiBold,
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )

      Spacer(modifier = Modifier.height(8.dp))

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        QuickLogPortionButton(
          label = if (isImperial) "+8 oz" else "+250 ml",
          sublabel = "1 Glass",
          onClick = { onUpdateGlasses(1) },
          tag = "quick_add_glass",
          modifier = Modifier.weight(1f)
        )
        QuickLogPortionButton(
          label = if (isImperial) "+16 oz" else "+500 ml",
          sublabel = "1 Bottle",
          onClick = { onUpdateGlasses(2) },
          tag = "quick_add_bottle",
          modifier = Modifier.weight(1f)
        )
        QuickLogPortionButton(
          label = if (isImperial) "+24 oz" else "+750 ml",
          sublabel = "Flask",
          onClick = { onUpdateGlasses(3) },
          tag = "quick_add_flask",
          modifier = Modifier.weight(1f)
        )
      }

      // Visual Glass Dots Tracker
      Spacer(modifier = Modifier.height(14.dp))
      GlassDotsIndicator(
        current = currentGlasses,
        target = targetGlasses
      )
    }
  }
}

@Composable
fun QuickLogPortionButton(
  label: String,
  sublabel: String,
  onClick: () -> Unit,
  tag: String,
  modifier: Modifier = Modifier
) {
  Surface(
    shape = RoundedCornerShape(14.dp),
    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
    modifier = modifier
      .clip(RoundedCornerShape(14.dp))
      .clickable(onClick = onClick)
      .testTag(tag)
  ) {
    Column(
      horizontalAlignment = Alignment.CenterHorizontally,
      modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp)
    ) {
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
      ) {
        Icon(
          imageVector = Icons.Default.LocalDrink,
          contentDescription = null,
          tint = WaterColor,
          modifier = Modifier.size(13.dp)
        )
        Spacer(modifier = Modifier.width(3.dp))
        Text(
          text = label,
          style = MaterialTheme.typography.labelMedium,
          fontWeight = FontWeight.ExtraBold,
          color = MaterialTheme.colorScheme.onSurface
        )
      }
      Text(
        text = sublabel,
        fontSize = 10.sp,
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )
    }
  }
}

@Composable
fun GlassDotsIndicator(
  current: Int,
  target: Int,
  modifier: Modifier = Modifier
) {
  val totalSlots = (if (target > 0) target else 8).coerceAtLeast(1)
  val displaySlots = totalSlots.coerceAtMost(12)

  Row(
    modifier = modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically
  ) {
    for (i in 1..displaySlots) {
      val isFilled = i <= current
      val isBonus = i > totalSlots

      Box(
        modifier = Modifier
          .weight(1f)
          .padding(horizontal = 2.dp)
          .height(6.dp)
          .clip(RoundedCornerShape(3.dp))
          .background(
            when {
              isFilled && isBonus -> Emerald500
              isFilled -> WaterColor
              else -> MaterialTheme.colorScheme.surfaceVariant
            }
          )
      )
    }
  }
}

@Composable
fun WaterReservoirVisualizer(
  animatedFillFraction: Float,
  currentDisplay: String,
  progressFraction: Float,
  modifier: Modifier = Modifier
) {
  // Scoped isolated wave phase translation: only invalidates this leaf visualizer
  val infiniteTransition = rememberInfiniteTransition(label = "reservoir_wave_loop")
  val wavePhase by infiniteTransition.animateFloat(
    initialValue = 0f,
    targetValue = (2 * Math.PI).toFloat(),
    animationSpec = infiniteRepeatable(
      animation = tween(durationMillis = 3600, easing = LinearEasing),
      repeatMode = RepeatMode.Restart
    ),
    label = "wave_phase"
  )

  Box(
    modifier = modifier
      .clip(RoundedCornerShape(20.dp))
      .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f))
      .border(
        width = 1.dp,
        color = WaterColor.copy(alpha = 0.25f),
        shape = RoundedCornerShape(20.dp)
      ),
    contentAlignment = Alignment.Center
  ) {
    Canvas(
      modifier = Modifier
        .matchParentSize()
        .semantics {
          contentDescription = "Hydration level at ${(animatedFillFraction * 100).toInt()} percent"
        }
    ) {
      val width = size.width
      val height = size.height

      if (width <= 0f || height <= 0f) return@Canvas

      val fillHeight = height * animatedFillFraction
      val waterTopY = height - fillHeight

      if (animatedFillFraction > 0f) {
        val waveAmplitude = (4.dp.toPx() * (1f - (animatedFillFraction - 0.5f).let { if (it < 0) -it else it } * 1.5f)).coerceIn(1.5f, 6.dp.toPx())
        val waveLength = (width * 0.85f).coerceAtLeast(10f)

        // Background secondary subtle wave with smooth cubic curve approximation
        val backPath = Path().apply {
          moveTo(0f, height)
          lineTo(0f, waterTopY)
          val step = (width / 16f).coerceAtLeast(16f)
          var x = 0f
          while (x < width) {
            val nextX = (x + step).coerceAtMost(width)
            val midX = (x + nextX) / 2f
            val yMid = waterTopY + sin((midX / waveLength * 2 * Math.PI + wavePhase + 1.2f).toDouble()).toFloat() * (waveAmplitude * 0.7f)
            val yNext = waterTopY + sin((nextX / waveLength * 2 * Math.PI + wavePhase + 1.2f).toDouble()).toFloat() * (waveAmplitude * 0.7f)
            quadraticTo(midX, yMid, nextX, yNext)
            x = nextX
          }
          lineTo(width, height)
          close()
        }
        drawPath(
          path = backPath,
          color = WaterColor.copy(alpha = 0.30f)
        )

        // Main Foreground Wave with smooth curve
        val path = Path().apply {
          moveTo(0f, height)
          lineTo(0f, waterTopY)
          val step = (width / 16f).coerceAtLeast(16f)
          var x = 0f
          while (x < width) {
            val nextX = (x + step).coerceAtMost(width)
            val midX = (x + nextX) / 2f
            val yMid = waterTopY + sin((midX / waveLength * 2 * Math.PI + wavePhase).toDouble()).toFloat() * waveAmplitude
            val yNext = waterTopY + sin((nextX / waveLength * 2 * Math.PI + wavePhase).toDouble()).toFloat() * waveAmplitude
            quadraticTo(midX, yMid, nextX, yNext)
            x = nextX
          }
          lineTo(width, height)
          close()
        }

        drawPath(
          path = path,
          brush = Brush.verticalGradient(
            colors = listOf(
              WaterColor,
              Color(0xFF0284C7),
              Color(0xFF0369A1)
            ),
            startY = (waterTopY - waveAmplitude).coerceAtLeast(0f),
            endY = height
          )
        )
      }

      // Reference target line (dotted / subtle guideline at 100%)
      val targetLineY = height * 0.08f
      drawLine(
        color = Color.White.copy(alpha = 0.4f),
        start = Offset(12.dp.toPx(), targetLineY),
        end = Offset(width - 12.dp.toPx(), targetLineY),
        strokeWidth = 1.5.dp.toPx(),
        cap = StrokeCap.Round
      )
    }

    // Stat overlay in middle of reservoir
    Column(
      horizontalAlignment = Alignment.CenterHorizontally,
      modifier = Modifier.padding(horizontal = 12.dp)
    ) {
      Text(
        text = currentDisplay,
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Black,
        color = if (animatedFillFraction > 0.45f) Color.White else MaterialTheme.colorScheme.onSurface
      )
      Text(
        text = "${(progressFraction * 100).toInt()}% of goal",
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        color = if (animatedFillFraction > 0.45f) Color.White.copy(alpha = 0.9f) else MaterialTheme.colorScheme.onSurfaceVariant
      )
    }
  }
}

