package com.example.ui.games

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
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
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
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

data class FruitItem(
    val id: Long,
    var x: Float,
    var y: Float,
    var vx: Float,
    var vy: Float,
    val color: Color,
    val innerColor: Color,
    val isBomb: Boolean = false,
    var isSliced: Boolean = false,
    var sliceOffset: Float = 0f,
    val radius: Float = 55f
)

data class SlashPoint(val offset: Offset, val time: Long)

@Composable
fun FruitSlashGame(
    onGameEnd: (score: Int) -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    var score by remember { mutableIntStateOf(0) }
    var timeLeft by remember { mutableIntStateOf(45) }
    var isGameOver by remember { mutableStateOf(false) }
    var comboCount by remember { mutableIntStateOf(0) }

    val fruits = remember { mutableStateListOf<FruitItem>() }
    val trailPoints = remember { mutableStateListOf<SlashPoint>() }

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

    // Spawner loop
    LaunchedEffect(isGameOver) {
        var idCounter = 0L
        while (!isGameOver) {
            delay(Random.nextLong(600, 1200))
            if (fruits.size < 7) {
                val isBomb = Random.nextFloat() < 0.22f
                val fruitColors = listOf(
                    Pair(Color(0xFF2E7D32), Color(0xFFFF1744)), // Watermelon
                    Pair(Color(0xFFFF9800), Color(0xFFFFE082)), // Orange
                    Pair(Color(0xFFFDD835), Color(0xFFFFF9C4)), // Banana
                    Pair(Color(0xFF9C27B0), Color(0xFFE1BEE7))  // Plum
                ).random()

                fruits.add(
                    FruitItem(
                        id = idCounter++,
                        x = Random.nextFloat() * 700f + 100f,
                        y = 1500f,
                        vx = (Random.nextFloat() - 0.5f) * 8f,
                        vy = -(Random.nextFloat() * 12f + 24f),
                        color = if (isBomb) Color(0xFF212121) else fruitColors.first,
                        innerColor = if (isBomb) Color(0xFFFF1744) else fruitColors.second,
                        isBomb = isBomb
                    )
                )
            }
        }
    }

    // Physics Engine Loop
    LaunchedEffect(isGameOver) {
        val gravity = 0.85f
        while (!isGameOver) {
            val iterator = fruits.iterator()
            while (iterator.hasNext()) {
                val f = iterator.next()
                f.x += f.vx
                f.y += f.vy
                f.vy += gravity

                if (f.isSliced) {
                    f.sliceOffset += 8f
                }

                // Clean out of bounds
                if (f.y > 1700f && f.vy > 0) {
                    iterator.remove()
                }
            }

            // Clean old trail points
            val now = System.currentTimeMillis()
            trailPoints.removeAll { now - it.time > 150 }

            delay(16)
        }
    }

    fun checkSlice(point: Offset) {
        fruits.forEach { fruit ->
            if (!fruit.isSliced) {
                val dist = hypot(fruit.x - point.x, fruit.y - point.y)
                if (dist < fruit.radius * 1.3f) {
                    fruit.isSliced = true
                    if (fruit.isBomb) {
                        vibrate(200)
                        score = (score - 50).coerceAtLeast(0)
                        comboCount = 0
                    } else {
                        vibrate(40)
                        comboCount++
                        val bonus = if (comboCount > 3) 25 else 15
                        score += bonus
                    }
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(WinzoBgDark, Color(0xFF1B1533), WinzoBgDark)
                )
            )
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = { offset ->
                        trailPoints.add(SlashPoint(offset, System.currentTimeMillis()))
                        checkSlice(offset)
                    },
                    onDrag = { change, _ ->
                        change.consume()
                        trailPoints.add(SlashPoint(change.position, System.currentTimeMillis()))
                        checkSlice(change.position)
                    }
                )
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
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                }

                Surface(
                    color = WinzoGold.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "COMBO x$comboCount",
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
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "SCORE: $score",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = WinzoGold
                )
            }
            Text(
                text = "Swipe fingers to slice juicy fruits! Avoid bombs 💣",
                fontSize = 12.sp,
                color = Color.LightGray,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            // Game Area Canvas
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    // Draw Fruits
                    fruits.forEach { fruit ->
                        if (!fruit.isSliced) {
                            if (fruit.isBomb) {
                                // Bomb
                                drawCircle(
                                    color = Color.Black,
                                    radius = fruit.radius,
                                    center = Offset(fruit.x, fruit.y)
                                )
                                drawCircle(
                                    color = Color(0xFFFF1744),
                                    radius = 12f,
                                    center = Offset(fruit.x + 8f, fruit.y - 8f)
                                )
                            } else {
                                // Whole Fruit Outer & Inner
                                drawCircle(
                                    color = fruit.color,
                                    radius = fruit.radius,
                                    center = Offset(fruit.x, fruit.y)
                                )
                                drawCircle(
                                    color = fruit.innerColor,
                                    radius = fruit.radius * 0.72f,
                                    center = Offset(fruit.x, fruit.y)
                                )
                                drawCircle(
                                    color = Color.White.copy(alpha = 0.6f),
                                    radius = fruit.radius * 0.2f,
                                    center = Offset(fruit.x - 12f, fruit.y - 12f)
                                )
                            }
                        } else {
                            // Sliced Halves drifting apart
                            drawCircle(
                                color = fruit.innerColor,
                                radius = fruit.radius * 0.6f,
                                center = Offset(fruit.x - fruit.sliceOffset, fruit.y)
                            )
                            drawCircle(
                                color = fruit.innerColor,
                                radius = fruit.radius * 0.6f,
                                center = Offset(fruit.x + fruit.sliceOffset, fruit.y)
                            )
                        }
                    }

                    // Draw Blade Swipe Trail
                    if (trailPoints.size > 1) {
                        val path = Path().apply {
                            moveTo(trailPoints.first().offset.x, trailPoints.first().offset.y)
                            for (i in 1 until trailPoints.size) {
                                lineTo(trailPoints[i].offset.x, trailPoints[i].offset.y)
                            }
                        }
                        drawPath(
                            path = path,
                            brush = Brush.linearGradient(
                                listOf(Color(0xFF00E5FF), Color.White, WinzoGold)
                            ),
                            style = Stroke(width = 14f, cap = StrokeCap.Round)
                        )
                    }
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
                        text = "TIME'S UP!",
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Black,
                        color = WinzoGold
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Total Score: $score",
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
