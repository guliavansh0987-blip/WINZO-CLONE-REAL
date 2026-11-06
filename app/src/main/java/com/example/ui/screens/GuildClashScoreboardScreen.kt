package com.example.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.TeamInfo
import com.example.data.model.WorldWarRoom
import com.example.ui.theme.AheadGreen
import com.example.ui.theme.BehindRed
import com.example.ui.theme.TowerBlueDark
import com.example.ui.theme.TowerBlueMid
import com.example.ui.theme.TowerBlueStart
import com.example.ui.theme.TowerOrangeDark
import com.example.ui.theme.TowerOrangeMid
import com.example.ui.theme.TowerOrangeStart
import com.example.ui.theme.WinzoBgDark
import com.example.ui.theme.WinzoEmerald
import com.example.ui.theme.WinzoGold

@Composable
fun GuildClashScoreboardScreen(
    room: WorldWarRoom,
    onBack: () -> Unit,
    onPlayMore: () -> Unit
) {
    var showTeamViewModal by remember { mutableStateOf<TeamInfo?>(null) }

    // Animation drivers for rising towers and score counters
    val animatedTowerHeight = remember { Animatable(0f) }
    LaunchedEffect(room.id) {
        animatedTowerHeight.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 1400, easing = FastOutSlowInEasing)
        )
    }

    val teamAAvgScore by animateIntAsState(
        targetValue = (room.teamA.avgScore * animatedTowerHeight.value).toInt(),
        animationSpec = tween(1200),
        label = "teamAAvg"
    )
    val teamBAvgScore by animateIntAsState(
        targetValue = (room.teamB.avgScore * animatedTowerHeight.value).toInt(),
        animationSpec = tween(1200),
        label = "teamBAvg"
    )

    val teamATotalScore by animateIntAsState(
        targetValue = (room.teamA.totalScore * animatedTowerHeight.value).toInt(),
        animationSpec = tween(1400),
        label = "teamATotal"
    )
    val teamBTotalScore by animateIntAsState(
        targetValue = (room.teamB.totalScore * animatedTowerHeight.value).toInt(),
        animationSpec = tween(1400),
        label = "teamBTotal"
    )

    val isUserInTeamA = room.userTeam == null || room.userTeam == room.teamA.teamName
    val scoreDiff = room.teamA.avgScore - room.teamB.avgScore
    val isAhead = scoreDiff >= 0

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF030A1A))
    ) {
        // Dynamic Split Background & Center Light Strike
        Row(modifier = Modifier.fillMaxSize()) {
            // Left Side: Deep Blue
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .background(
                        Brush.horizontalGradient(
                            listOf(Color(0xFF021B3A), Color(0xFF02325E))
                        )
                    )
            )
            // Right Side: Deep Amber/Orange
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .background(
                        Brush.horizontalGradient(
                            listOf(Color(0xFF3E1F03), Color(0xFF5A2A03))
                        )
                    )
            )
        }

        // Center Separation Line & Energy Glow
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawLine(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0x00FFFFFF),
                        Color(0xFF80D8FF),
                        Color(0xFFFFFFFF),
                        Color(0xFFFFD180),
                        Color(0x00FFFFFF)
                    )
                ),
                start = Offset(size.width / 2f, 0f),
                end = Offset(size.width / 2f, size.height),
                strokeWidth = 2.5f
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 14.dp, vertical = 10.dp)
        ) {
            // Top Section (Reference 1 Top Badge: "WINNINGS ₹3.12")
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier
                        .size(38.dp)
                        .background(Color(0x44000000), CircleShape)
                ) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFF072B4F).copy(alpha = 0.85f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF00E5FF).copy(alpha = 0.6f))
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(horizontal = 24.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "WINNINGS",
                            color = Color(0xFF80D8FF),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = "₹${String.format("%.2f", room.totalPot)}",
                            color = WinzoGold,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Black
                        )
                    }
                }

                IconButton(
                    onClick = { /* Help rule */ },
                    modifier = Modifier
                        .size(38.dp)
                        .background(Color(0x44000000), CircleShape)
                ) {
                    Icon(
                        Icons.Filled.Info,
                        contentDescription = "Info",
                        tint = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Main Arena: Dual Split Towers
            BoxWithConstraints(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                val totalWidth = maxWidth
                val totalHeight = maxHeight

                Row(modifier = Modifier.fillMaxSize()) {
                    // LEFT BLUE TOWER: YOUR TEAM (e.g. KOHLI)
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .padding(end = 4.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Top Tower Structure (Blue Castle Tower)
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            contentAlignment = Alignment.BottomCenter
                        ) {
                            val towerFill = (room.teamA.avgScore.toFloat() / 500f).coerceIn(0.2f, 0.95f) * animatedTowerHeight.value

                            Column(
                                modifier = Modifier
                                    .fillMaxWidth(0.92f)
                                    .fillMaxHeight(towerFill)
                                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                                    .background(
                                        Brush.verticalGradient(
                                            listOf(TowerBlueStart, TowerBlueMid, TowerBlueDark)
                                        )
                                    )
                                    .border(
                                        1.5.dp,
                                        Color(0xFF80D8FF),
                                        RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                                    )
                                    .padding(horizontal = 8.dp, vertical = 10.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "Avg. Score",
                                    color = Color.White.copy(alpha = 0.85f),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Medium
                                )
                                Text(
                                    text = "$teamAAvgScore",
                                    color = Color.White,
                                    fontSize = 28.sp,
                                    fontWeight = FontWeight.Black
                                )

                                Spacer(modifier = Modifier.height(6.dp))

                                // AHEAD BY BADGE (Reference 1)
                                if (isAhead) {
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = Color(0xFF00C853)
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                        ) {
                                            Icon(
                                                Icons.Filled.ArrowDropUp,
                                                contentDescription = null,
                                                tint = Color.White,
                                                modifier = Modifier.size(16.dp)
                                            )
                                            Text(
                                                text = "AHEAD BY ${Math.abs(scoreDiff)}",
                                                color = Color.White,
                                                fontWeight = FontWeight.Black,
                                                fontSize = 9.sp
                                            )
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.weight(1f))

                                // Vertical Team Name
                                Text(
                                    text = room.teamA.teamName,
                                    color = Color.White.copy(alpha = 0.35f),
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Black,
                                    letterSpacing = 2.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Avatar & Player Tag
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .background(Color(0xFF0A2B4E), CircleShape)
                                .border(2.dp, Color(0xFF00E5FF), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(room.teamA.avatarEmoji, fontSize = 28.sp)
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = Color(0xFF0091EA)
                        ) {
                            Text(
                                text = "YOUR TEAM",
                                color = Color.White,
                                fontWeight = FontWeight.Black,
                                fontSize = 10.sp,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        // Team Stats Bar (Members, Total Score, View Button)
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Color(0xFF08223F).copy(alpha = 0.9f),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF0091EA)),
                            modifier = Modifier.fillMaxWidth(0.96f)
                        ) {
                            Column(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            Icons.Filled.People,
                                            contentDescription = null,
                                            tint = Color(0xFF80D8FF),
                                            modifier = Modifier.size(14.dp)
                                        )
                                        Spacer(modifier = Modifier.width(3.dp))
                                        Text(
                                            text = room.teamA.membersLabel,
                                            color = Color.White,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 11.sp
                                        )
                                    }

                                    Column(horizontalAlignment = Alignment.End) {
                                        Text(
                                            text = "Total Score",
                                            color = Color(0xFF80D8FF),
                                            fontSize = 9.sp
                                        )
                                        Text(
                                            text = "$teamATotalScore",
                                            color = Color.White,
                                            fontWeight = FontWeight.Black,
                                            fontSize = 14.sp
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(6.dp))

                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = Color(0xFF0091EA),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { showTeamViewModal = room.teamA }
                                ) {
                                    Text(
                                        text = "View",
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 11.sp,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.padding(vertical = 4.dp)
                                    )
                                }
                            }
                        }
                    }

                    // RIGHT GOLDEN/ORANGE TOWER: RIVAL TEAM (e.g. DHONI)
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .padding(start = 4.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Top Tower Structure (Gold Castle Tower)
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            contentAlignment = Alignment.BottomCenter
                        ) {
                            val towerFill = (room.teamB.avgScore.toFloat() / 500f).coerceIn(0.2f, 0.95f) * animatedTowerHeight.value

                            Column(
                                modifier = Modifier
                                    .fillMaxWidth(0.92f)
                                    .fillMaxHeight(towerFill)
                                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                                    .background(
                                        Brush.verticalGradient(
                                            listOf(TowerOrangeStart, TowerOrangeMid, TowerOrangeDark)
                                        )
                                    )
                                    .border(
                                        1.5.dp,
                                        Color(0xFFFFD54F),
                                        RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                                    )
                                    .padding(horizontal = 8.dp, vertical = 10.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "Avg. Score",
                                    color = Color.White.copy(alpha = 0.85f),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Medium
                                )
                                Text(
                                    text = "$teamBAvgScore",
                                    color = Color.White,
                                    fontSize = 28.sp,
                                    fontWeight = FontWeight.Black
                                )

                                Spacer(modifier = Modifier.height(6.dp))

                                // BEHIND BY BADGE (If applicable)
                                if (!isAhead) {
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = Color(0xFFD50000)
                                    ) {
                                        Text(
                                            text = "AHEAD BY ${Math.abs(scoreDiff)}",
                                            color = Color.White,
                                            fontWeight = FontWeight.Black,
                                            fontSize = 9.sp,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.weight(1f))

                                // Vertical Team Name
                                Text(
                                    text = room.teamB.teamName,
                                    color = Color.White.copy(alpha = 0.35f),
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Black,
                                    letterSpacing = 2.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Avatar & Player Tag
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .background(Color(0xFF422102), CircleShape)
                                .border(2.dp, Color(0xFFFF9100), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(room.teamB.avatarEmoji, fontSize = 28.sp)
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = Color(0xFFEF6C00)
                        ) {
                            Text(
                                text = "RIVAL TEAM",
                                color = Color.White,
                                fontWeight = FontWeight.Black,
                                fontSize = 10.sp,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        // Team Stats Bar
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Color(0xFF381903).copy(alpha = 0.9f),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFF9100)),
                            modifier = Modifier.fillMaxWidth(0.96f)
                        ) {
                            Column(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            Icons.Filled.People,
                                            contentDescription = null,
                                            tint = Color(0xFFFFD54F),
                                            modifier = Modifier.size(14.dp)
                                        )
                                        Spacer(modifier = Modifier.width(3.dp))
                                        Text(
                                            text = room.teamB.membersLabel,
                                            color = Color.White,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 11.sp
                                        )
                                    }

                                    Column(horizontalAlignment = Alignment.End) {
                                        Text(
                                            text = "Total Score",
                                            color = Color(0xFFFFD54F),
                                            fontSize = 9.sp
                                        )
                                        Text(
                                            text = "$teamBTotalScore",
                                            color = Color.White,
                                            fontWeight = FontWeight.Black,
                                            fontSize = 14.sp
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(6.dp))

                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = Color(0xFFEF6C00),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { showTeamViewModal = room.teamB }
                                ) {
                                    Text(
                                        text = "View",
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 11.sp,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.padding(vertical = 4.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Bottom Section: Your Personal Score Card & Action Button (Reference 1)
            Surface(
                shape = RoundedCornerShape(18.dp),
                color = Color(0xFF131F3B),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, Color(0xFF2979FF)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Score Hexagon/Pill
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFF1E3A70)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 20.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = "YOUR SCORE: ",
                                color = Color(0xFF82B1FF),
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                            Text(
                                text = "${room.userScore}",
                                color = Color.White,
                                fontWeight = FontWeight.Black,
                                fontSize = 20.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Play more to stay ahead and win!",
                        color = Color(0xFFB0BEC5),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Button(
                        onClick = onPlayMore,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF00E676),
                            contentColor = Color.Black
                        ),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Filled.PlayArrow,
                                contentDescription = null,
                                tint = Color.Black,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "PLAY MORE (FREE RE-ENTRY)",
                                fontWeight = FontWeight.Black,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }
        }
    }

    // Modal: View Team Roster Details
    if (showTeamViewModal != null) {
        val team = showTeamViewModal!!
        Dialog(onDismissRequest = { showTeamViewModal = null }) {
            Surface(
                shape = RoundedCornerShape(24.dp),
                color = Color(0xFF101935),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, Color(team.colorHex)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .background(Color(0xFF1E2E55), CircleShape)
                            .border(2.dp, Color(team.colorHex), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(team.avatarEmoji, fontSize = 32.sp)
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "TEAM ${team.teamName}",
                        color = Color.White,
                        fontWeight = FontWeight.Black,
                        fontSize = 20.sp
                    )

                    Text(
                        text = "Led by ${team.captainName}",
                        color = Color(0xFF82B1FF),
                        fontSize = 12.sp
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Total Members", color = Color.Gray, fontSize = 10.sp)
                            Text(team.membersLabel, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Average Score", color = Color.Gray, fontSize = 10.sp)
                            Text("${team.avgScore}", color = WinzoGold, fontWeight = FontWeight.Black, fontSize = 16.sp)
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Total Points", color = Color.Gray, fontSize = 10.sp)
                            Text("${team.totalScore}", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = { showTeamViewModal = null },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(team.colorHex)),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("CLOSE", fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            }
        }
    }
}
