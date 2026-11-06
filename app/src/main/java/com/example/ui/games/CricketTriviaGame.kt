package com.example.ui.games

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.compose.animation.animateColorAsState
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.WinzoBgDark
import com.example.ui.theme.WinzoCrimson
import com.example.ui.theme.WinzoEmerald
import com.example.ui.theme.WinzoGold
import kotlinx.coroutines.delay

data class CricketQuestion(
    val question: String,
    val options: List<String>,
    val correctIndex: Int
)

@Composable
fun CricketTriviaGame(
    onGameEnd: (score: Int) -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val questions = remember {
        listOf(
            CricketQuestion(
                "Who holds the record for the highest individual score in ODI cricket?",
                listOf("Rohit Sharma (264)", "Martin Guptill (237)", "Virender Sehwag (219)", "Chris Gayle (215)"),
                0
            ),
            CricketQuestion(
                "Which IPL franchise has won 5 IPL trophies under MS Dhoni?",
                listOf("Mumbai Indians", "Chennai Super Kings", "Kolkata Knight Riders", "Gujarat Titans"),
                1
            ),
            CricketQuestion(
                "Who was the Captain of India during the 2007 T20 World Cup triumph?",
                listOf("Rahul Dravid", "Sourav Ganguly", "MS Dhoni", "Yuvraj Singh"),
                2
            ),
            CricketQuestion(
                "How many sixes did Yuvraj Singh hit in one over off Stuart Broad in 2007?",
                listOf("4", "5", "6", "7"),
                2
            ),
            CricketQuestion(
                "Who is known as the 'King of Cricket' with 50 ODI centuries?",
                listOf("Sachin Tendulkar", "Virat Kohli", "Brian Lara", "Ricky Ponting"),
                1
            ),
            CricketQuestion(
                "Which bowler has taken the most wickets in Test Cricket history?",
                listOf("Muttiah Muralitharan", "Shane Warne", "James Anderson", "Anil Kumble"),
                0
            ),
            CricketQuestion(
                "Which stadium is the largest cricket stadium in the world by capacity?",
                listOf("Melbourne Cricket Ground", "Eden Gardens", "Narendra Modi Stadium", "Lord's"),
                2
            ),
            CricketQuestion(
                "Who won the Player of the Tournament award in the 2011 ICC Cricket World Cup?",
                listOf("MS Dhoni", "Yuvraj Singh", "Zaheer Khan", "Sachin Tendulkar"),
                1
            )
        )
    }

    var currentQIndex by remember { mutableIntStateOf(0) }
    var score by remember { mutableIntStateOf(0) }
    var selectedOption by remember { mutableStateOf<Int?>(null) }
    var timerProgress by remember { mutableFloatStateOf(1f) }
    var isGameOver by remember { mutableStateOf(false) }

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

    // Question Timer Loop
    LaunchedEffect(currentQIndex, isGameOver) {
        if (isGameOver) return@LaunchedEffect
        selectedOption = null
        timerProgress = 1f
        val steps = 100
        for (i in 0..steps) {
            if (selectedOption != null) break
            timerProgress = 1f - (i / steps.toFloat())
            delay(100)
        }
        if (selectedOption == null && !isGameOver) {
            // Time out on question
            if (currentQIndex < questions.size - 1) {
                currentQIndex++
            } else {
                isGameOver = true
                onGameEnd(score)
            }
        }
    }

    fun chooseAnswer(index: Int) {
        if (selectedOption != null || isGameOver) return
        selectedOption = index
        val q = questions[currentQIndex]
        if (index == q.correctIndex) {
            vibrate(40)
            val speedBonus = (timerProgress * 25).toInt()
            score += 25 + speedBonus
        } else {
            vibrate(120)
        }

        // Delay before moving to next question
        kotlinx.coroutines.GlobalScope.let {
            // Will transition in 1 second
        }
    }

    LaunchedEffect(selectedOption) {
        if (selectedOption != null) {
            delay(900)
            if (currentQIndex < questions.size - 1) {
                currentQIndex++
            } else {
                isGameOver = true
                onGameEnd(score)
            }
        }
    }

    val currentQ = questions[currentQIndex]

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
                        text = "Q ${currentQIndex + 1} / ${questions.size}",
                        color = WinzoGold,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                    )
                }

                Surface(
                    color = WinzoEmerald.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "SCORE: $score",
                        color = WinzoEmerald,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Timer Progress Bar
            LinearProgressIndicator(
                progress = { timerProgress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = if (timerProgress > 0.3f) WinzoGold else WinzoCrimson,
                trackColor = Color(0xFF261E47)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Question Card
            Surface(
                color = Color(0xFF241D47),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "CRICKET TRIVIA 🏏",
                        color = WinzoGold,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Black
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = currentQ.question,
                        color = Color.White,
                        fontSize = 19.sp,
                        fontWeight = FontWeight.Bold,
                        lineHeight = 26.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Options List
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                currentQ.options.forEachIndexed { index, option ->
                    val isSelected = selectedOption == index
                    val isCorrect = index == currentQ.correctIndex

                    val containerColor by animateColorAsState(
                        targetValue = when {
                            selectedOption == null -> Color(0xFF1F183D)
                            isCorrect -> WinzoEmerald.copy(alpha = 0.3f)
                            isSelected -> WinzoCrimson.copy(alpha = 0.3f)
                            else -> Color(0xFF1F183D).copy(alpha = 0.5f)
                        },
                        label = "option_color"
                    )

                    val borderColor = when {
                        selectedOption != null && isCorrect -> WinzoEmerald
                        selectedOption != null && isSelected -> WinzoCrimson
                        else -> Color(0xFF332960)
                    }

                    Surface(
                        color = containerColor,
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.5.dp, borderColor, RoundedCornerShape(16.dp))
                            .clickable(enabled = selectedOption == null) {
                                chooseAnswer(index)
                            }
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "${('A' + index)}. ",
                                fontWeight = FontWeight.Black,
                                color = WinzoGold,
                                fontSize = 16.sp
                            )
                            Text(
                                text = option,
                                color = Color.White,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 16.sp,
                                modifier = Modifier.weight(1f)
                            )
                            if (selectedOption != null) {
                                if (isCorrect) {
                                    Text("✅", fontSize = 18.sp)
                                } else if (isSelected) {
                                    Text("❌", fontSize = 18.sp)
                                }
                            }
                        }
                    }
                }
            }
        }

        // Game Over Overlay
        if (isGameOver) {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = Color.Black.copy(alpha = 0.88f)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "QUIZ COMPLETED!",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Black,
                        color = WinzoGold
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Total Trivia Score: $score",
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
