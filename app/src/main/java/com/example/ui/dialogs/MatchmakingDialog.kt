package com.example.ui.dialogs

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.MatchmakingState
import com.example.ui.theme.WinzoBgDark
import com.example.ui.theme.WinzoCrimson
import com.example.ui.theme.WinzoEmerald
import com.example.ui.theme.WinzoGold

@Composable
fun MatchmakingDialog(
    state: MatchmakingState,
    onCancel: () -> Unit
) {
    if (!state.isSearching && !state.isMatchFound) return

    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "spin"
    )

    Dialog(onDismissRequest = {}) {
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
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header
                Text(
                    text = state.targetGame.title.uppercase(),
                    color = WinzoGold,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Black
                )
                Text(
                    text = "1v1 BATTLE  •  BOOT: ₹${state.bootAmount.toInt()}",
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Versus Avatars
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // You
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(
                            modifier = Modifier
                                .size(72.dp)
                                .background(Color(0xFF2E1F55), CircleShape)
                                .border(2.dp, WinzoGold, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("👑", fontSize = 34.sp)
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text("You", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Text("₹${state.bootAmount.toInt()} Boot", color = WinzoGold, fontSize = 12.sp)
                    }

                    // VS Emblem
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .background(WinzoCrimson, CircleShape)
                            .border(2.dp, Color.White, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("VS", color = Color.White, fontWeight = FontWeight.Black, fontSize = 18.sp)
                    }

                    // Opponent
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(
                            modifier = Modifier
                                .size(72.dp)
                                .background(Color(0xFF2E1F55), CircleShape)
                                .border(2.dp, if (state.isMatchFound) WinzoEmerald else Color.Gray, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            if (state.isMatchFound) {
                                Text(state.opponentAvatar ?: "🔥", fontSize = 34.sp)
                            } else {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(36.dp),
                                    color = WinzoGold,
                                    strokeWidth = 3.dp
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = if (state.isMatchFound) (state.matchedOpponent ?: "Rival") else "Searching...",
                            color = if (state.isMatchFound) WinzoEmerald else Color.LightGray,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                        Text("₹${state.bootAmount.toInt()} Boot", color = WinzoGold, fontSize = 12.sp)
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Status message
                Text(
                    text = if (state.isMatchFound) "MATCH FOUND! STARTING BATTLE..." else "Searching for live rival...",
                    color = if (state.isMatchFound) WinzoEmerald else WinzoGold,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "Winner takes: ₹${(state.bootAmount * 1.9).toInt()} Winnings Cash",
                    color = Color.LightGray,
                    fontSize = 13.sp,
                    modifier = Modifier.padding(top = 4.dp)
                )

                Spacer(modifier = Modifier.height(20.dp))

                if (!state.isMatchFound) {
                    Button(
                        onClick = onCancel,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF332A55)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("CANCEL & REFUND", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
