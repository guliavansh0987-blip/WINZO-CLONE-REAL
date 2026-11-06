package com.example.ui.games

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
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
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
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
import kotlin.random.Random

data class RunnerObstacle(
    val id: Long,
    val lane: Int, // 0, 1, 2
    var y: Float,
    val isTrain: Boolean,
    val height: Float = if (isTrain) 180f else 90f
)

data class RunnerCoin(
    val id: Long,
    val lane: Int,
    var y: Float,
    var isCollected: Boolean = false
)

@Composable
fun MetroRunnerGame(
    onGameEnd: (score: Int) -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    var playerLane by remember { mutableIntStateOf(1) } // 0=left, 1=center, 2=right
    var score by remember { mutableIntStateOf(0) }
    var coinsCollected by remember { mutableIntStateOf(0) }
    var distanceMeters by remember { mutableIntStateOf(0) }
    var lives by remember { mutableIntStateOf(3) }
    var isGameOver by remember { mutableStateOf(false) }

    val obstacles = remember { mutableStateListOf<RunnerObstacle>() }
    val coins = remember { mutableStateListOf<RunnerCoin>() }

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

    // Spawner Loop
    LaunchedEffect(isGameOver) {
        var idCounter = 0L
        while (!isGameOver) {
            delay(1200)
            val lane = Random.nextInt(0, 3)
            val isTrain = Random.nextBoolean()
            obstacles.add(
                RunnerObstacle(
                    id = idCounter++,
                    lane = lane,
                    y = -200f,
                    isTrain = isTrain
                )
            )

            // Spawn coins in an empty lane
            val coinLane = (lane + 1) % 3
            for (i in 0..2) {
                coins.add(
                    RunnerCoin(
                        id = idCounter++,
                        lane = coinLane,
                        y = -250f - (i * 70f)
                    )
                )
            }
        }
    }

    // Game Physics & Scroll Loop
    LaunchedEffect(isGameOver) {
        val speed = 14f
        val playerY = 1200f
        val playerHeight = 100f

        while (!isGameOver) {
            distanceMeters += 2
            score = distanceMeters + (coinsCollected * 25)

            // Move obstacles
            val obsIterator = obstacles.iterator()
            while (obsIterator.hasNext()) {
                val obs = obsIterator.next()
                obs.y += speed

                // Collision check
                if (obs.lane == playerLane && obs.y in (playerY - obs.height)..(playerY + playerHeight * 0.6f)) {
                    vibrate(180)
                    lives--
                    obsIterator.remove()
                    if (lives <= 0) {
                        isGameOver = true
                        onGameEnd(score)
                    }
                } else if (obs.y > 1700f) {
                    obsIterator.remove()
                }
            }

            // Move coins
            val coinIterator = coins.iterator()
            while (coinIterator.hasNext()) {
                val c = coinIterator.next()
                c.y += speed

                if (!c.isCollected && c.lane == playerLane && c.y in (playerY - 40f)..(playerY + playerHeight)) {
                    c.isCollected = true
                    coinsCollected++
                    vibrate(25)
                    coinIterator.remove()
                } else if (c.y > 1700f) {
                    coinIterator.remove()
                }
            }

            delay(16)
        }
    }

    // Animated player X
    val playerTargetXFraction = when (playerLane) {
        0 -> 0.22f
        1 -> 0.50f
        else -> 0.78f
    }
    val animatedPlayerX by animateFloatAsState(
        targetValue = playerTargetXFraction,
        animationSpec = tween(durationMillis = 140),
        label = "player_x"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF0A071E), Color(0xFF1B153D), WinzoBgDark)
                )
            )
            .pointerInput(Unit) {
                detectHorizontalDragGestures { _, dragAmount ->
                    if (dragAmount > 25 && playerLane < 2) {
                        playerLane++
                        vibrate(20)
                    } else if (dragAmount < -25 && playerLane > 0) {
                        playerLane--
                        vibrate(20)
                    }
                }
            }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
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
                        text = "🪙 $coinsCollected Coins",
                        color = WinzoGold,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }

                Surface(
                    color = WinzoCrimson.copy(alpha = 0.25f),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "❤️".repeat(lives.coerceAtLeast(0)),
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "$distanceMeters m  |  SCORE: $score",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Black,
                    color = Color.White
                )
            }

            // Game Canvas
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val w = size.width
                    val h = size.height

                    // 3 Tracks
                    val lane0X = w * 0.22f
                    val lane1X = w * 0.50f
                    val lane2X = w * 0.78f
                    val laneWidth = w * 0.25f

                    // Draw Track Rails
                    val trackColors = listOf(Color(0xFF2A2352), Color(0xFF1E173D))
                    drawLine(Color(0x55FFFFFF), Offset(w * 0.36f, 0f), Offset(w * 0.36f, h), strokeWidth = 3f)
                    drawLine(Color(0x55FFFFFF), Offset(w * 0.64f, 0f), Offset(w * 0.64f, h), strokeWidth = 3f)

                    // Draw Coins
                    coins.forEach { coin ->
                        if (!coin.isCollected) {
                            val coinX = when (coin.lane) {
                                0 -> lane0X
                                1 -> lane1X
                                else -> lane2X
                            }
                            drawCircle(
                                color = WinzoGold,
                                radius = 18f,
                                center = Offset(coinX, coin.y)
                            )
                            drawCircle(
                                color = Color(0xFFFFE082),
                                radius = 10f,
                                center = Offset(coinX, coin.y)
                            )
                        }
                    }

                    // Draw Obstacles (Trains and Hurdles)
                    obstacles.forEach { obs ->
                        val obsX = when (obs.lane) {
                            0 -> lane0X
                            1 -> lane1X
                            else -> lane2X
                        }
                        if (obs.isTrain) {
                            // Train Body
                            drawRoundRect(
                                color = Color(0xFFD32F2F),
                                topLeft = Offset(obsX - laneWidth * 0.42f, obs.y),
                                size = Size(laneWidth * 0.84f, obs.height),
                                cornerRadius = CornerRadius(16f, 16f)
                            )
                            // Train Windows
                            drawRoundRect(
                                color = Color(0xFF81D4FA),
                                topLeft = Offset(obsX - laneWidth * 0.32f, obs.y + 16f),
                                size = Size(laneWidth * 0.64f, 40f),
                                cornerRadius = CornerRadius(8f, 8f)
                            )
                        } else {
                            // Barrier Hurdle
                            drawRoundRect(
                                color = Color(0xFFFF9800),
                                topLeft = Offset(obsX - laneWidth * 0.45f, obs.y),
                                size = Size(laneWidth * 0.9f, 45f),
                                cornerRadius = CornerRadius(8f, 8f)
                            )
                            drawRect(
                                color = Color.White,
                                topLeft = Offset(obsX - laneWidth * 0.15f, obs.y),
                                size = Size(laneWidth * 0.3f, 45f)
                            )
                        }
                    }

                    // Draw Runner Character
                    val runnerX = w * animatedPlayerX
                    val runnerY = 1200f
                    // Character Glow Shadow
                    drawCircle(
                        color = Color(0x6600E5FF),
                        radius = 35f,
                        center = Offset(runnerX, runnerY + 10f)
                    )
                    // Torso & Head
                    drawCircle(
                        color = Color(0xFF00E5FF),
                        radius = 28f,
                        center = Offset(runnerX, runnerY)
                    )
                    drawCircle(
                        color = Color.White,
                        radius = 12f,
                        center = Offset(runnerX, runnerY - 6f)
                    )
                }
            }

            // On-screen Lane Switch Controls for Touch Accessibility
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = {
                        if (playerLane > 0) {
                            playerLane--
                            vibrate(20)
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C2454)),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(52.dp)
                        .padding(end = 8.dp)
                ) {
                    Text("⬅️ LEFT LANE", fontWeight = FontWeight.Bold, color = Color.White)
                }

                Button(
                    onClick = {
                        if (playerLane < 2) {
                            playerLane++
                            vibrate(20)
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C2454)),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(52.dp)
                        .padding(start = 8.dp)
                ) {
                    Text("RIGHT LANE ➡️", fontWeight = FontWeight.Bold, color = Color.White)
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
                        text = "CRASHED!",
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Black,
                        color = WinzoCrimson
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Distance: ${distanceMeters}m | Coins: $coinsCollected",
                        fontSize = 18.sp,
                        color = Color.LightGray
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Total Score: $score",
                        fontSize = 28.sp,
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
                            text = "SETTLE MATCH",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }
                }
            }
        }
    }
}
