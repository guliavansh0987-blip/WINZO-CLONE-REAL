package com.example.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.localization.AppLanguage
import com.example.data.model.AppNotification
import com.example.data.model.AutoPunchState
import com.example.data.model.AutoPushActionType
import com.example.data.model.AutoPushNotification
import com.example.data.model.DailyStreakDay
import com.example.data.model.FantasyMatch
import com.example.data.model.FantasySquad
import com.example.data.model.GameType
import com.example.util.AutoPushNotificationManager
import com.example.data.model.PurchasedVoucher
import com.example.data.model.ReferralData
import com.example.data.model.StoreProduct
import com.example.data.model.TransactionRecord
import com.example.data.model.VipClubState
import com.example.data.model.WinzoTvClip
import com.example.data.model.WorldWarRoom
import com.example.data.repository.WalletState
import com.example.data.repository.WinzoRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.random.Random

sealed interface ScreenDestination {
    data object Lobby : ScreenDestination // Split Baazi & World War Hub
    data object WorldWar : ScreenDestination
    data object Fantasy : ScreenDestination
    data object Store : ScreenDestination // WinZO Store
    data object Tv : ScreenDestination // WinZO TV
    data object Wallet : ScreenDestination
    data object Transactions : ScreenDestination
    data object Tournaments : ScreenDestination
    data class GuildClashScoreboard(val room: WorldWarRoom) : ScreenDestination
    data class PlayGame(
        val gameType: GameType,
        val mode: GameMode,
        val bootAmount: Double,
        val worldWarRoomId: String? = null,
        val tournamentId: String? = null,
        val rivalName: String = "Rahul_Gamer",
        val rivalAvatar: String = "⚡"
    ) : ScreenDestination
}

enum class GameMode {
    ONE_VS_ONE,
    WORLD_WAR,
    TOURNAMENT,
    PRACTICE
}

data class MatchmakingState(
    val isSearching: Boolean = false,
    val targetGame: GameType = GameType.KNIFE_UP,
    val bootAmount: Double = 5.0,
    val searchSeconds: Int = 0,
    val matchedOpponent: String? = null,
    val opponentAvatar: String? = null,
    val isMatchFound: Boolean = false
)

data class WithdrawalSuccessReceipt(
    val record: TransactionRecord,
    val targetAccount: String
)

class WinzoMainViewModel(
    private val repository: WinzoRepository = WinzoRepository()
) : ViewModel() {

    val appLanguage: StateFlow<AppLanguage> = repository.appLanguage
    val walletState: StateFlow<WalletState> = repository.walletState
    val vipClubState: StateFlow<VipClubState> = repository.vipClubState
    val referralData: StateFlow<ReferralData> = repository.referralData
    val transactions: StateFlow<List<TransactionRecord>> = repository.transactions
    val worldWarRooms: StateFlow<List<WorldWarRoom>> = repository.worldWarRooms
    val tournaments = repository.tournaments
    val fantasyMatches: StateFlow<List<FantasyMatch>> = repository.fantasyMatches
    val storeProducts: StateFlow<List<StoreProduct>> = repository.storeProducts
    val purchasedVouchers: StateFlow<List<PurchasedVoucher>> = repository.purchasedVouchers
    val tvClips: StateFlow<List<WinzoTvClip>> = repository.tvClips
    val dailyStreak: StateFlow<List<DailyStreakDay>> = repository.dailyStreak
    val notifications: StateFlow<List<AppNotification>> = repository.notifications
    val isPhoneVerified: StateFlow<Boolean> = repository.isPhoneVerified
    val phoneNumber: StateFlow<String> = repository.phoneNumber
    val userProfile = repository.userProfile

    private val _currentScreen = MutableStateFlow<ScreenDestination>(ScreenDestination.Lobby)
    val currentScreen: StateFlow<ScreenDestination> = _currentScreen.asStateFlow()

    // Dialog & Flow States
    private val _matchmakingState = MutableStateFlow(MatchmakingState())
    val matchmakingState: StateFlow<MatchmakingState> = _matchmakingState.asStateFlow()

    // Fullscreen promotional pop-up ad on app launch!
    private val _showPromoAdDialog = MutableStateFlow(true)
    val showPromoAdDialog: StateFlow<Boolean> = _showPromoAdDialog.asStateFlow()

    private val _showAddCashDialog = MutableStateFlow(false)
    val showAddCashDialog: StateFlow<Boolean> = _showAddCashDialog.asStateFlow()

    private val _showWithdrawDialog = MutableStateFlow(false)
    val showWithdrawDialog: StateFlow<Boolean> = _showWithdrawDialog.asStateFlow()

    private val _showDailySpinDialog = MutableStateFlow(false)
    val showDailySpinDialog: StateFlow<Boolean> = _showDailySpinDialog.asStateFlow()

    private val _showScratchCardDialog = MutableStateFlow(false)
    val showScratchCardDialog: StateFlow<Boolean> = _showScratchCardDialog.asStateFlow()

    private val _showDailyStreakDialog = MutableStateFlow(false)
    val showDailyStreakDialog: StateFlow<Boolean> = _showDailyStreakDialog.asStateFlow()

    private val _showPhoneOtpDialog = MutableStateFlow(false)
    val showPhoneOtpDialog: StateFlow<Boolean> = _showPhoneOtpDialog.asStateFlow()

    private val _showNotificationDrawer = MutableStateFlow(false)
    val showNotificationDrawer: StateFlow<Boolean> = _showNotificationDrawer.asStateFlow()

    private val _showVipDialog = MutableStateFlow(false)
    val showVipDialog: StateFlow<Boolean> = _showVipDialog.asStateFlow()

    private val _showReferralDialog = MutableStateFlow(false)
    val showReferralDialog: StateFlow<Boolean> = _showReferralDialog.asStateFlow()

    private val _activeWorldWarCardPick = MutableStateFlow<WorldWarRoom?>(null)
    val activeWorldWarCardPick: StateFlow<WorldWarRoom?> = _activeWorldWarCardPick.asStateFlow()

    private val _activeFantasyMatchForSquad = MutableStateFlow<FantasyMatch?>(null)
    val activeFantasyMatchForSquad: StateFlow<FantasyMatch?> = _activeFantasyMatchForSquad.asStateFlow()

    private val _lastWithdrawalReceipt = MutableStateFlow<WithdrawalSuccessReceipt?>(null)
    val lastWithdrawalReceipt: StateFlow<WithdrawalSuccessReceipt?> = _lastWithdrawalReceipt.asStateFlow()

    private val _lastPurchasedVoucher = MutableStateFlow<PurchasedVoucher?>(null)
    val lastPurchasedVoucher: StateFlow<PurchasedVoucher?> = _lastPurchasedVoucher.asStateFlow()

    private val _toastMessage = MutableStateFlow<String?>(null)
    val toastMessage: StateFlow<String?> = _toastMessage.asStateFlow()

    // Auto Push to User State
    private val _currentAutoPush = MutableStateFlow<AutoPushNotification?>(null)
    val currentAutoPush: StateFlow<AutoPushNotification?> = _currentAutoPush.asStateFlow()

    private val _autoPushEnabled = MutableStateFlow(true)
    val autoPushEnabled: StateFlow<Boolean> = _autoPushEnabled.asStateFlow()

    // Auto Punch-in Attendance State with App Logo & Name
    private val _autoPunchState = MutableStateFlow(
        AutoPunchState(userName = repository.userProfile.value.name)
    )
    val autoPunchState: StateFlow<AutoPunchState> = _autoPunchState.asStateFlow()

    private val _showAutoPunchDialog = MutableStateFlow(false)
    val showAutoPunchDialog: StateFlow<Boolean> = _showAutoPunchDialog.asStateFlow()

    init {
        startAutoPushEngine()
    }

    private fun startAutoPushEngine() {
        viewModelScope.launch {
            // First auto push occurs 3.5s after launch to greet user with App Logo and Name
            delay(3500)
            if (_autoPushEnabled.value && _currentAutoPush.value == null) {
                triggerAutoPush()
            }

            // Recurring auto push to user every 35 seconds
            while (true) {
                delay(35000)
                if (_autoPushEnabled.value && _currentAutoPush.value == null) {
                    triggerAutoPush()
                }
            }
        }
    }

    private fun generateRandomPush(): AutoPushNotification {
        val userName = userProfile.value.name
        val templates = listOf(
            AutoPushNotification(
                title = "🎁 ₹550 Cash Credited!",
                message = "WinZO Games deposited ₹550 welcome booster into your cash wallet. Tap to claim!",
                actionLabel = "CLAIM NOW",
                actionType = AutoPushActionType.ADD_CASH,
                targetUser = userName
            ),
            AutoPushNotification(
                title = "🎯 Daily Spin Wheel Ready!",
                message = "Free mega spin unlocked on WinZO Games! Spin now to win up to ₹1,000 cash.",
                actionLabel = "SPIN WHEEL",
                actionType = AutoPushActionType.SPIN,
                targetUser = userName
            ),
            AutoPushNotification(
                title = "⚔️ Knife Up 1v1 Battle Invitation",
                message = "Rahul_Gamer challenged you in a 1v1 Knife Up cash battle. Win ₹18 instant cash!",
                actionLabel = "PLAY 1v1",
                actionType = AutoPushActionType.ONE_VS_ONE,
                targetUser = userName
            ),
            AutoPushNotification(
                title = "🛡️ World War Guild Clash Live!",
                message = "Team Kohli is leading by +240 pts in WinZO World War! Play now to defend the pot.",
                actionLabel = "VIEW ARENA",
                actionType = AutoPushActionType.WORLD_WAR,
                targetUser = userName
            ),
            AutoPushNotification(
                title = "🎫 Free Scratch Card Available!",
                message = "WinZO Games unlocked a lucky scratch card for $userName! Scratch & win cash.",
                actionLabel = "SCRATCH CARD",
                actionType = AutoPushActionType.SCRATCH,
                targetUser = userName
            ),
            AutoPushNotification(
                title = "⚡ Auto Punch Attendance Verified",
                message = "Daily punch-in confirmed with WinZO Games for $userName! Streak Day 4 (+₹20).",
                actionLabel = "VIEW PUNCH",
                actionType = AutoPushActionType.DAILY_PUNCH,
                targetUser = userName
            )
        )
        return templates.random()
    }

    fun triggerAutoPush(customPush: AutoPushNotification? = null, context: Context? = null) {
        val push = customPush ?: generateRandomPush()
        _currentAutoPush.value = push
        if (context != null) {
            AutoPushNotificationManager.showSystemNotification(context, push)
        }
    }

    fun dismissAutoPush() {
        _currentAutoPush.value = null
    }

    fun toggleAutoPush() {
        _autoPushEnabled.update { !it }
        val status = if (_autoPushEnabled.value) "ENABLED" else "DISABLED"
        _toastMessage.value = "WinZO Games Auto-Push to User: $status"
    }

    fun handleAutoPushAction(push: AutoPushNotification) {
        _currentAutoPush.value = null
        when (push.actionType) {
            AutoPushActionType.SPIN -> openDailySpin()
            AutoPushActionType.SCRATCH -> openScratchCard()
            AutoPushActionType.ADD_CASH -> openAddCash()
            AutoPushActionType.WORLD_WAR -> navigateTo(ScreenDestination.WorldWar)
            AutoPushActionType.ONE_VS_ONE -> start1v1Matchmaking(GameType.KNIFE_UP, 5.0)
            AutoPushActionType.DAILY_PUNCH -> openAutoPunchDialog()
            AutoPushActionType.VIP -> openVipDialog()
            AutoPushActionType.FANTASY -> navigateTo(ScreenDestination.Fantasy)
        }
    }

    fun openAutoPunchDialog() {
        _showAutoPunchDialog.value = true
    }

    fun closeAutoPunchDialog() {
        _showAutoPunchDialog.value = false
    }

    fun resyncAutoPunch() {
        repository.addBonusCash(20.0, "AUTO_PUNCH_ATTENDANCE")
        _autoPunchState.update {
            it.copy(
                isPunchedToday = true,
                rewardClaimed = true,
                punchTimestamp = "Just now (Verified)"
            )
        }
        _toastMessage.value = "Auto Punch Re-Synced with WinZO Games! +₹20 Bonus Added to ${userProfile.value.name}."
    }

    fun navigateTo(destination: ScreenDestination) {
        _currentScreen.value = destination
    }

    fun toggleLanguage() {
        repository.toggleLanguage()
    }

    fun openPromoAd() {
        _showPromoAdDialog.value = true
    }

    fun closePromoAd() {
        _showPromoAdDialog.value = false
    }

    fun claimPromoAdBonus() {
        _showPromoAdDialog.value = false
        repository.addDepositCash(550.0, "WELCOME_BONUS")
        _toastMessage.value = "Welcome Bonus of ₹550 Cash Credited to Wallet!"
    }

    fun openAddCash() {
        _showAddCashDialog.value = true
    }

    fun closeAddCash() {
        _showAddCashDialog.value = false
    }

    fun openWithdraw() {
        _showWithdrawDialog.value = true
    }

    fun closeWithdraw() {
        _showWithdrawDialog.value = false
    }

    fun dismissReceipt() {
        _lastWithdrawalReceipt.value = null
    }

    fun dismissVoucherReceipt() {
        _lastPurchasedVoucher.value = null
    }

    fun openDailySpin() {
        _showDailySpinDialog.value = true
    }

    fun closeDailySpin() {
        _showDailySpinDialog.value = false
    }

    fun openScratchCard() {
        _showScratchCardDialog.value = true
    }

    fun closeScratchCard() {
        _showScratchCardDialog.value = false
    }

    fun openDailyStreak() {
        _showDailyStreakDialog.value = true
    }

    fun closeDailyStreak() {
        _showDailyStreakDialog.value = false
    }

    fun openPhoneOtp() {
        _showPhoneOtpDialog.value = true
    }

    fun closePhoneOtp() {
        _showPhoneOtpDialog.value = false
    }

    fun submitPhoneOtp(phone: String, otp: String) {
        val success = repository.verifyPhone(phone, otp)
        if (success) {
            _showPhoneOtpDialog.value = false
            _toastMessage.value = "Phone Verified! ₹20 Bonus Cash added to wallet."
        } else {
            _toastMessage.value = "Invalid OTP! Use 5599."
        }
    }

    fun openNotifications() {
        _showNotificationDrawer.value = true
        repository.markNotificationsRead()
    }

    fun closeNotifications() {
        _showNotificationDrawer.value = false
    }

    fun claimDailyStreakReward() {
        val (success, amt) = repository.claimTodayStreak()
        if (success) {
            _toastMessage.value = "Streak Reward Claimed! ₹${amt.toInt()} added to wallet."
        }
    }

    fun buyStoreProduct(productId: String) {
        val (success, voucher) = repository.buyStoreProduct(productId)
        if (success && voucher != null) {
            _lastPurchasedVoucher.value = voucher
            _toastMessage.value = "Purchase Successful! Voucher Generated."
        } else {
            _toastMessage.value = "Insufficient Balance! Please Add Cash."
            _showAddCashDialog.value = true
        }
    }

    fun toggleLikeTvClip(clipId: String) {
        repository.toggleLikeTvClip(clipId)
    }

    fun openVipDialog() {
        _showVipDialog.value = true
    }

    fun closeVipDialog() {
        _showVipDialog.value = false
    }

    fun claimVipPerk() {
        val success = repository.claimDailyVipPerk()
        if (success) {
            _toastMessage.value = "VIP Daily Perk Claimed! ₹20 Bonus & 2 Tickets credited."
        } else {
            _toastMessage.value = "Already claimed today's VIP perk!"
        }
    }

    fun openReferralDialog() {
        _showReferralDialog.value = true
    }

    fun closeReferralDialog() {
        _showReferralDialog.value = false
    }

    fun simulateReferralShare() {
        val reward = repository.simulateFriendReferral()
        _toastMessage.value = "Friend Joined! ₹${reward.toInt()} credited to your Bonus Cash!"
    }

    fun openWorldWarCardPick(room: WorldWarRoom) {
        _activeWorldWarCardPick.value = room
    }

    fun closeWorldWarCardPick() {
        _activeWorldWarCardPick.value = null
    }

    fun openFantasySquadBuilder(match: FantasyMatch) {
        _activeFantasyMatchForSquad.value = match
    }

    fun closeFantasySquadBuilder() {
        _activeFantasyMatchForSquad.value = null
    }

    fun submitFantasySquad(matchId: String, squad: FantasySquad) {
        val success = repository.joinFantasyMatch(matchId, squad)
        _activeFantasyMatchForSquad.value = null
        if (success) {
            _toastMessage.value = "Dream 11 Team Registered! Contest Joined Successfully!"
        } else {
            _toastMessage.value = "Insufficient balance to join Fantasy match!"
            _showAddCashDialog.value = true
        }
    }

    fun clearToast() {
        _toastMessage.value = null
    }

    fun start1v1Matchmaking(game: GameType, boot: Double) {
        if (!repository.deductEntryFee(boot, "1v1 Entry - ${game.title}")) {
            _toastMessage.value = "Insufficient Balance! Please Add Cash."
            _showAddCashDialog.value = true
            return
        }

        _matchmakingState.value = MatchmakingState(
            isSearching = true,
            targetGame = game,
            bootAmount = boot,
            searchSeconds = 1
        )

        val rivals = listOf(
            Pair("Ajay_Gamer", "🔥"),
            Pair("Pooja_Pro", "✨"),
            Pair("Vikas_Legend", "🦁"),
            Pair("Sneha_Queen", "👑"),
            Pair("Rohan_Sniper", "🎯")
        )

        viewModelScope.launch {
            delay(1200)
            val matched = rivals.random()
            _matchmakingState.update {
                it.copy(
                    isSearching = false,
                    isMatchFound = true,
                    matchedOpponent = matched.first,
                    opponentAvatar = matched.second
                )
            }
            delay(1500)
            _matchmakingState.value = MatchmakingState()
            _currentScreen.value = ScreenDestination.PlayGame(
                gameType = game,
                mode = GameMode.ONE_VS_ONE,
                bootAmount = boot,
                rivalName = matched.first,
                rivalAvatar = matched.second
            )
        }
    }

    fun cancelMatchmaking() {
        val state = _matchmakingState.value
        if (state.isSearching) {
            repository.creditWinnings(state.bootAmount, "1v1 Refund - Match Cancelled")
            _matchmakingState.value = MatchmakingState()
        }
    }

    fun confirmWorldWarCardPick(room: WorldWarRoom, pickedTeam: String) {
        val success = repository.joinWorldWarRoom(room.id, pickedTeam)
        _activeWorldWarCardPick.value = null
        if (success) {
            _currentScreen.value = ScreenDestination.PlayGame(
                gameType = room.gameType,
                mode = GameMode.WORLD_WAR,
                bootAmount = room.bootFee,
                worldWarRoomId = room.id
            )
        } else {
            _toastMessage.value = "Insufficient Balance to join World War!"
            _showAddCashDialog.value = true
        }
    }

    fun processAddCash(amount: Double, method: String) {
        viewModelScope.launch {
            delay(600)
            repository.addDepositCash(amount, method)
            _showAddCashDialog.value = false
            _toastMessage.value = "₹$amount Added Successfully via $method!"
        }
    }

    fun processWithdraw(amount: Double, upiOrBank: String) {
        val (success, record) = repository.withdrawWinnings(amount, upiOrBank)
        if (success && record != null) {
            _showWithdrawDialog.value = false
            _lastWithdrawalReceipt.value = WithdrawalSuccessReceipt(record, upiOrBank)
        } else {
            _toastMessage.value = "Withdrawal failed! Check minimum amount (₹10) and winnings balance."
        }
    }

    fun claimSpinPrize(amount: Double, isBonus: Boolean) {
        repository.claimDailySpin(amount, isBonus)
        _toastMessage.value = "Congratulations! Won ₹$amount ${if (isBonus) "Bonus Cash" else "Instant Cash"}!"
    }

    fun scratchCardWin(amount: Double) {
        repository.scratchBonusCard(amount)
        _toastMessage.value = "Awesome! ₹$amount Bonus Cash added to wallet!"
    }

    fun onGameFinished(
        gameType: GameType,
        mode: GameMode,
        bootAmount: Double,
        userScore: Int,
        worldWarRoomId: String?,
        tournamentId: String?,
        rivalName: String
    ): Pair<Boolean, Double> {
        return when (mode) {
            GameMode.ONE_VS_ONE -> {
                val rivalScore = (userScore * (0.8 + Random.nextDouble(0.0, 0.35))).toInt()
                val userWon = userScore >= rivalScore
                val prize = if (userWon) bootAmount * 1.9 else 0.0
                if (userWon) {
                    repository.creditWinnings(prize, "${gameType.title} 1v1 Victory vs $rivalName")
                }
                Pair(userWon, prize)
            }
            GameMode.WORLD_WAR -> {
                if (worldWarRoomId != null) {
                    repository.submitWorldWarGameScore(worldWarRoomId, userScore)
                } else {
                    Pair(true, bootAmount * 1.9)
                }
            }
            GameMode.TOURNAMENT -> {
                if (tournamentId != null) {
                    repository.submitTournamentScore(tournamentId, userScore)
                }
                Pair(true, 0.0)
            }
            GameMode.PRACTICE -> Pair(true, 0.0)
        }
    }
}
