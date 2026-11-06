package com.example.ui.games

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
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
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.WinzoBgDark
import com.example.ui.theme.WinzoCrimson
import com.example.ui.theme.WinzoEmerald
import com.example.ui.theme.WinzoGold
import kotlinx.coroutines.delay
import kotlin.math.abs

@Composable
fun KnifeUpGame(
    onGameEnd: (score: Int) -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    var score by remember { mutableIntStateOf(0) }
    var stage by remember { mutableIntStateOf(1) }
    var knivesToThrow by remember { mutableIntStateOf(7) }
    var timeLeft by remember { mutableIntStateOf(45) }
    var isGameOver by remember { mutableStateOf(false) }
    var isVictory by remember { mutableStateOf(false) }

    // Wood rotation angle
    var targetAngle by remember { mutableFloatStateOf(0f) }
    val embeddedKnives = remember { mutableStateListOf<Float>(0f, 120f) }

    // Flying knife animation
    var flyingKnifeProgress by remember { mutableFloatStateOf(0f) }
    var isKnifeFlying by remember { mutableStateOf(false) }

    // Game loop for rotation & timer
    LaunchedEffect(isGameOver) {
        if (isGameOver) return@LaunchedEffect
        var rotationSpeed = 2.4f
        while (!isGameOver && timeLeft > 0) {
            targetAngle = (targetAngle + rotationSpeed) % 360f
            delay(16)
        }
    }

    // Countdown Timer
    LaunchedEffect(isGameOver) {
        while (!isGameOver && timeLeft > 0) {
            delay(1000)
            timeLeft--
            if (timeLeft <= 0) {
                isGameOver = true
                onGameEnd(score)
            }
        }
    }

    fun vibrateDevice(pattern: Long = 40) {
        try {
            val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator?.vibrate(VibrationEffect.createOneShot(pattern, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                @Suppress("DEPRECATION")
                vibrator?.vibrate(pattern)
            }
        } catch (_: Exception) {}
    }

    fun throwKnife() {
        if (isKnifeFlying || isGameOver || knivesToThrow <= 0) return

        isKnifeFlying = true
        vibrateDevice(25)

        // Calculate if hits existing knife
        // When knife reaches target (angle at 270 deg / bottom)
        val hitAngle = (270f - targetAngle + 360f) % 360f
        val collides = embeddedKnives.any { existing ->
            val diff = abs(existing - hitAngle)
            val minDiff = if (diff > 180) 360 - diff else diff
            minDiff < 14f
        }

        if (collides) {
            vibrateDevice(150)
            isGameOver = true
            isVictory = false
            onGameEnd(score)
        } else {
            embeddedKnives.add(hitAngle)
            score += 15
            knivesToThrow--
            isKnifeFlying = false

            if (knivesToThrow <= 0) {
                // Next Stage
                stage++
                knivesToThrow = 6 + stage
                score += 50
                embeddedKnives.clear()
                embeddedKnives.add(0f)
                if (stage > 2) embeddedKnives.add(180f)
                vibrateDevice(80)
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(WinzoBgDark, Color(0xFF1E1338), WinzoBgDark)
                )
            )
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                throwKnife()
            }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(
                        Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }

                Surface(
                    color = WinzoGold.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "STAGE $stage",
                        color = WinzoGold,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                    )
                }

                Surface(
                    color = WinzoCrimson.copy(alpha = 0.25f),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "⏱️ ${timeLeft}s",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                    )
                }
            }

            // Score Banner
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "SCORE: $score",
                fontSize = 32.sp,
                fontWeight = FontWeight.ExtraBold,
                color = WinzoGold
            )

            Text(
                text = "Knives Left: $knivesToThrow",
                fontSize = 14.sp,
                color = Color.LightGray
            )

            Spacer(modifier = Modifier.height(30.dp))

            // Canvas for Target Log and Knives
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val centerX = size.width / 2f
                    val centerY = size.height * 0.35f
                    val logRadius = 85.dp.toPx()

                    // Draw Log with rotating wood texture
                    rotate(targetAngle, pivot = Offset(centerX, centerY)) {
                        // Wood Base
                        drawCircle(
                            brush = Brush.radialGradient(
                                listOf(Color(0xFF8D6E63), Color(0xFF4E342E), Color(0xFF2E1C14)),
                                center = Offset(centerX, centerY),
                                radius = logRadius
                            ),
                            radius = logRadius,
                            center = Offset(centerX, centerY)
                        )
                        // Wood Rings
                        drawCircle(
                            color = Color(0x33000000),
                            radius = logRadius * 0.7f,
                            center = Offset(centerX, centerY)
                        )
                        drawCircle(
                            color = Color(0x33000000),
                            radius = logRadius * 0.4f,
                            center = Offset(centerX, centerY)
                        )

                        // Embedded knives rotating with log
                        embeddedKnives.forEach { knifeAngle ->
                            rotate(knifeAngle, pivot = Offset(centerX, centerY)) {
                                drawKnife(
                                    centerX = centerX,
                                    startY = centerY + logRadius - 10f,
                                    length = 65.dp.toPx()
                                )
                            }
                        }
                    }

                    // Draw Player's Knife Ready to Throw at Bottom
                    val bottomKnifeY = size.height * 0.82f
                    drawKnife(
                        centerX = centerX,
                        startY = bottomKnifeY,
                        length = 75.dp.toPx()
                    )
                }
            }

            // Throw Instruction / Action Button
            Button(
                onClick = { throwKnife() },
                colors = ButtonDefaults.buttonColors(containerColor = WinzoCrimson),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text(
                    text = "TAP TO THROW KNIFE",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
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
                        text = "GAME OVER!",
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Black,
                        color = WinzoCrimson
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Final Score: $score",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = WinzoGold
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Button(
                        onClick = { onGameEnd(score) },
                        colors = ButtonDefaults.buttonColors(containerColor = WinzoEmerald),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Text(
                            text = "CONTINUE & SETTLE",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }
                }
            }
        }
    }
}

private fun DrawScope.drawKnife(centerX: Float, startY: Float, length: Float) {
    // Silver blade
    val bladePath = Path().apply {
        moveTo(centerX, startY + length)
        lineTo(centerX - 9f, startY + length * 0.45f)
        lineTo(centerX + 9f, startY + length * 0.45f)
        close()
    }
    drawPath(bladePath, color = Color(0xFFECEFF1))

    // Handle
    drawRect(
        color = Color(0xFFFF9800),
        topLeft = Offset(centerX - 6f, startY),
        size = androidx.compose.ui.geometry.Size(12f, length * 0.45f)
    )

    // Pommel / guard
    drawCircle(
        color = Color(0xFFFFD54F),
        radius = 8f,
        center = Offset(centerX, startY)
    )
}
