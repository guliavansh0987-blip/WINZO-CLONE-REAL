package com.example.ui.dialogs

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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.GameMode
import com.example.ui.theme.WinzoBgDark
import com.example.ui.theme.WinzoCrimson
import com.example.ui.theme.WinzoEmerald
import com.example.ui.theme.WinzoGold

@Composable
fun GameResultDialog(
    gameTitle: String,
    mode: GameMode,
    isVictory: Boolean,
    prizeAmount: Double,
    userScore: Int,
    rivalName: String,
    onPlayAgain: () -> Unit,
    onBackToLobby: () -> Unit
) {
    Dialog(onDismissRequest = onBackToLobby) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = WinzoBgDark,
            modifier = Modifier
                .fillMaxWidth()
                .border(2.dp, if (isVictory) WinzoGold else WinzoCrimson, RoundedCornerShape(24.dp))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(22.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Trophy or Skull icon
                Box(
                    modifier = Modifier
                        .size(68.dp)
                        .background(
                            if (isVictory) WinzoGold.copy(alpha = 0.2f) else WinzoCrimson.copy(alpha = 0.2f),
                            CircleShape
                        )
                        .border(
                            2.dp,
                            if (isVictory) WinzoGold else WinzoCrimson,
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(if (isVictory) "🏆" else "💔", fontSize = 36.sp)
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = if (isVictory) "CONGRATULATIONS!" else "GOOD EFFORT!",
                    color = if (isVictory) WinzoGold else Color.LightGray,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Black
                )

                Text(
                    text = if (isVictory) "YOU WON ₹${String.format("%.2f", prizeAmount)}" else "You placed 2nd place",
                    color = if (isVictory) WinzoEmerald else Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.ExtraBold,
                    modifier = Modifier.padding(vertical = 4.dp)
                )

                if (isVictory && prizeAmount > 0) {
                    Text(
                        text = "Instant credit to Withdrawable Winnings Cash ✅",
                        color = Color.LightGray,
                        fontSize = 12.sp
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider(color = Color(0xFF332960), thickness = 1.dp)
                Spacer(modifier = Modifier.height(14.dp))

                // Breakdown
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Game", color = Color.Gray, fontSize = 13.sp)
                    Text(gameTitle, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }
                Row(
                    modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Your Score", color = Color.Gray, fontSize = 13.sp)
                    Text("$userScore Pts", color = WinzoGold, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }
                if (mode == GameMode.ONE_VS_ONE) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Opponent ($rivalName)", color = Color.Gray, fontSize = 13.sp)
                        Text(if (isVictory) "${(userScore * 0.82).toInt()} Pts" else "${(userScore * 1.15).toInt()} Pts", color = Color.White, fontSize = 13.sp)
                    }
                }

                Spacer(modifier = Modifier.height(22.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = onBackToLobby,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C2450)),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("LOBBY", color = Color.LightGray, fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = onPlayAgain,
                        colors = ButtonDefaults.buttonColors(containerColor = if (isVictory) WinzoGold else WinzoCrimson),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(1.3f)
                    ) {
                        Text(
                            text = "PLAY AGAIN ⚔️",
                            fontWeight = FontWeight.Black,
                            color = Color.Black
                        )
                    }
                }
            }
        }
    }
}
