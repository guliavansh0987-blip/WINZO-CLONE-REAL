package com.example.ui.screens

import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.localization.AppLanguage
import com.example.data.localization.WinzoStrings
import com.example.data.model.GameType
import com.example.data.model.UserProfile
import com.example.data.model.VipClubState
import com.example.data.model.WorldWarRoom
import com.example.data.repository.WalletState
import com.example.ui.ScreenDestination
import com.example.ui.theme.WinzoBgDark
import com.example.ui.theme.WinzoCrimson
import com.example.ui.theme.WinzoEmerald
import com.example.ui.theme.WinzoGold
import com.example.ui.theme.WinzoSurfaceCard

data class GameStoryItem(
    val title: String,
    val iconEmoji: String,
    val gameType: GameType?,
    val isLive: Boolean = true
)

@Composable
fun LobbyScreen(
    walletState: WalletState,
    userProfile: UserProfile,
    vipState: VipClubState,
    isPhoneVerified: Boolean,
    worldWarRooms: List<WorldWarRoom>,
    language: AppLanguage,
    onToggleLanguage: () -> Unit,
    onOpenNotifications: () -> Unit,
    onNavigate: (ScreenDestination) -> Unit,
    onStart1v1: (game: GameType, boot: Double) -> Unit,
    onOpenAddCash: () -> Unit,
    onOpenDailySpin: () -> Unit,
    onOpenScratchCard: () -> Unit,
    onOpenDailyStreak: () -> Unit,
    onOpenPhoneOtp: () -> Unit,
    onOpenVipClub: () -> Unit,
    onOpenReferral: () -> Unit,
    onCardPickRequested: (room: WorldWarRoom) -> Unit,
    onPlayPractice: (game: GameType) -> Unit,
    onTriggerAutoPush: () -> Unit = {},
    onOpenAutoPunch: () -> Unit = {}
) {
    var selectedCategory by remember { mutableStateOf("ALL GAMES") }
    val categories = listOf("ALL GAMES", "ACTION", "CASUAL", "SPORTS")

    val gameStories = remember {
        listOf(
            GameStoryItem("Knife Up", "🗡️", GameType.KNIFE_UP),
            GameStoryItem("Fruit Slash", "🍉", GameType.FRUIT_SLASH),
            GameStoryItem("Metro Surfer", "🏃‍♂️", GameType.METRO_RUNNER),
            GameStoryItem("Bubble Blitz", "🔮", GameType.BUBBLE_SHOOTER),
            GameStoryItem("Archery", "🎯", GameType.ARCHERY_BATTLE),
            GameStoryItem("Cricket", "🏏", GameType.CRICKET_TRIVIA),
            GameStoryItem("Ludo Super", "🎲", null),
            GameStoryItem("Pool 3D", "🎱", null)
        )
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(WinzoBgDark)
            .padding(horizontal = 14.dp),
        contentPadding = PaddingValues(bottom = 85.dp)
    ) {
        // Dual Top Bar Header
        item {
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Left: VIP Crown Avatar
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .background(Color.White, RoundedCornerShape(20.dp))
                        .border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(20.dp))
                        .clickable { onOpenVipClub() }
                        .padding(horizontal = 8.dp, vertical = 5.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .background(Color(0xFFEDE7F6), CircleShape)
                            .border(1.5.dp, Color(vipState.currentTier.badgeColorHex), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(userProfile.avatarEmoji, fontSize = 18.sp)
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    Column {
                        Text(userProfile.name, color = Color(0xFF1E1E1E), fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        Text(vipState.currentTier.title, color = Color(vipState.currentTier.badgeColorHex), fontWeight = FontWeight.Black, fontSize = 9.sp)
                    }
                }

                // Center: WinZO Logo and Name
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { onTriggerAutoPush() }
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.winzo_logo_1793967443731),
                        contentDescription = "WinZO Logo",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(24.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .border(1.dp, WinzoGold, RoundedCornerShape(6.dp))
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = "WinZO",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Black,
                        color = WinzoCrimson
                    )
                }

                // Right Actions: Notifications, Lang Toggle, Wallet
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = onOpenNotifications,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            Icons.Default.Notifications,
                            contentDescription = "Alerts",
                            tint = Color(0xFF555555),
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(4.dp))

                    Surface(
                        color = Color.White,
                        shape = RoundedCornerShape(12.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE0E0E0)),
                        modifier = Modifier.clickable { onToggleLanguage() }
                    ) {
                        Text(
                            text = if (language == AppLanguage.ENGLISH) "हिंदी" else "ENG",
                            color = Color(0xFF333333),
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            modifier = Modifier.padding(horizontal = 7.dp, vertical = 4.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(6.dp))

                    // Wallet Balance
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .background(Color.White, RoundedCornerShape(16.dp))
                            .border(1.dp, Color(0xFFFFB300), RoundedCornerShape(16.dp))
                            .clickable { onNavigate(ScreenDestination.Wallet) }
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "₹${walletState.totalBalance.toInt()}",
                            color = Color(0xFF1B5E20),
                            fontWeight = FontWeight.Black,
                            fontSize = 13.sp
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Box(
                            modifier = Modifier
                                .size(20.dp)
                                .background(WinzoEmerald, CircleShape)
                                .clickable { onOpenAddCash() },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.Add,
                                contentDescription = "Add",
                                tint = Color.White,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }
                }
            }
        }

        // Circular Game Stories
        item {
            Spacer(modifier = Modifier.height(14.dp))
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(gameStories) { story ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.clickable {
                            if (story.gameType != null) {
                                onPlayPractice(story.gameType)
                            }
                        }
                    ) {
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .background(
                                    Brush.linearGradient(
                                        listOf(WinzoGold, WinzoCrimson, Color(0xFF7C4DFF))
                                    ),
                                    CircleShape
                                )
                                .padding(2.5.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(Color.White, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(story.iconEmoji, fontSize = 24.sp)
                            }
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = story.title,
                            color = Color(0xFF263238),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1
                        )
                    }
                }
            }
        }

        // Auto Push & Auto Punch System Card with App Logo & Name to User
        item {
            Spacer(modifier = Modifier.height(12.dp))
            Surface(
                color = Color.White,
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.2.dp, Color(0xFFFFD54F)),
                shadowElevation = 3.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Image(
                                painter = painterResource(id = R.drawable.winzo_logo_1793967443731),
                                contentDescription = "WinZO App Logo",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .border(1.dp, WinzoGold, RoundedCornerShape(8.dp))
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "WinZO Games",
                                        fontWeight = FontWeight.Black,
                                        fontSize = 13.sp,
                                        color = Color(0xFF1E1E1E)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Icon(
                                        Icons.Default.ElectricBolt,
                                        contentDescription = null,
                                        tint = WinzoCrimson,
                                        modifier = Modifier.size(13.dp)
                                    )
                                }
                                Text(
                                    text = "Auto Push & Punch to ${userProfile.name}",
                                    fontSize = 10.sp,
                                    color = Color(0xFF6A1B9A),
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        Surface(
                            color = Color(0xFFE8F5E9),
                            shape = RoundedCornerShape(8.dp),
                            border = androidx.compose.foundation.BorderStroke(0.5.dp, Color(0xFF81C784))
                        ) {
                            Text(
                                text = "AUTO ACTIVE",
                                color = Color(0xFF2E7D32),
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Black,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Automated real-time push alerts featuring WinZO logo & personalized rewards. Daily attendance auto-punched with app logo (+₹20 cash).",
                        fontSize = 11.sp,
                        color = Color(0xFF555555),
                        lineHeight = 14.sp
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = onTriggerAutoPush,
                            colors = ButtonDefaults.buttonColors(containerColor = WinzoCrimson),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(36.dp)
                        ) {
                            Text(
                                text = "SEND AUTO PUSH ⚡",
                                fontWeight = FontWeight.Black,
                                fontSize = 10.sp,
                                color = Color.White
                            )
                        }

                        Button(
                            onClick = onOpenAutoPunch,
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32)),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(36.dp)
                        ) {
                            Text(
                                text = "AUTO PUNCH CARD 👆",
                                fontWeight = FontWeight.Black,
                                fontSize = 10.sp,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }

        // KYC Phone Verification Banner if not verified
        if (!isPhoneVerified) {
            item {
                Spacer(modifier = Modifier.height(12.dp))
                Surface(
                    color = Color.White,
                    shape = RoundedCornerShape(14.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E4EC)),
                    shadowElevation = 2.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onOpenPhoneOtp() }
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("🛡️", fontSize = 20.sp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = "Verify Phone & Get ₹20 Free",
                                    color = Color(0xFF1E1E1E),
                                    fontWeight = FontWeight.Black,
                                    fontSize = 12.sp
                                )
                                Text(
                                    text = "Unlock instant UPI withdrawal access",
                                    color = Color(0xFF757575),
                                    fontSize = 10.sp
                                )
                            }
                        }
                        Surface(
                            color = WinzoEmerald,
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = "VERIFY",
                                color = Color.White,
                                fontWeight = FontWeight.Black,
                                fontSize = 10.sp,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }
                    }
                }
            }
        }

        // Hero Banner
        item {
            Spacer(modifier = Modifier.height(12.dp))
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF221A44)),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onNavigate(ScreenDestination.WorldWar) }
            ) {
                Box(modifier = Modifier.fillMaxWidth()) {
                    Image(
                        painter = painterResource(id = R.drawable.winzo_hero_banner_1793967464399),
                        contentDescription = "Winzo World War Banner",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(140.dp)
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(140.dp)
                            .background(
                                Brush.verticalGradient(
                                    listOf(Color.Transparent, Color(0xDD0D0822))
                                )
                            )
                    )
                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(12.dp)
                    ) {
                        Surface(
                            color = WinzoCrimson,
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = "WORLD WAR ARENA",
                                color = Color.White,
                                fontWeight = FontWeight.Black,
                                fontSize = 9.sp,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                        Text(
                            text = "Team Dhoni vs Team Kohli • ₹25,000 War",
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(top = 2.dp)
                        )
                    }
                }
            }
        }

        // 4 Action Pills: Daily Spin, Scratch, 10-Day Streak, Store
        item {
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                QuickActionPill(
                    title = "Daily Spin",
                    subtitle = "Win ₹100",
                    emoji = "🎯",
                    badge = if (walletState.isSpinAvailable) "FREE" else null,
                    onClick = onOpenDailySpin,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(6.dp))
                QuickActionPill(
                    title = "Scratch",
                    subtitle = "Cash 🎁",
                    emoji = "✨",
                    badge = "NEW",
                    onClick = onOpenScratchCard,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(6.dp))
                QuickActionPill(
                    title = "10-Day Streak",
                    subtitle = "Day 3: ₹10",
                    emoji = "🔥",
                    badge = "HOT",
                    onClick = onOpenDailyStreak,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(6.dp))
                QuickActionPill(
                    title = "WinZO Store",
                    subtitle = "Top-Ups",
                    emoji = "💎",
                    badge = "10% OFF",
                    onClick = { onNavigate(ScreenDestination.Store) },
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // INTEGRATED WORLD WAR LIVE ROOMS (SPLIT HUB FEATURE)
        item {
            Spacer(modifier = Modifier.height(18.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.ElectricBolt,
                        contentDescription = "War",
                        tint = WinzoCrimson,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "LIVE WORLD WAR ROOMS",
                        color = Color(0xFF1E1E1E),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Black
                    )
                }

                Text(
                    text = "VIEW ALL >",
                    color = WinzoCrimson,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { onNavigate(ScreenDestination.WorldWar) }
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Show top 2 active World War rooms
            worldWarRooms.take(2).forEach { room ->
                HomeWorldWarCard(
                    room = room,
                    onJoinOrPlay = { onCardPickRequested(room) }
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }

        // WINZO BAAZI 1v1 ARCADE SECTION
        item {
            Spacer(modifier = Modifier.height(18.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = WinzoStrings.get("tab_baazi", language).uppercase(),
                    color = Color(0xFF1E1E1E),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Black
                )
                Text(
                    text = "6 Games Live",
                    color = Color(0xFF757575),
                    fontSize = 11.sp
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(categories) { cat ->
                    val isSelected = selectedCategory == cat
                    Surface(
                        color = if (isSelected) WinzoCrimson else Color.White,
                        shape = RoundedCornerShape(10.dp),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (isSelected) WinzoCrimson else Color(0xFFE2E4EC)
                        ),
                        modifier = Modifier.clickable { selectedCategory = cat }
                    ) {
                        Text(
                            text = cat,
                            color = if (isSelected) Color.White else Color(0xFF424242),
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }
                }
            }
        }

        // Game Cards List
        items(GameType.entries.toTypedArray()) { game ->
            Spacer(modifier = Modifier.height(10.dp))
            GameLobbyCard(
                game = game,
                language = language,
                onStartBattle = { boot -> onStart1v1(game, boot) },
                onPractice = { onPlayPractice(game) }
            )
        }
    }
}

@Composable
private fun HomeWorldWarCard(
    room: WorldWarRoom,
    onJoinOrPlay: () -> Unit
) {
    Surface(
        color = Color.White,
        shape = RoundedCornerShape(14.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E4EC)),
        shadowElevation = 2.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(room.gameType.iconEmoji, fontSize = 24.sp)
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(room.title, color = Color(0xFF1E1E1E), fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    Text("Pool: ₹${room.totalPot.toInt()} • Boot: ₹${room.bootFee.toInt()}", color = Color(0xFF00C853), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }

            Button(
                onClick = onJoinOrPlay,
                colors = ButtonDefaults.buttonColors(containerColor = WinzoCrimson),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.height(34.dp)
            ) {
                Text("CARD WAR 🃏", color = Color.White, fontWeight = FontWeight.Black, fontSize = 11.sp)
            }
        }
    }
}

@Composable
private fun QuickActionPill(
    title: String,
    subtitle: String,
    emoji: String,
    badge: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        color = Color.White,
        shape = RoundedCornerShape(12.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E4EC)),
        shadowElevation = 1.dp,
        modifier = modifier.clickable(onClick = onClick)
    ) {
        Box(modifier = Modifier.padding(8.dp)) {
            if (badge != null) {
                Surface(
                    color = WinzoCrimson,
                    shape = RoundedCornerShape(4.dp),
                    modifier = Modifier.align(Alignment.TopEnd)
                ) {
                    Text(
                        text = badge,
                        color = Color.White,
                        fontSize = 7.sp,
                        fontWeight = FontWeight.Black,
                        modifier = Modifier.padding(horizontal = 3.dp, vertical = 1.dp)
                    )
                }
            }
            Column {
                Text(emoji, fontSize = 20.sp)
                Spacer(modifier = Modifier.height(2.dp))
                Text(title, color = Color(0xFF1E1E1E), fontWeight = FontWeight.Bold, fontSize = 11.sp, maxLines = 1)
                Text(subtitle, color = Color(0xFFE65100), fontSize = 9.sp, fontWeight = FontWeight.Bold, maxLines = 1)
            }
        }
    }
}

@Composable
private fun GameLobbyCard(
    game: GameType,
    language: AppLanguage,
    onStartBattle: (boot: Double) -> Unit,
    onPractice: () -> Unit
) {
    var selectedBoot by remember { mutableDoubleStateOf(game.defaultBoot) }
    val bootOptions = listOf(2.0, 5.0, 10.0, 25.0, 50.0)

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E4EC)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(Color(0xFFEDE7F6), RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(game.iconEmoji, fontSize = 26.sp)
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(game.title, color = Color(0xFF1E1E1E), fontWeight = FontWeight.Black, fontSize = 15.sp)
                        Spacer(modifier = Modifier.width(6.dp))
                        Surface(
                            color = Color(0xFFE8F5E9),
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text(
                                "🟢 ${game.activePlayers} Live",
                                color = Color(0xFF2E7D32),
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                            )
                        }
                    }
                    Text(game.description, color = Color(0xFF616161), fontSize = 11.sp, maxLines = 1)
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Boot Fee Selector Chips
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                bootOptions.forEach { boot ->
                    val isSelected = selectedBoot == boot
                    Surface(
                        color = if (isSelected) WinzoCrimson else Color(0xFFF5F5F5),
                        shape = RoundedCornerShape(8.dp),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (isSelected) WinzoCrimson else Color(0xFFE0E0E0)
                        ),
                        modifier = Modifier.clickable { selectedBoot = boot }
                    ) {
                        Text(
                            text = "₹${boot.toInt()}",
                            color = if (isSelected) Color.White else Color(0xFF333333),
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Action Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = onPractice,
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF424242)),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFBDBDBD)),
                    modifier = Modifier.weight(1f)
                ) {
                    Text(WinzoStrings.get("practice_free", language), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }

                Button(
                    onClick = { onStartBattle(selectedBoot) },
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = WinzoCrimson),
                    modifier = Modifier.weight(1.8f)
                ) {
                    Text(
                        text = "${WinzoStrings.get("play_1v1", language)} (₹${selectedBoot.toInt()}) ⚔️",
                        fontWeight = FontWeight.Black,
                        fontSize = 12.sp,
                        color = Color.White
                    )
                }
            }
        }
    }
}
