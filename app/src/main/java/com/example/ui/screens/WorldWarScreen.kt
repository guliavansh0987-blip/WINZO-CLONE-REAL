package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
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
import com.example.data.model.WorldWarRoom
import com.example.ui.theme.WinzoBgDark
import com.example.ui.theme.WinzoCrimson
import com.example.ui.theme.WinzoEmerald
import com.example.ui.theme.WinzoGold
import com.example.ui.theme.WinzoSurfaceCard

@Composable
fun WorldWarScreen(
    rooms: List<WorldWarRoom>,
    onCardPickRequested: (room: WorldWarRoom) -> Unit,
    onEnterGameDirectly: (room: WorldWarRoom) -> Unit,
    onOpenScoreboard: (room: WorldWarRoom) -> Unit
) {
    var selectedFilterBoot by remember { mutableDoubleStateOf(0.0) } // 0.0 means ALL

    val filteredRooms = if (selectedFilterBoot == 0.0) {
        rooms
    } else {
        rooms.filter { it.bootFee == selectedFilterBoot }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(WinzoBgDark)
            .padding(horizontal = 14.dp),
        contentPadding = PaddingValues(bottom = 85.dp)
    ) {
        // Top Header
        item {
            Spacer(modifier = Modifier.height(12.dp))
            Surface(
                color = Color(0xFF221742),
                shape = RoundedCornerShape(18.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.ElectricBolt,
                            contentDescription = "World War",
                            tint = WinzoGold,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "WINZO WORLD WAR",
                            color = WinzoGold,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Black
                        )
                    }
                    Text(
                        text = "Pick a mystery card to enter a team. Score points in the featured game to lead your team to victory and share the massive pot!",
                        color = Color.LightGray,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        }

        // Filter chips
        item {
            Spacer(modifier = Modifier.height(14.dp))
            val filterOptions = listOf(
                Pair("ALL WARS", 0.0),
                Pair("₹5 BOOT", 5.0),
                Pair("₹10 BOOT", 10.0),
                Pair("₹25 BOOT", 25.0),
                Pair("₹50 BOOT", 50.0)
            )

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(filterOptions) { (label, boot) ->
                    val isSelected = selectedFilterBoot == boot
                    Surface(
                        color = if (isSelected) WinzoCrimson else Color(0xFF211A42),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.clickable { selectedFilterBoot = boot }
                    ) {
                        Text(
                            text = label,
                            color = if (isSelected) Color.White else Color.LightGray,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 7.dp)
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(14.dp))
        }

        // Room Cards
        items(filteredRooms) { room ->
            WorldWarRoomCard(
                room = room,
                onJoinOrPlay = {
                    if (room.userTeam != null) {
                        onEnterGameDirectly(room)
                    } else {
                        onCardPickRequested(room)
                    }
                },
                onOpenScoreboard = { onOpenScoreboard(room) }
            )
            Spacer(modifier = Modifier.height(14.dp))
        }
    }
}

@Composable
private fun WorldWarRoomCard(
    room: WorldWarRoom,
    onJoinOrPlay: () -> Unit,
    onOpenScoreboard: (room: WorldWarRoom) -> Unit
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = WinzoSurfaceCard),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF3B2F6E)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Header Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(room.gameType.iconEmoji, fontSize = 22.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = room.title,
                            color = Color.White,
                            fontWeight = FontWeight.Black,
                            fontSize = 15.sp
                        )
                        Text(
                            text = "${room.gameType.title} • Boot: ₹${room.bootFee.toInt()}",
                            color = WinzoGold,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Surface(
                    color = WinzoCrimson.copy(alpha = 0.25f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "⏱️ ${room.timeLeftSeconds / 60}m ${room.timeLeftSeconds % 60}s",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Total Prize Pool Banner
            Surface(
                color = Color(0xFF1B1438),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "TOTAL PRIZE POOL",
                        color = Color.LightGray,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "₹${room.totalPot.toInt()}",
                        color = WinzoEmerald,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Black
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Team A vs Team B Breakdown
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Team A
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.Start
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(room.teamA.avatarEmoji, fontSize = 20.sp)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = room.teamA.teamName,
                            color = Color(room.teamA.colorHex),
                            fontWeight = FontWeight.Black,
                            fontSize = 13.sp
                        )
                    }
                    Text(
                        text = "${room.teamA.totalScore} Pts (${room.teamA.memberCount} players)",
                        color = Color.LightGray,
                        fontSize = 11.sp
                    )
                }

                // VS Badge
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .background(Color(0xFF33275B), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text("VS", color = WinzoGold, fontSize = 11.sp, fontWeight = FontWeight.Black)
                }

                // Team B
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.End
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = room.teamB.teamName,
                            color = Color(room.teamB.colorHex),
                            fontWeight = FontWeight.Black,
                            fontSize = 13.sp
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(room.teamB.avatarEmoji, fontSize = 20.sp)
                    }
                    Text(
                        text = "${room.teamB.totalScore} Pts (${room.teamB.memberCount} players)",
                        color = Color.LightGray,
                        fontSize = 11.sp
                    )
                }
            }

            // Reference 6: Tag Chips (Teams Count, Rules, Entry Type)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Surface(
                    color = Color(0xFF261D4C),
                    shape = RoundedCornerShape(6.dp),
                    border = androidx.compose.foundation.BorderStroke(0.8.dp, Color(0xFF7C4DFF))
                ) {
                    Text(
                        text = room.teamsCountLabel,
                        color = Color(0xFFB39DDB),
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
                Surface(
                    color = Color(0xFF261D4C),
                    shape = RoundedCornerShape(6.dp),
                    border = androidx.compose.foundation.BorderStroke(0.8.dp, Color(0xFF00E5FF))
                ) {
                    Text(
                        text = room.ruleLabel,
                        color = Color(0xFF80D8FF),
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
                Surface(
                    color = Color(0xFF261D4C),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = room.entryTypeLabel,
                        color = Color.LightGray,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Action Buttons Row (Reference 6: Team Details & Play / Card Pick)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Team Details Button (Opens Live Scoreboard / Towers)
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFF281E52),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF7C4DFF)),
                    modifier = Modifier
                        .weight(1f)
                        .height(44.dp)
                        .clickable { onOpenScoreboard(room) }
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = "TEAM DETAILS",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }
                }

                // Main Play / Join Button
                if (room.userTeam != null) {
                    Button(
                        onClick = onJoinOrPlay,
                        colors = ButtonDefaults.buttonColors(containerColor = WinzoEmerald),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .weight(1.2f)
                            .height(44.dp)
                    ) {
                        Text(
                            text = "PLAY NOW ⚔️",
                            fontWeight = FontWeight.Black,
                            color = Color.Black,
                            fontSize = 13.sp
                        )
                    }
                } else {
                    Button(
                        onClick = onJoinOrPlay,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00B0FF)),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .weight(1.2f)
                            .height(44.dp)
                    ) {
                        Text(
                            text = "₹${room.bootFee.toInt()} PLAY",
                            fontWeight = FontWeight.Black,
                            color = Color.White,
                            fontSize = 13.sp
                        )
                    }
                }
            }
        }
    }
}
