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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SportsCricket
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.localization.AppLanguage
import com.example.data.localization.WinzoStrings
import com.example.data.model.FantasyMatch
import com.example.data.model.FantasyPlayer
import com.example.data.model.FantasySquad
import com.example.data.model.PlayerRole
import com.example.ui.theme.WinzoBgDark
import com.example.ui.theme.WinzoCrimson
import com.example.ui.theme.WinzoEmerald
import com.example.ui.theme.WinzoGold
import com.example.ui.theme.WinzoSurfaceCard

@Composable
fun FantasyScreen(
    matches: List<FantasyMatch>,
    language: AppLanguage,
    onOpenSquadBuilder: (match: FantasyMatch) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(WinzoBgDark)
            .padding(horizontal = 14.dp),
        contentPadding = PaddingValues(bottom = 85.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(12.dp))
            Surface(
                color = Color(0xFF221744),
                shape = RoundedCornerShape(18.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.SportsCricket,
                            contentDescription = "Fantasy Cricket",
                            tint = WinzoGold,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = WinzoStrings.get("fantasy_title", language),
                            color = WinzoGold,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Black
                        )
                    }
                    Text(
                        text = WinzoStrings.get("fantasy_desc", language),
                        color = Color.LightGray,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(top = 4.dp),
                        lineHeight = 16.sp
                    )
                }
            }
            Spacer(modifier = Modifier.height(14.dp))
        }

        items(matches) { match ->
            FantasyMatchCard(
                match = match,
                onJoinOrBuild = { onOpenSquadBuilder(match) }
            )
            Spacer(modifier = Modifier.height(14.dp))
        }
    }
}

@Composable
private fun FantasyMatchCard(
    match: FantasyMatch,
    onJoinOrBuild: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = WinzoSurfaceCard),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF382C68)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Header Row: Series & Time
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = match.seriesName,
                    color = WinzoGold,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
                Surface(
                    color = WinzoCrimson.copy(alpha = 0.25f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "⏱️ ${match.startTime}",
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Teams Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .background(Color(0xFF342862), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("🏏", fontSize = 18.sp)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = match.teamAShort,
                        color = Color.White,
                        fontWeight = FontWeight.Black,
                        fontSize = 17.sp
                    )
                }

                Surface(
                    color = Color(0xFF1B1439),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "VS",
                        color = WinzoGold,
                        fontWeight = FontWeight.Black,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = match.teamBShort,
                        color = Color.White,
                        fontWeight = FontWeight.Black,
                        fontSize = 17.sp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .background(Color(0xFF342862), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("⚡", fontSize = 18.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Prize Pool Box
            Surface(
                color = Color(0xFF1B1438),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("MEGA PRIZE POOL", color = Color.Gray, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        Text(
                            "₹${match.megaPrizePool.toInt()}",
                            color = WinzoEmerald,
                            fontWeight = FontWeight.Black,
                            fontSize = 20.sp
                        )
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text("1ST PRIZE", color = Color.Gray, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        Text(
                            "🏆 ₹${match.firstPrize.toInt()}",
                            color = WinzoGold,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Progress Bar
            val progress = (match.filledSpots.toFloat() / match.totalSpots.toFloat()).coerceIn(0f, 1f)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("${match.filledSpots} joined", color = Color.LightGray, fontSize = 11.sp)
                Text("${match.totalSpots - match.filledSpots} spots left", color = WinzoGold, fontSize = 11.sp)
            }
            Spacer(modifier = Modifier.height(4.dp))
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = WinzoGold,
                trackColor = Color(0xFF1C153A)
            )

            Spacer(modifier = Modifier.height(14.dp))

            Button(
                onClick = onJoinOrBuild,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (match.isJoined) WinzoEmerald else WinzoCrimson
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(46.dp)
            ) {
                Text(
                    text = if (match.isJoined) "TEAM REGISTERED (VIEW) ✓" else "CREATE SQUAD (₹${match.entryFee.toInt()}) 🏏",
                    fontWeight = FontWeight.Black,
                    color = if (match.isJoined) Color.Black else Color.White,
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Composable
fun FantasySquadBuilderDialog(
    match: FantasyMatch,
    onSaveAndJoin: (squad: FantasySquad) -> Unit,
    onDismiss: () -> Unit
) {
    var selectedTab by remember { mutableIntStateOf(0) } // 0=WK, 1=BAT, 2=AR, 3=BOWL
    val roles = listOf(PlayerRole.WICKET_KEEPER, PlayerRole.BATSMAN, PlayerRole.ALL_ROUNDER, PlayerRole.BOWLER)

    val selectedPlayerIds = remember { mutableStateListOf<String>() }
    var captainId by remember { mutableStateOf<String?>(null) }
    var viceCaptainId by remember { mutableStateOf<String?>(null) }

    val currentCreditsUsed = match.players.filter { selectedPlayerIds.contains(it.id) }.sumOf { it.credits }
    val creditsRemaining = 100.0 - currentCreditsUsed

    val currentRole = roles[selectedTab]
    val playersInRole = match.players.filter { it.role == currentRole }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(22.dp),
            color = WinzoBgDark,
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 24.dp)
                .border(2.dp, WinzoGold, RoundedCornerShape(22.dp))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "${match.teamAShort} vs ${match.teamBShort}",
                            color = Color.White,
                            fontWeight = FontWeight.Black,
                            fontSize = 16.sp
                        )
                        Text(
                            text = "Pick 11 Players (Max 7 from one team)",
                            color = Color.LightGray,
                            fontSize = 11.sp
                        )
                    }

                    Surface(
                        color = Color(0xFF221844),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(
                            text = "Credits: ${String.format("%.1f", creditsRemaining)}",
                            color = WinzoGold,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Selection Progress
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Selected: ${selectedPlayerIds.size} / 11",
                        color = if (selectedPlayerIds.size == 11) WinzoEmerald else WinzoGold,
                        fontWeight = FontWeight.Black,
                        fontSize = 14.sp
                    )
                    Text(
                        text = if (captainId != null && viceCaptainId != null) "C & VC Set ✓" else "Choose C & VC",
                        color = Color.LightGray,
                        fontSize = 12.sp
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Role Tabs
                TabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = Color(0xFF1E163E),
                    contentColor = Color.White,
                    indicator = { tabPositions ->
                        TabRowDefaults.SecondaryIndicator(
                            Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                            color = WinzoGold
                        )
                    }
                ) {
                    roles.forEachIndexed { index, role ->
                        val countInRole = match.players.count { selectedPlayerIds.contains(it.id) && it.role == role }
                        Tab(
                            selected = selectedTab == index,
                            onClick = { selectedTab = index },
                            text = {
                                Text(
                                    text = "${role.shortName} ($countInRole)",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp
                                )
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Players List in Current Role
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(playersInRole) { player ->
                        val isSelected = selectedPlayerIds.contains(player.id)
                        val isCaptain = captainId == player.id
                        val isViceCaptain = viceCaptainId == player.id

                        Surface(
                            color = if (isSelected) Color(0xFF2E2255) else Color(0xFF1B1439),
                            shape = RoundedCornerShape(12.dp),
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                if (isSelected) WinzoGold else Color(0xFF2C2250)
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    if (isSelected) {
                                        selectedPlayerIds.remove(player.id)
                                        if (isCaptain) captainId = null
                                        if (isViceCaptain) viceCaptainId = null
                                    } else {
                                        if (selectedPlayerIds.size < 11 && (creditsRemaining >= player.credits)) {
                                            selectedPlayerIds.add(player.id)
                                            if (captainId == null) captainId = player.id
                                            else if (viceCaptainId == null && captainId != player.id) viceCaptainId = player.id
                                        }
                                    }
                                }
                        ) {
                            Row(
                                modifier = Modifier.padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(player.avatarEmoji, fontSize = 22.sp)
                                Spacer(modifier = Modifier.width(8.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(player.name, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Surface(
                                            color = Color(0xFF38296A),
                                            shape = RoundedCornerShape(4.dp)
                                        ) {
                                            Text(
                                                player.teamName,
                                                color = WinzoGold,
                                                fontSize = 9.sp,
                                                fontWeight = FontWeight.Black,
                                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                            )
                                        }
                                    }
                                    Text(
                                        "${player.points} Pts • Sel ${player.selectionRate}",
                                        color = Color.Gray,
                                        fontSize = 10.sp
                                    )
                                }

                                Text(
                                    "${player.credits} Cr",
                                    color = WinzoGold,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )

                                Spacer(modifier = Modifier.width(8.dp))

                                // C and VC Badges if selected
                                if (isSelected) {
                                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                        Surface(
                                            color = if (isCaptain) WinzoGold else Color(0xFF1E173C),
                                            shape = CircleShape,
                                            modifier = Modifier
                                                .size(24.dp)
                                                .clickable {
                                                    captainId = player.id
                                                    if (viceCaptainId == player.id) viceCaptainId = null
                                                },
                                            border = androidx.compose.foundation.BorderStroke(1.dp, WinzoGold)
                                        ) {
                                            Box(contentAlignment = Alignment.Center) {
                                                Text(
                                                    "C",
                                                    color = if (isCaptain) Color.Black else Color.White,
                                                    fontSize = 11.sp,
                                                    fontWeight = FontWeight.Black
                                                )
                                            }
                                        }

                                        Surface(
                                            color = if (isViceCaptain) Color(0xFF00E5FF) else Color(0xFF1E173C),
                                            shape = CircleShape,
                                            modifier = Modifier
                                                .size(24.dp)
                                                .clickable {
                                                    viceCaptainId = player.id
                                                    if (captainId == player.id) captainId = null
                                                },
                                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF00E5FF))
                                        ) {
                                            Box(contentAlignment = Alignment.Center) {
                                                Text(
                                                    "VC",
                                                    color = if (isViceCaptain) Color.Black else Color.White,
                                                    fontSize = 9.sp,
                                                    fontWeight = FontWeight.Black
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Footer Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = onDismiss,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2A214C)),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("CANCEL", color = Color.LightGray)
                    }

                    var showCaptainModal by remember { mutableStateOf(false) }

                    Button(
                        onClick = {
                            if (selectedPlayerIds.size == 11) {
                                showCaptainModal = true
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7C13E7)),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(1.8f),
                        enabled = selectedPlayerIds.size == 11
                    ) {
                        Text(
                            text = if (selectedPlayerIds.size < 11) "SELECT 11 (${selectedPlayerIds.size}/11)" else "CHOOSE C & VC 👉",
                            fontWeight = FontWeight.Black,
                            color = Color.White,
                            fontSize = 13.sp
                        )
                    }

                    if (showCaptainModal) {
                        val selectedList = match.players.filter { selectedPlayerIds.contains(it.id) }
                        CaptainViceCaptainSelectionDialog(
                            match = match,
                            selectedPlayers = selectedList,
                            initialCaptainId = captainId,
                            initialViceCaptainId = viceCaptainId,
                            onSaveTeam = { squad ->
                                showCaptainModal = false
                                onSaveAndJoin(squad)
                            },
                            onDismiss = { showCaptainModal = false }
                        )
                    }
                }
            }
        }
    }
}
