package com.example.ui.screens

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.FantasyMatch
import com.example.data.model.FantasyPlayer
import com.example.data.model.FantasySquad
import com.example.data.model.PlayerRole

/**
 * 1:1 Authentic Captain & Vice-Captain selection modal matching Reference 5 (Screenshot_20261106_194052_b380c2c2.jpg):
 * - Purple header banner: "Mon, 9 Sep"
 * - Subheader: "Choose your Captain and Vice Captain"
 * - (C) gets 2x points, (VC) gets 1.5x points pill badges
 * - Category sections: Wicket-Keeper, Batsmen, All-Rounders, Bowlers
 * - Interactive C / VC circle toggles that illuminate with solid violet (#7C4DFF)
 * - Bottom Bar: "TEAM PREVIEW" and "SAVE TEAM"
 */
@Composable
fun CaptainViceCaptainSelectionDialog(
    match: FantasyMatch,
    selectedPlayers: List<FantasyPlayer>,
    initialCaptainId: String?,
    initialViceCaptainId: String?,
    onSaveTeam: (squad: FantasySquad) -> Unit,
    onDismiss: () -> Unit
) {
    var captainId by remember { mutableStateOf(initialCaptainId ?: selectedPlayers.firstOrNull()?.id) }
    var viceCaptainId by remember { mutableStateOf(initialViceCaptainId ?: selectedPlayers.getOrNull(1)?.id) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = Color(0xFFF7F7F7),
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxSize(0.95f)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Header (Reference 5: Bright Purple Header with Close)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF7C13E7))
                        .padding(horizontal = 14.dp, vertical = 14.dp)
                ) {
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .size(28.dp)
                    ) {
                        Icon(
                            Icons.Filled.Close,
                            contentDescription = "Close",
                            tint = Color.White
                        )
                    }

                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = match.title,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "Choose your Captain and Vice Captain",
                            color = Color.White.copy(alpha = 0.9f),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Multiplier Rule Pills (Reference 5)
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = Color.White.copy(alpha = 0.2f)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(16.dp)
                                            .background(Color.White, CircleShape),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text("C", color = Color(0xFF7C13E7), fontSize = 10.sp, fontWeight = FontWeight.Black)
                                    }
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("gets 2x points", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                }
                            }

                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = Color.White.copy(alpha = 0.2f)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(16.dp)
                                            .background(Color.White, CircleShape),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text("VC", color = Color(0xFF7C13E7), fontSize = 8.sp, fontWeight = FontWeight.Black)
                                    }
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("gets 1.5x points", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }

                // Players List Grouped by Role (Reference 5)
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .background(Color(0xFFF0F0F3))
                ) {
                    val groupedRoles = listOf(
                        PlayerRole.WICKET_KEEPER to "Wicket-Keeper",
                        PlayerRole.BATSMAN to "Batsmen",
                        PlayerRole.ALL_ROUNDER to "All-Rounders",
                        PlayerRole.BOWLER to "Bowlers"
                    )

                    groupedRoles.forEach { (role, title) ->
                        val players = selectedPlayers.filter { it.role == role }
                        if (players.isNotEmpty()) {
                            item {
                                Surface(
                                    color = Color(0xFFE2E2E6),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = title,
                                        color = Color(0xFF212121),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 11.sp,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.padding(vertical = 5.dp)
                                    )
                                }
                            }

                            items(players) { player ->
                                val isCaptain = captainId == player.id
                                val isViceCaptain = viceCaptainId == player.id

                                Surface(
                                    color = Color.White,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 10.dp, vertical = 3.dp),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 12.dp, vertical = 8.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        // Player Avatar
                                        Box(
                                            modifier = Modifier
                                                .size(36.dp)
                                                .background(Color(0xFFEEEEEE), CircleShape),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(player.avatarEmoji, fontSize = 20.sp)
                                        }

                                        Spacer(modifier = Modifier.width(10.dp))

                                        // Player Name & Role
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                text = player.name,
                                                color = Color(0xFF1E1E1E),
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 13.sp
                                            )
                                            Text(
                                                text = "${player.teamName} - ${role.shortName}",
                                                color = Color.Gray,
                                                fontSize = 10.sp
                                            )
                                        }

                                        Text(
                                            text = "${player.points} pts",
                                            color = Color.DarkGray,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Medium
                                        )

                                        Spacer(modifier = Modifier.width(14.dp))

                                        // C Button Toggle (Reference 5: Solid purple circle when selected)
                                        Box(
                                            modifier = Modifier
                                                .size(32.dp)
                                                .background(
                                                    if (isCaptain) Color(0xFF7C13E7) else Color.White,
                                                    CircleShape
                                                )
                                                .border(
                                                    1.5.dp,
                                                    if (isCaptain) Color(0xFF7C13E7) else Color(0xFFBDBDBD),
                                                    CircleShape
                                                )
                                                .clickable {
                                                    captainId = player.id
                                                    if (viceCaptainId == player.id) viceCaptainId = null
                                                },
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = "C",
                                                color = if (isCaptain) Color.White else Color(0xFF757575),
                                                fontWeight = FontWeight.Black,
                                                fontSize = 12.sp
                                            )
                                        }

                                        Spacer(modifier = Modifier.width(10.dp))

                                        // VC Button Toggle
                                        Box(
                                            modifier = Modifier
                                                .size(32.dp)
                                                .background(
                                                    if (isViceCaptain) Color(0xFF7C13E7) else Color.White,
                                                    CircleShape
                                                )
                                                .border(
                                                    1.5.dp,
                                                    if (isViceCaptain) Color(0xFF7C13E7) else Color(0xFFBDBDBD),
                                                    CircleShape
                                                )
                                                .clickable {
                                                    viceCaptainId = player.id
                                                    if (captainId == player.id) captainId = null
                                                },
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = "VC",
                                                color = if (isViceCaptain) Color.White else Color(0xFF757575),
                                                fontWeight = FontWeight.Black,
                                                fontSize = 10.sp
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // Sticky Bottom Bar: TEAM PREVIEW + SAVE TEAM (Reference 5)
                Surface(
                    color = Color.White,
                    shadowElevation = 8.dp,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 12.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Button(
                            onClick = onDismiss,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.White,
                                contentColor = Color(0xFF7C13E7)
                            ),
                            border = androidx.compose.foundation.BorderStroke(1.5.dp, Color(0xFF7C13E7)),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(46.dp)
                        ) {
                            Text(
                                text = "TEAM PREVIEW",
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                        }

                        Button(
                            onClick = {
                                if (captainId != null && viceCaptainId != null) {
                                    onSaveTeam(
                                        FantasySquad(
                                            matchId = match.id,
                                            selectedPlayerIds = selectedPlayers.map { it.id }.toSet(),
                                            captainId = captainId,
                                            viceCaptainId = viceCaptainId
                                        )
                                    )
                                }
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF7C13E7),
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .weight(1.2f)
                                .height(46.dp),
                            enabled = captainId != null && viceCaptainId != null
                        ) {
                            Text(
                                text = "SAVE TEAM",
                                fontWeight = FontWeight.Black,
                                fontSize = 13.sp
                            )
                        }
                    }
                }
            }
        }
    }
}
