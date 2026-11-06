package com.example.ui.games

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
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
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

data class BubbleCell(
    val row: Int,
    val col: Int,
    var color: Color,
    var isPopped: Boolean = false
)

@Composable
fun BubbleShooterGame(
    onGameEnd: (score: Int) -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    var score by remember { mutableIntStateOf(0) }
    var timeLeft by remember { mutableIntStateOf(45) }
    var isGameOver by remember { mutableStateOf(false) }

    val bubbleColors = remember {
        listOf(
            Color(0xFFFF1744), // Crimson Red
            Color(0xFF2979FF), // Royal Blue
            Color(0xFF00E676), // Emerald Green
            Color(0xFFFFD600), // Amber Yellow
            Color(0xFFAA00FF)  // Purple
        )
    }

    var currentBulletColor by remember { mutableStateOf(bubbleColors.random()) }
    var nextBulletColor by remember { mutableStateOf(bubbleColors.random()) }

    // Bubble Grid (5 rows x 7 cols)
    val grid = remember {
        mutableStateListOf<BubbleCell>().apply {
            for (r in 0 until 5) {
                for (c in 0 until 7) {
                    add(BubbleCell(r, c, bubbleColors.random()))
                }
            }
        }
    }

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

    // Timer Loop
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

    fun fireBubbleTowards(targetX: Float, targetY: Float, canvasWidth: Float, canvasHeight: Float) {
        if (isGameOver) return

        vibrate(30)
        // Find bottom-most unpopped bubble in nearest column
        val colWidth = canvasWidth / 7f
        val hitCol = (targetX / colWidth).toInt().coerceIn(0, 6)

        // Find available row
        val matchingInCol = grid.filter { it.col == hitCol && !it.isPopped }
        if (matchingInCol.isNotEmpty()) {
            val lowestBubble = matchingInCol.maxByOrNull { it.row }
            if (lowestBubble != null) {
                if (lowestBubble.color == currentBulletColor) {
                    // Match found! Pop it and adjacent
                    vibrate(70)
                    lowestBubble.isPopped = true
                    score += 30

                    // Check neighbors in same row or above
                    grid.filter {
                        !it.isPopped && it.color == currentBulletColor &&
                                (it.row == lowestBubble.row || it.row == lowestBubble.row - 1) &&
                                kotlin.math.abs(it.col - lowestBubble.col) <= 1
                    }.forEach {
                        it.isPopped = true
                        score += 25
                    }
                } else {
                    // Mismatch, deduct tiny score
                    score = (score - 5).coerceAtLeast(0)
                }
            }
        } else {
            // Respawn a row if all cleared in that column
            score += 40
        }

        // Shift next bubble to current
        currentBulletColor = nextBulletColor
        nextBulletColor = bubbleColors.random()

        // Check if all popped
        if (grid.all { it.isPopped }) {
            // Respawn fresh board
            grid.forEach {
                it.isPopped = false
                it.color = bubbleColors.random()
            }
            score += 150
            vibrate(120)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(WinzoBgDark, Color(0xFF140E2D), WinzoBgDark)
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Header Bar
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
                        text = "SCORE: $score",
                        color = WinzoGold,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
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

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Tap on column to aim and match same colors!",
                fontSize = 13.sp,
                color = Color.LightGray,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            // Canvas with Bubble Grid and Cannon
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                var cWidth by remember { mutableFloatStateOf(1000f) }
                var cHeight by remember { mutableFloatStateOf(1500f) }

                Canvas(
                    modifier = Modifier
                        .fillMaxSize()
                        .pointerInput(Unit) {
                            detectTapGestures { offset ->
                                fireBubbleTowards(offset.x, offset.y, cWidth, cHeight)
                            }
                        }
                ) {
                    cWidth = size.width
                    cHeight = size.height

                    val colWidth = size.width / 7f
                    val bubbleRadius = colWidth * 0.44f

                    // Draw ceiling line
                    drawLine(
                        color = WinzoGold,
                        start = Offset(0f, 10f),
                        end = Offset(size.width, 10f),
                        strokeWidth = 6f
                    )

                    // Draw Grid Bubbles
                    grid.forEach { cell ->
                        if (!cell.isPopped) {
                            val cx = (cell.col * colWidth) + (colWidth / 2f)
                            val cy = 40f + (cell.row * bubbleRadius * 2.1f) + bubbleRadius

                            // 3D Bubble Effect
                            drawCircle(
                                brush = Brush.radialGradient(
                                    listOf(Color.White.copy(alpha = 0.9f), cell.color, cell.color.copy(alpha = 0.7f)),
                                    center = Offset(cx - bubbleRadius * 0.3f, cy - bubbleRadius * 0.3f),
                                    radius = bubbleRadius
                                ),
                                radius = bubbleRadius,
                                center = Offset(cx, cy)
                            )
                        }
                    }

                    // Draw Bottom Cannon Base
                    val cannonX = size.width / 2f
                    val cannonY = size.height - 80f

                    drawCircle(
                        color = Color(0xFF31275A),
                        radius = 60f,
                        center = Offset(cannonX, cannonY)
                    )

                    // Current ready bubble
                    drawCircle(
                        brush = Brush.radialGradient(
                            listOf(Color.White, currentBulletColor),
                            center = Offset(cannonX - 8f, cannonY - 8f),
                            radius = bubbleRadius * 1.1f
                        ),
                        radius = bubbleRadius * 1.1f,
                        center = Offset(cannonX, cannonY)
                    )

                    // Next bubble preview
                    drawCircle(
                        color = nextBulletColor,
                        radius = bubbleRadius * 0.6f,
                        center = Offset(cannonX + 90f, cannonY + 20f)
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
                        text = "ROUND FINISHED!",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Black,
                        color = WinzoGold
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Score: $score",
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
