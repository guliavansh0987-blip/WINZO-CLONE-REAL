package com.example.ui.dialogs

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseOutQuad
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.theme.WinzoBgDark
import com.example.ui.theme.WinzoCrimson
import com.example.ui.theme.WinzoEmerald
import com.example.ui.theme.WinzoGold
import kotlinx.coroutines.launch
import kotlin.random.Random

data class SpinReward(
    val label: String,
    val amount: Double,
    val isBonus: Boolean,
    val color: Color
)

@Composable
fun DailySpinWheelDialog(
    onPrizeClaimed: (amount: Double, isBonus: Boolean) -> Unit,
    onDismiss: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val rotationAngle = remember { Animatable(0f) }
    var isSpinning by remember { mutableStateOf(false) }
    var wonReward by remember { mutableStateOf<SpinReward?>(null) }

    val segments = remember {
        listOf(
            SpinReward("₹10 Cash", 10.0, false, Color(0xFF00E676)),
            SpinReward("₹25 Bonus", 25.0, true, Color(0xFF7C4DFF)),
            SpinReward("₹50 Cash", 50.0, false, Color(0xFFFFD600)),
            SpinReward("₹5 Bonus", 5.0, true, Color(0xFFFF1744)),
            SpinReward("₹100 Mega", 100.0, false, Color(0xFFFF9100)),
            SpinReward("₹15 Bonus", 15.0, true, Color(0xFF00E5FF)),
            SpinReward("₹20 Cash", 20.0, false, Color(0xFFE91E63)),
            SpinReward("₹30 Bonus", 30.0, true, Color(0xFF3D5AFE))
        )
    }

    fun startSpin() {
        if (isSpinning) return
        isSpinning = true
        wonReward = null

        val winningIndex = Random.nextInt(segments.size)
        val targetSegment = segments[winningIndex]

        // 360 / 8 = 45 degrees per segment
        val segmentAngle = 360f / segments.size
        // Top pointer is at 270 deg (or 0 deg top), adjust target rotation
        val targetAngle = (360f * 5) + (360f - (winningIndex * segmentAngle)) - (segmentAngle / 2f)

        coroutineScope.launch {
            rotationAngle.animateTo(
                targetValue = targetAngle,
                animationSpec = tween(durationMillis = 3500, easing = EaseOutQuad)
            )
            isSpinning = false
            wonReward = targetSegment
        }
    }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = WinzoBgDark,
            modifier = Modifier
                .fillMaxWidth()
                .border(2.dp, WinzoGold, RoundedCornerShape(24.dp))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "WINZO DAILY LUCKY WHEEL",
                    color = WinzoGold,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Black
                )
                Text(
                    text = "Spin daily for guaranteed cash & bonuses!",
                    color = Color.LightGray,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 2.dp)
                )

                Spacer(modifier = Modifier.height(18.dp))

                // Wheel Canvas
                Box(
                    modifier = Modifier.size(260.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val cx = size.width / 2f
                        val cy = size.height / 2f
                        val radius = size.width * 0.46f

                        // Outer Gold Rim
                        drawCircle(
                            color = WinzoGold,
                            radius = radius + 6f,
                            center = Offset(cx, cy)
                        )

                        // Rotating Wheel Segments
                        rotate(rotationAngle.value, pivot = Offset(cx, cy)) {
                            val sweep = 360f / segments.size
                            segments.forEachIndexed { i, seg ->
                                drawArc(
                                    color = seg.color,
                                    startAngle = i * sweep,
                                    sweepAngle = sweep,
                                    useCenter = true,
                                    topLeft = Offset(cx - radius, cy - radius),
                                    size = Size(radius * 2, radius * 2)
                                )
                            }
                        }

                        // Center Hub
                        drawCircle(
                            color = Color(0xFF140D2E),
                            radius = 32f,
                            center = Offset(cx, cy)
                        )
                        drawCircle(
                            color = WinzoGold,
                            radius = 16f,
                            center = Offset(cx, cy)
                        )

                        // Top Pointer Triangle Pin
                        val pointerPath = Path().apply {
                            moveTo(cx, cy - radius - 14f)
                            lineTo(cx - 14f, cy - radius + 12f)
                            lineTo(cx + 14f, cy - radius + 12f)
                            close()
                        }
                        drawPath(pointerPath, color = Color.White)
                        drawPath(pointerPath, color = WinzoCrimson)
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                if (wonReward != null) {
                    Surface(
                        color = WinzoEmerald.copy(alpha = 0.25f),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(10.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "🎉 YOU WON ${wonReward!!.label}! 🎉",
                                color = WinzoGold,
                                fontWeight = FontWeight.Black,
                                fontSize = 16.sp
                            )
                            Text(
                                text = "Credited directly into your WinZO wallet.",
                                color = Color.White,
                                fontSize = 12.sp
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(14.dp))
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = onDismiss,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C2450)),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("CLOSE", color = Color.LightGray)
                    }

                    Button(
                        onClick = {
                            if (wonReward != null) {
                                onPrizeClaimed(wonReward!!.amount, wonReward!!.isBonus)
                                onDismiss()
                            } else {
                                startSpin()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = WinzoGold),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(1.5f),
                        enabled = !isSpinning
                    ) {
                        Text(
                            text = if (wonReward != null) "CLAIM REWARD" else if (isSpinning) "SPINNING..." else "SPIN NOW! 🎯",
                            fontWeight = FontWeight.Black,
                            color = Color.Black
                        )
                    }
                }
            }
        }
    }
}
