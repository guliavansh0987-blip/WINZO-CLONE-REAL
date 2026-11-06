package com.example.ui.games

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.WinzoBgDark
import com.example.ui.theme.WinzoCrimson
import com.example.ui.theme.WinzoEmerald
import com.example.ui.theme.WinzoGold
import kotlinx.coroutines.delay
import kotlin.math.hypot
import kotlin.random.Random

data class ArrowHit(val offset: Offset, val points: Int)

@Composable
fun ArcheryGame(
    onGameEnd: (score: Int) -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    var score by remember { mutableIntStateOf(0) }
    var arrowsLeft by remember { mutableIntStateOf(7) }
    var windSpeed by remember { mutableFloatStateOf(Random.nextFloat() * 6f - 3f) } // -3 to +3 m/s
    var aimOffsetX by remember { mutableFloatStateOf(0f) }
    var aimOffsetY by remember { mutableFloatStateOf(0f) }
    var bowTension by remember { mutableFloatStateOf(0.7f) }
    var lastShotText by remember { mutableStateOf("Aim & Release Arrow!") }
    var isGameOver by remember { mutableStateOf(false) }

    val hits = remember { mutableStateListOf<ArrowHit>() }

    fun vibrate(ms: Long) {
        try {
            val v = context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                v?.vibrate(VibrationEffect.createOneShot(ms, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                @Suppress("DEPRECATION")
                v?.vibrate(ms)
            }
        } catch (_: Exception) {}
    }

    fun releaseArrow(targetCenterX: Float, targetCenterY: Float) {
        if (arrowsLeft <= 0 || isGameOver) return

        vibrate(60)
        arrowsLeft--

        // Calculate impact with wind drift
        val windDrift = windSpeed * 22f
        val hitX = targetCenterX + aimOffsetX + windDrift
        val hitY = targetCenterY + aimOffsetY + (1f - bowTension) * 50f

        val distFromCenter = hypot(hitX - targetCenterX, hitY - targetCenterY)

        val pts = when {
            distFromCenter < 25f -> 50 // Perfect Bullseye!
            distFromCenter < 60f -> 30 // Inner Red
            distFromCenter < 100f -> 20 // Blue
            distFromCenter < 150f -> 10 // Black
            distFromCenter < 200f -> 5  // White outer
            else -> 0
        }

        score += pts
        hits.add(ArrowHit(Offset(hitX, hitY), pts))

        lastShotText = when (pts) {
            50 -> "🎯 BULLSEYE! +50 PTS"
            30 -> "🔥 GREAT SHOT! +30 PTS"
            20 -> "✨ NICE HIT! +20 PTS"
            10 -> "👍 HIT! +10 PTS"
            else -> "💨 MISSED TARGET!"
        }

        // Change wind for next shot
        windSpeed = Random.nextFloat() * 7f - 3.5f

        if (arrowsLeft <= 0) {
            isGameOver = true
            onGameEnd(score)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(WinzoBgDark, Color(0xFF161036), WinzoBgDark)
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Top Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }

                Surface(
                    color = WinzoGold.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "🏹 Arrows: $arrowsLeft",
                        color = WinzoGold,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                    )
                }

                Surface(
                    color = Color(0xFF2979FF).copy(alpha = 0.25f),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    val windDirection = if (windSpeed > 0) "East ➡️" else "West ⬅️"
                    Text(
                        text = "💨 ${String.format("%.1f", kotlin.math.abs(windSpeed))} m/s $windDirection",
                        color = Color(0xFF80D8FF),
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "SCORE: $score",
                fontSize = 32.sp,
                fontWeight = FontWeight.Black,
                color = WinzoGold
            )
            Text(
                text = lastShotText,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = if (lastShotText.contains("BULLSEYE")) WinzoEmerald else Color.White
            )

            // Canvas with Archery Target and Drag Aiming
            var tX by remember { mutableFloatStateOf(0f) }
            var tY by remember { mutableFloatStateOf(0f) }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .pointerInput(Unit) {
                        detectDragGestures { change, dragAmount ->
                            change.consume()
                            aimOffsetX = (aimOffsetX + dragAmount.x).coerceIn(-180f, 180f)
                            aimOffsetY = (aimOffsetY + dragAmount.y).coerceIn(-180f, 180f)
                        }
                    }
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val cx = size.width / 2f
                    val cy = size.height * 0.42f
                    tX = cx
                    tY = cy

                    // Concentric Target Rings
                    val r5 = 190f // White ring
                    val r4 = 140f // Black ring
                    val r3 = 95f  // Blue ring
                    val r2 = 55f  // Red ring
                    val r1 = 25f  // Gold Bullseye

                    drawCircle(Color.White, radius = r5, center = Offset(cx, cy))
                    drawCircle(Color(0xFF37474F), radius = r4, center = Offset(cx, cy))
                    drawCircle(Color(0xFF1E88E5), radius = r3, center = Offset(cx, cy))
                    drawCircle(Color(0xFFE53935), radius = r2, center = Offset(cx, cy))
                    drawCircle(Color(0xFFFFD54F), radius = r1, center = Offset(cx, cy))
                    drawCircle(Color(0xFFFF8F00), radius = 10f, center = Offset(cx, cy))

                    // Previous Hit Marks
                    hits.forEach { hit ->
                        drawCircle(Color.Black, radius = 6f, center = hit.offset)
                        drawCircle(Color.Red, radius = 3f, center = hit.offset)
                    }

                    // Aiming Crosshairs
                    val crossX = cx + aimOffsetX
                    val crossY = cy + aimOffsetY

                    drawCircle(
                        color = Color(0x8800E5FF),
                        radius = 24f,
                        center = Offset(crossX, crossY),
                        style = Stroke(width = 3f)
                    )
                    drawLine(Color(0xFF00E5FF), Offset(crossX - 35f, crossY), Offset(crossX + 35f, crossY), strokeWidth = 2f)
                    drawLine(Color(0xFF00E5FF), Offset(crossX, crossY - 35f), Offset(crossX, crossY + 35f), strokeWidth = 2f)
                }
            }

            // Tension Slider & Shoot Button
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(
                    text = "Bow Draw Tension: ${(bowTension * 100).toInt()}%",
                    color = Color.LightGray,
                    fontSize = 13.sp
                )
                Slider(
                    value = bowTension,
                    onValueChange = { bowTension = it },
                    valueRange = 0.4f..1.0f,
                    colors = SliderDefaults.colors(
                        thumbColor = WinzoGold,
                        activeTrackColor = WinzoCrimson
                    )
                )

                Spacer(modifier = Modifier.height(6.dp))

                Button(
                    onClick = { releaseArrow(tX, tY) },
                    colors = ButtonDefaults.buttonColors(containerColor = WinzoEmerald),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp)
                ) {
                    Text(
                        text = "RELEASE ARROW 🎯",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }
            }
        }

        // Game Over Overlay
        if (isGameOver) {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = Color.Black.copy(alpha = 0.85f)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "CONTEST COMPLETED!",
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Black,
                        color = WinzoGold
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Final Archery Score: $score",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Button(
                        onClick = { onGameEnd(score) },
                        colors = ButtonDefaults.buttonColors(containerColor = WinzoEmerald),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Text(
                            text = "CLAIM WINNINGS",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }
                }
            }
        }
    }
}
