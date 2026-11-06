package com.example.ui.dialogs

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.WorldWarRoom
import com.example.ui.theme.WinzoBgDark
import com.example.ui.theme.WinzoCrimson
import com.example.ui.theme.WinzoEmerald
import com.example.ui.theme.WinzoGold

@Composable
fun WorldWarCardPickDialog(
    room: WorldWarRoom,
    onTeamConfirmed: (pickedTeam: String) -> Unit,
    onDismiss: () -> Unit
) {
    var revealedTeam by remember { mutableStateOf<String?>(null) }
    var selectedCardIndex by remember { mutableStateOf<Int?>(null) }

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
                // Header
                Text(
                    text = "WORLD WAR BATTLE CARD",
                    color = WinzoGold,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Black
                )
                Text(
                    text = room.title,
                    color = Color.White,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Boot Fee: ₹${room.bootFee.toInt()}  |  Prize Pool: ₹${room.totalPot.toInt()}",
                    color = WinzoEmerald,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(top = 4.dp)
                )

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = if (revealedTeam == null) "TAP A CARD TO PICK YOUR TEAM!" else "YOUR ASSIGNED TEAM REVEALED!",
                    color = if (revealedTeam == null) Color.LightGray else WinzoGold,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(16.dp))

                // 2 Mystery Cards
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    // Card 1
                    MysteryCard(
                        cardIndex = 1,
                        isSelected = selectedCardIndex == 1,
                        revealedTeam = if (selectedCardIndex == 1) revealedTeam else null,
                        teamInfo = if (revealedTeam == room.teamA.teamName) room.teamA else room.teamB,
                        onClick = {
                            if (revealedTeam == null) {
                                selectedCardIndex = 1
                                revealedTeam = if (listOf(true, false).random()) room.teamA.teamName else room.teamB.teamName
                            }
                        }
                    )

                    // Card 2
                    MysteryCard(
                        cardIndex = 2,
                        isSelected = selectedCardIndex == 2,
                        revealedTeam = if (selectedCardIndex == 2) revealedTeam else null,
                        teamInfo = if (revealedTeam == room.teamA.teamName) room.teamA else room.teamB,
                        onClick = {
                            if (revealedTeam == null) {
                                selectedCardIndex = 2
                                revealedTeam = if (listOf(true, false).random()) room.teamA.teamName else room.teamB.teamName
                            }
                        }
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                if (revealedTeam != null) {
                    val activeTeam = if (revealedTeam == room.teamA.teamName) room.teamA else room.teamB
                    Surface(
                        color = Color(activeTeam.colorHex).copy(alpha = 0.25f),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Fighting for ${activeTeam.teamName} ${activeTeam.avatarEmoji}",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                            Text(
                                text = "Score maximum in ${room.gameType.title} to lift team to victory!",
                                color = Color.LightGray,
                                fontSize = 12.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = { onTeamConfirmed(revealedTeam!!) },
                        colors = ButtonDefaults.buttonColors(containerColor = WinzoEmerald),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                    ) {
                        Text(
                            text = "START BATTLE NOW ⚔️",
                            fontWeight = FontWeight.Black,
                            fontSize = 16.sp,
                            color = Color.Black
                        )
                    }
                } else {
                    Button(
                        onClick = onDismiss,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF322A54)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("CANCEL", color = Color.LightGray)
                    }
                }
            }
        }
    }
}

@Composable
private fun MysteryCard(
    cardIndex: Int,
    isSelected: Boolean,
    revealedTeam: String?,
    teamInfo: com.example.data.model.TeamInfo,
    onClick: () -> Unit
) {
    val cardBackground = if (revealedTeam != null) {
        Brush.verticalGradient(
            listOf(Color(teamInfo.colorHex), Color(0xFF1E173D))
        )
    } else {
        Brush.verticalGradient(
            listOf(Color(0xFF3A2E6B), Color(0xFF1C1635))
        )
    }

    Box(
        modifier = Modifier
            .size(width = 135.dp, height = 185.dp)
            .background(cardBackground, RoundedCornerShape(16.dp))
            .border(
                2.dp,
                if (isSelected) WinzoGold else Color(0x66FFD700),
                RoundedCornerShape(16.dp)
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        if (revealedTeam == null) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("🃏", fontSize = 42.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "CARD $cardIndex",
                    color = WinzoGold,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 14.sp
                )
                Text(
                    text = "TAP TO FLIP",
                    color = Color.LightGray,
                    fontSize = 10.sp
                )
            }
        } else {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(teamInfo.avatarEmoji, fontSize = 44.sp)
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = teamInfo.teamName,
                    color = Color.White,
                    fontWeight = FontWeight.Black,
                    fontSize = 15.sp
                )
                Text(
                    text = "Cap: ${teamInfo.captainName}",
                    color = WinzoGold,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}
