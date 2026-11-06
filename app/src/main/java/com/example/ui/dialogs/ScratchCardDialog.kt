package com.example.ui.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.theme.WinzoBgDark
import com.example.ui.theme.WinzoEmerald
import com.example.ui.theme.WinzoGold

@Composable
fun ScratchCardDialog(
    onClaimed: (amount: Double) -> Unit,
    onDismiss: () -> Unit
) {
    val prizeAmount = remember { listOf(15.0, 20.0, 25.0, 35.0, 50.0).random() }
    var scratchProgress by remember { mutableFloatStateOf(0f) }
    var isRevealed by remember { mutableStateOf(false) }

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
                    .padding(22.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "GOLDEN SCRATCH CARD",
                    color = WinzoGold,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Black
                )
                Text(
                    text = "Scratch the card to reveal mystery bonus cash!",
                    color = Color.LightGray,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 2.dp)
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Scratch Card Area
                Box(
                    modifier = Modifier
                        .size(width = 240.dp, height = 160.dp)
                        .background(
                            Brush.verticalGradient(
                                listOf(Color(0xFF261D4C), Color(0xFF161033))
                            ),
                            RoundedCornerShape(16.dp)
                        )
                        .border(2.dp, Color(0xFFFFD54F), RoundedCornerShape(16.dp))
                        .pointerInput(Unit) {
                            detectDragGestures { change, _ ->
                                change.consume()
                                scratchProgress = (scratchProgress + 0.05f).coerceAtMost(1f)
                                if (scratchProgress > 0.45f) {
                                    isRevealed = true
                                }
                            }
                        }
                        .clickable {
                            // Tap to reveal helper
                            isRevealed = true
                            scratchProgress = 1f
                        },
                    contentAlignment = Alignment.Center
                ) {
                    // Underneath Prize
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("🎁", fontSize = 36.sp)
                        Text(
                            text = "₹${prizeAmount.toInt()}",
                            color = WinzoEmerald,
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Black
                        )
                        Text(
                            text = "Bonus Cash",
                            color = Color.White,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Golden Scratch Surface
                    if (!isRevealed) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(160.dp)
                                .background(
                                    Brush.linearGradient(
                                        listOf(
                                            Color(0xFFFFD54F),
                                            Color(0xFFFFA000),
                                            Color(0xFFFFD54F)
                                        )
                                    ),
                                    RoundedCornerShape(16.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("✨", fontSize = 28.sp)
                                Text(
                                    text = "SCRATCH HERE",
                                    color = Color.Black,
                                    fontWeight = FontWeight.Black,
                                    fontSize = 16.sp
                                )
                                Text(
                                    text = "Swipe fingers or tap",
                                    color = Color(0xFF422B00),
                                    fontSize = 11.sp
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

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
                            onClaimed(prizeAmount)
                            onDismiss()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = WinzoEmerald),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(1.5f),
                        enabled = isRevealed
                    ) {
                        Text(
                            text = if (isRevealed) "CLAIM ₹${prizeAmount.toInt()}" else "SCRATCH FIRST",
                            fontWeight = FontWeight.Black,
                            color = Color.Black
                        )
                    }
                }
            }
        }
    }
}
