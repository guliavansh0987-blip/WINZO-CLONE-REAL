package com.example.ui

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.SportsCricket
import androidx.compose.material.icons.outlined.AccountBalanceWallet
import androidx.compose.material.icons.outlined.Casino
import androidx.compose.material.icons.outlined.MilitaryTech
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.material.icons.outlined.SportsCricket
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.localization.WinzoStrings
import com.example.data.model.GameType
import com.example.ui.components.AutoPushBanner
import com.example.ui.dialogs.AddCashDialog
import com.example.ui.dialogs.AutoPunchDialog
import com.example.ui.dialogs.DailySpinWheelDialog
import com.example.ui.dialogs.DailyStreakDialog
import com.example.ui.dialogs.FullscreenPromoAdDialog
import com.example.ui.dialogs.GameResultDialog
import com.example.ui.dialogs.MatchmakingDialog
import com.example.ui.dialogs.NotificationDrawerDialog
import com.example.ui.dialogs.PhoneOtpVerificationDialog
import com.example.ui.dialogs.ReferAndEarnDialog
import com.example.ui.dialogs.ScratchCardDialog
import com.example.ui.dialogs.VipClubDialog
import com.example.ui.dialogs.VoucherPurchaseReceiptDialog
import com.example.ui.dialogs.WithdrawDialog
import com.example.ui.dialogs.WithdrawalReceiptDialog
import com.example.ui.dialogs.WorldWarCardPickDialog
import com.example.ui.games.ArcheryGame
import com.example.ui.games.BubbleShooterGame
import com.example.ui.games.CricketTriviaGame
import com.example.ui.games.FruitSlashGame
import com.example.ui.games.KnifeUpGame
import com.example.ui.games.MetroRunnerGame
import com.example.ui.screens.FantasyScreen
import com.example.ui.screens.FantasySquadBuilderDialog
import com.example.ui.screens.LobbyScreen
import com.example.ui.screens.TournamentScreen
import com.example.ui.screens.WalletScreen
import com.example.ui.screens.WinzoStoreScreen
import com.example.ui.screens.WinzoTvScreen
import com.example.ui.screens.WorldWarScreen
import com.example.ui.theme.WinzoBgDark
import com.example.ui.theme.WinzoCrimson
import com.example.ui.theme.WinzoEmerald
import com.example.ui.theme.WinzoGold
import com.example.ui.theme.WinzoTextPrimary

data class PostGameResultState(
    val gameTitle: String,
    val mode: GameMode,
    val isVictory: Boolean,
    val prizeAmount: Double,
    val userScore: Int,
    val rivalName: String,
    val bootAmount: Double,
    val worldWarRoomId: String? = null,
    val gameType: GameType
)

@Composable
fun WinzoApp(
    viewModel: WinzoMainViewModel = viewModel()
) {
    val context = LocalContext.current
    val currentScreen by viewModel.currentScreen.collectAsState()
    val language by viewModel.appLanguage.collectAsState()
    val walletState by viewModel.walletState.collectAsState()
    val vipClubState by viewModel.vipClubState.collectAsState()
    val referralData by viewModel.referralData.collectAsState()
    val transactions by viewModel.transactions.collectAsState()
    val worldWarRooms by viewModel.worldWarRooms.collectAsState()
    val tournaments by viewModel.tournaments.collectAsState()
    val fantasyMatches by viewModel.fantasyMatches.collectAsState()
    val storeProducts by viewModel.storeProducts.collectAsState()
    val tvClips by viewModel.tvClips.collectAsState()
    val dailyStreak by viewModel.dailyStreak.collectAsState()
    val notifications by viewModel.notifications.collectAsState()
    val isPhoneVerified by viewModel.isPhoneVerified.collectAsState()
    val userProfile by viewModel.userProfile.collectAsState()

    val matchmakingState by viewModel.matchmakingState.collectAsState()
    val showPromoAd by viewModel.showPromoAdDialog.collectAsState()
    val showAddCash by viewModel.showAddCashDialog.collectAsState()
    val showWithdraw by viewModel.showWithdrawDialog.collectAsState()
    val showDailySpin by viewModel.showDailySpinDialog.collectAsState()
    val showScratchCard by viewModel.showScratchCardDialog.collectAsState()
    val showDailyStreak by viewModel.showDailyStreakDialog.collectAsState()
    val showPhoneOtp by viewModel.showPhoneOtpDialog.collectAsState()
    val showNotificationDrawer by viewModel.showNotificationDrawer.collectAsState()
    val showVipDialog by viewModel.showVipDialog.collectAsState()
    val showReferralDialog by viewModel.showReferralDialog.collectAsState()
    val activeWorldWarCardPick by viewModel.activeWorldWarCardPick.collectAsState()
    val activeFantasyMatchForSquad by viewModel.activeFantasyMatchForSquad.collectAsState()
    val withdrawalReceipt by viewModel.lastWithdrawalReceipt.collectAsState()
    val purchasedVoucherReceipt by viewModel.lastPurchasedVoucher.collectAsState()
    val toastMsg by viewModel.toastMessage.collectAsState()
    val currentAutoPush by viewModel.currentAutoPush.collectAsState()
    val autoPushEnabled by viewModel.autoPushEnabled.collectAsState()
    val autoPunchState by viewModel.autoPunchState.collectAsState()
    val showAutoPunch by viewModel.showAutoPunchDialog.collectAsState()

    var postGameResult by remember { mutableStateOf<PostGameResultState?>(null) }

    LaunchedEffect(toastMsg) {
        if (toastMsg != null) {
            Toast.makeText(context, toastMsg, Toast.LENGTH_SHORT).show()
            viewModel.clearToast()
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(WinzoBgDark),
        bottomBar = {
            if (currentScreen !is ScreenDestination.PlayGame) {
                NavigationBar(
                    containerColor = Color.White,
                    contentColor = WinzoTextPrimary,
                    tonalElevation = 8.dp,
                    modifier = Modifier.navigationBarsPadding()
                ) {
                    // 1. WinZO Baazi
                    NavigationBarItem(
                        selected = currentScreen is ScreenDestination.Lobby,
                        onClick = { viewModel.navigateTo(ScreenDestination.Lobby) },
                        icon = {
                            Icon(
                                if (currentScreen is ScreenDestination.Lobby) Icons.Filled.Casino else Icons.Outlined.Casino,
                                contentDescription = "Baazi"
                            )
                        },
                        label = {
                            Text(
                                text = WinzoStrings.get("tab_baazi", language),
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp,
                                maxLines = 1
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = WinzoCrimson,
                            selectedTextColor = WinzoCrimson,
                            unselectedIconColor = Color(0xFF757575),
                            unselectedTextColor = Color(0xFF757575),
                            indicatorColor = Color(0xFFFFEBEE)
                        )
                    )

                    // 2. World War
                    NavigationBarItem(
                        selected = currentScreen is ScreenDestination.WorldWar,
                        onClick = { viewModel.navigateTo(ScreenDestination.WorldWar) },
                        icon = {
                            Icon(
                                if (currentScreen is ScreenDestination.WorldWar) Icons.Filled.MilitaryTech else Icons.Outlined.MilitaryTech,
                                contentDescription = "World War"
                            )
                        },
                        label = {
                            Text(
                                text = WinzoStrings.get("tab_world_war", language),
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp,
                                maxLines = 1
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = WinzoCrimson,
                            selectedTextColor = WinzoCrimson,
                            unselectedIconColor = Color(0xFF757575),
                            unselectedTextColor = Color(0xFF757575),
                            indicatorColor = Color(0xFFFFEBEE)
                        )
                    )

                    // 3. Fantasy Arena
                    NavigationBarItem(
                        selected = currentScreen is ScreenDestination.Fantasy,
                        onClick = { viewModel.navigateTo(ScreenDestination.Fantasy) },
                        icon = {
                            Icon(
                                if (currentScreen is ScreenDestination.Fantasy) Icons.Filled.SportsCricket else Icons.Outlined.SportsCricket,
                                contentDescription = "Fantasy"
                            )
                        },
                        label = {
                            Text(
                                text = WinzoStrings.get("tab_fantasy", language),
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp,
                                maxLines = 1
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color(0xFF6200EA),
                            selectedTextColor = Color(0xFF6200EA),
                            unselectedIconColor = Color(0xFF757575),
                            unselectedTextColor = Color(0xFF757575),
                            indicatorColor = Color(0xFFEDE7F6)
                        )
                    )

                    // 4. WinZO Store
                    NavigationBarItem(
                        selected = currentScreen is ScreenDestination.Store,
                        onClick = { viewModel.navigateTo(ScreenDestination.Store) },
                        icon = {
                            Icon(
                                if (currentScreen is ScreenDestination.Store) Icons.Filled.ShoppingBag else Icons.Outlined.ShoppingBag,
                                contentDescription = "Store"
                            )
                        },
                        label = {
                            Text(
                                text = "Store",
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp,
                                maxLines = 1
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color(0xFFFF8F00),
                            selectedTextColor = Color(0xFFFF8F00),
                            unselectedIconColor = Color(0xFF757575),
                            unselectedTextColor = Color(0xFF757575),
                            indicatorColor = Color(0xFFFFF8E1)
                        )
                    )

                    // 5. Wallet
                    NavigationBarItem(
                        selected = currentScreen is ScreenDestination.Wallet,
                        onClick = { viewModel.navigateTo(ScreenDestination.Wallet) },
                        icon = {
                            Icon(
                                if (currentScreen is ScreenDestination.Wallet) Icons.Filled.AccountBalanceWallet else Icons.Outlined.AccountBalanceWallet,
                                contentDescription = "Wallet"
                            )
                        },
                        label = {
                            Text(
                                text = WinzoStrings.get("tab_wallet", language),
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp,
                                maxLines = 1
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = WinzoEmerald,
                            selectedTextColor = WinzoEmerald,
                            unselectedIconColor = Color(0xFF757575),
                            unselectedTextColor = Color(0xFF757575),
                            indicatorColor = Color(0xFFE8F5E9)
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .statusBarsPadding()
        ) {
            when (val screen = currentScreen) {
                is ScreenDestination.Lobby -> {
                    LobbyScreen(
                        walletState = walletState,
                        userProfile = userProfile,
                        vipState = vipClubState,
                        isPhoneVerified = isPhoneVerified,
                        worldWarRooms = worldWarRooms,
                        language = language,
                        onToggleLanguage = { viewModel.toggleLanguage() },
                        onOpenNotifications = { viewModel.openNotifications() },
                        onNavigate = { viewModel.navigateTo(it) },
                        onStart1v1 = { game, boot -> viewModel.start1v1Matchmaking(game, boot) },
                        onOpenAddCash = { viewModel.openAddCash() },
                        onOpenDailySpin = { viewModel.openDailySpin() },
                        onOpenScratchCard = { viewModel.openScratchCard() },
                        onOpenDailyStreak = { viewModel.openDailyStreak() },
                        onOpenPhoneOtp = { viewModel.openPhoneOtp() },
                        onOpenVipClub = { viewModel.openVipDialog() },
                        onOpenReferral = { viewModel.openReferralDialog() },
                        onCardPickRequested = { room -> viewModel.openWorldWarCardPick(room) },
                        onPlayPractice = { game ->
                            viewModel.navigateTo(
                                ScreenDestination.PlayGame(
                                    gameType = game,
                                    mode = GameMode.PRACTICE,
                                    bootAmount = 0.0
                                )
                            )
                        },
                        onTriggerAutoPush = { viewModel.triggerAutoPush(context = context) },
                        onOpenAutoPunch = { viewModel.openAutoPunchDialog() }
                    )
                }

                is ScreenDestination.WorldWar -> {
                    BackHandler { viewModel.navigateTo(ScreenDestination.Lobby) }
                    WorldWarScreen(
                        rooms = worldWarRooms,
                        onCardPickRequested = { room -> viewModel.openWorldWarCardPick(room) },
                        onEnterGameDirectly = { room ->
                            viewModel.navigateTo(
                                ScreenDestination.PlayGame(
                                    gameType = room.gameType,
                                    mode = GameMode.WORLD_WAR,
                                    bootAmount = room.bootFee,
                                    worldWarRoomId = room.id
                                )
                            )
                        },
                        onOpenScoreboard = { room ->
                            viewModel.navigateTo(ScreenDestination.GuildClashScoreboard(room))
                        }
                    )
                }

                is ScreenDestination.GuildClashScoreboard -> {
                    BackHandler { viewModel.navigateTo(ScreenDestination.WorldWar) }
                    com.example.ui.screens.GuildClashScoreboardScreen(
                        room = screen.room,
                        onBack = { viewModel.navigateTo(ScreenDestination.WorldWar) },
                        onPlayMore = {
                            viewModel.navigateTo(
                                ScreenDestination.PlayGame(
                                    gameType = screen.room.gameType,
                                    mode = GameMode.WORLD_WAR,
                                    bootAmount = screen.room.bootFee,
                                    worldWarRoomId = screen.room.id
                                )
                            )
                        }
                    )
                }

                is ScreenDestination.Transactions -> {
                    BackHandler { viewModel.navigateTo(ScreenDestination.Wallet) }
                    com.example.ui.screens.TransactionsScreen(
                        transactions = transactions,
                        onBack = { viewModel.navigateTo(ScreenDestination.Wallet) }
                    )
                }

                is ScreenDestination.Fantasy -> {
                    BackHandler { viewModel.navigateTo(ScreenDestination.Lobby) }
                    FantasyScreen(
                        matches = fantasyMatches,
                        language = language,
                        onOpenSquadBuilder = { match -> viewModel.openFantasySquadBuilder(match) }
                    )
                }

                is ScreenDestination.Store -> {
                    BackHandler { viewModel.navigateTo(ScreenDestination.Lobby) }
                    WinzoStoreScreen(
                        products = storeProducts,
                        onBuyProduct = { productId -> viewModel.buyStoreProduct(productId) }
                    )
                }

                is ScreenDestination.Tv -> {
                    BackHandler { viewModel.navigateTo(ScreenDestination.Lobby) }
                    WinzoTvScreen(
                        clips = tvClips,
                        onToggleLike = { clipId -> viewModel.toggleLikeTvClip(clipId) }
                    )
                }

                is ScreenDestination.Tournaments -> {
                    BackHandler { viewModel.navigateTo(ScreenDestination.Lobby) }
                    TournamentScreen(
                        tournaments = tournaments,
                        onJoinTournament = { tourney ->
                            viewModel.navigateTo(
                                ScreenDestination.PlayGame(
                                    gameType = tourney.gameType,
                                    mode = GameMode.TOURNAMENT,
                                    bootAmount = tourney.entryFee,
                                    tournamentId = tourney.id
                                )
                            )
                        }
                    )
                }

                is ScreenDestination.Wallet -> {
                    BackHandler { viewModel.navigateTo(ScreenDestination.Lobby) }
                    WalletScreen(
                        walletState = walletState,
                        transactions = transactions,
                        onOpenAddCash = { viewModel.openAddCash() },
                        onOpenWithdraw = { viewModel.openWithdraw() },
                        onOpenDailySpin = { viewModel.openDailySpin() },
                        onOpenScratchCard = { viewModel.openScratchCard() },
                        onOpenTransactions = { viewModel.navigateTo(ScreenDestination.Transactions) }
                    )
                }

                is ScreenDestination.PlayGame -> {
                    BackHandler {
                        viewModel.navigateTo(ScreenDestination.Lobby)
                    }

                    val onFinish: (score: Int) -> Unit = { finalScore ->
                        val (isVictory, prizeWon) = viewModel.onGameFinished(
                            gameType = screen.gameType,
                            mode = screen.mode,
                            bootAmount = screen.bootAmount,
                            userScore = finalScore,
                            worldWarRoomId = screen.worldWarRoomId,
                            tournamentId = screen.tournamentId,
                            rivalName = screen.rivalName
                        )

                        postGameResult = PostGameResultState(
                            gameTitle = screen.gameType.title,
                            mode = screen.mode,
                            isVictory = isVictory,
                            prizeAmount = prizeWon,
                            userScore = finalScore,
                            rivalName = screen.rivalName,
                            bootAmount = screen.bootAmount,
                            worldWarRoomId = screen.worldWarRoomId,
                            gameType = screen.gameType
                        )
                    }

                    val onBackClick: () -> Unit = {
                        viewModel.navigateTo(ScreenDestination.Lobby)
                    }

                    when (screen.gameType) {
                        GameType.KNIFE_UP -> KnifeUpGame(onGameEnd = onFinish, onBack = onBackClick)
                        GameType.FRUIT_SLASH -> FruitSlashGame(onGameEnd = onFinish, onBack = onBackClick)
                        GameType.METRO_RUNNER -> MetroRunnerGame(onGameEnd = onFinish, onBack = onBackClick)
                        GameType.BUBBLE_SHOOTER -> BubbleShooterGame(onGameEnd = onFinish, onBack = onBackClick)
                        GameType.ARCHERY_BATTLE -> ArcheryGame(onGameEnd = onFinish, onBack = onBackClick)
                        GameType.CRICKET_TRIVIA -> CricketTriviaGame(onGameEnd = onFinish, onBack = onBackClick)
                    }
                }
            }

            // Dialogs
            MatchmakingDialog(
                state = matchmakingState,
                onCancel = { viewModel.cancelMatchmaking() }
            )

            // Fullscreen Promo Ad on App Launch or Re-trigger
            if (showPromoAd) {
                FullscreenPromoAdDialog(
                    onClaimBonus = {
                        viewModel.claimPromoAdBonus()
                    },
                    onDismiss = { viewModel.closePromoAd() }
                )
            }

            if (showAddCash) {
                AddCashDialog(
                    onAddConfirmed = { amt, method -> viewModel.processAddCash(amt, method) },
                    onDismiss = { viewModel.closeAddCash() }
                )
            }

            if (showWithdraw) {
                WithdrawDialog(
                    maxWinningsAvailable = walletState.winningsCash,
                    onWithdrawConfirmed = { amt, dest -> viewModel.processWithdraw(amt, dest) },
                    onDismiss = { viewModel.closeWithdraw() }
                )
            }

            if (showDailySpin) {
                DailySpinWheelDialog(
                    onPrizeClaimed = { amt, isBonus -> viewModel.claimSpinPrize(amt, isBonus) },
                    onDismiss = { viewModel.closeDailySpin() }
                )
            }

            if (showScratchCard) {
                ScratchCardDialog(
                    onClaimed = { amt -> viewModel.scratchCardWin(amt) },
                    onDismiss = { viewModel.closeScratchCard() }
                )
            }

            if (showDailyStreak) {
                DailyStreakDialog(
                    days = dailyStreak,
                    onClaimToday = { viewModel.claimDailyStreakReward() },
                    onDismiss = { viewModel.closeDailyStreak() }
                )
            }

            if (showPhoneOtp) {
                PhoneOtpVerificationDialog(
                    onVerifyConfirmed = { phone, otp -> viewModel.submitPhoneOtp(phone, otp) },
                    onDismiss = { viewModel.closePhoneOtp() }
                )
            }

            if (showNotificationDrawer) {
                NotificationDrawerDialog(
                    notifications = notifications,
                    autoPushEnabled = autoPushEnabled,
                    userName = userProfile.name,
                    onToggleAutoPush = { viewModel.toggleAutoPush() },
                    onTriggerAutoPush = { viewModel.triggerAutoPush(context = context) },
                    onOpenAutoPunch = {
                        viewModel.closeNotifications()
                        viewModel.openAutoPunchDialog()
                    },
                    onDismiss = { viewModel.closeNotifications() }
                )
            }

            if (showVipDialog) {
                VipClubDialog(
                    vipState = vipClubState,
                    onClaimDailyPerk = { viewModel.claimVipPerk() },
                    onDismiss = { viewModel.closeVipDialog() }
                )
            }

            if (showReferralDialog) {
                ReferAndEarnDialog(
                    referralData = referralData,
                    onSimulateInvite = { viewModel.simulateReferralShare() },
                    onDismiss = { viewModel.closeReferralDialog() }
                )
            }

            if (activeWorldWarCardPick != null) {
                WorldWarCardPickDialog(
                    room = activeWorldWarCardPick!!,
                    onTeamConfirmed = { team -> viewModel.confirmWorldWarCardPick(activeWorldWarCardPick!!, team) },
                    onDismiss = { viewModel.closeWorldWarCardPick() }
                )
            }

            if (activeFantasyMatchForSquad != null) {
                FantasySquadBuilderDialog(
                    match = activeFantasyMatchForSquad!!,
                    onSaveAndJoin = { squad -> viewModel.submitFantasySquad(activeFantasyMatchForSquad!!.id, squad) },
                    onDismiss = { viewModel.closeFantasySquadBuilder() }
                )
            }

            if (withdrawalReceipt != null) {
                WithdrawalReceiptDialog(
                    receipt = withdrawalReceipt!!,
                    onDismiss = { viewModel.dismissReceipt() }
                )
            }

            if (purchasedVoucherReceipt != null) {
                VoucherPurchaseReceiptDialog(
                    voucher = purchasedVoucherReceipt!!,
                    onDismiss = { viewModel.dismissVoucherReceipt() }
                )
            }

            if (postGameResult != null) {
                val res = postGameResult!!
                GameResultDialog(
                    gameTitle = res.gameTitle,
                    mode = res.mode,
                    isVictory = res.isVictory,
                    prizeAmount = res.prizeAmount,
                    userScore = res.userScore,
                    rivalName = res.rivalName,
                    onPlayAgain = {
                        val g = res.gameType
                        val b = res.bootAmount
                        val m = res.mode
                        val wwId = res.worldWarRoomId
                        postGameResult = null
                        if (m == GameMode.ONE_VS_ONE) {
                            viewModel.start1v1Matchmaking(g, b)
                        } else {
                            viewModel.navigateTo(
                                ScreenDestination.PlayGame(
                                    gameType = g,
                                    mode = m,
                                    bootAmount = b,
                                    worldWarRoomId = wwId
                                )
                            )
                        }
                    },
                    onBackToLobby = {
                        postGameResult = null
                        viewModel.navigateTo(ScreenDestination.Lobby)
                    }
                )
            }

            if (showAutoPunch) {
                AutoPunchDialog(
                    punchState = autoPunchState,
                    onPunchAgain = { viewModel.resyncAutoPunch() },
                    onDismiss = { viewModel.closeAutoPunchDialog() }
                )
            }

            // In-App Floating Heads-up Auto Push Notification Banner with App Logo & Name to User
            AutoPushBanner(
                notification = currentAutoPush,
                onAction = { push -> viewModel.handleAutoPushAction(push) },
                onDismiss = { viewModel.dismissAutoPush() }
            )
        }
    }
}
