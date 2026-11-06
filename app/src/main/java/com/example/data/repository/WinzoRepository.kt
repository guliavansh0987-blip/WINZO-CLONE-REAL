package com.example.data.repository

import com.example.data.localization.AppLanguage
import com.example.data.model.AppNotification
import com.example.data.model.DailyStreakDay
import com.example.data.model.FantasyMatch
import com.example.data.model.FantasyPlayer
import com.example.data.model.FantasySquad
import com.example.data.model.GameType
import com.example.data.model.PlayerRole
import com.example.data.model.PurchasedVoucher
import com.example.data.model.ReferralData
import com.example.data.model.StoreCategory
import com.example.data.model.StoreProduct
import com.example.data.model.TeamInfo
import com.example.data.model.TournamentContest
import com.example.data.model.TournamentRank
import com.example.data.model.TransactionRecord
import com.example.data.model.TransactionType
import com.example.data.model.UserProfile
import com.example.data.model.VipClubState
import com.example.data.model.VipTier
import com.example.data.model.WinzoTvClip
import com.example.data.model.WorldWarRoom
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.random.Random

data class WalletState(
    val depositCash: Double = 120.0,
    val winningsCash: Double = 95.0,
    val bonusCash: Double = 35.0,
    val tickets: Int = 8,
    val isSpinAvailable: Boolean = true
) {
    val totalBalance: Double get() = depositCash + winningsCash + bonusCash
}

class WinzoRepository {

    private val _appLanguage = MutableStateFlow(AppLanguage.ENGLISH)
    val appLanguage: StateFlow<AppLanguage> = _appLanguage.asStateFlow()

    private val _walletState = MutableStateFlow(WalletState())
    val walletState: StateFlow<WalletState> = _walletState.asStateFlow()

    private val _vipClubState = MutableStateFlow(VipClubState())
    val vipClubState: StateFlow<VipClubState> = _vipClubState.asStateFlow()

    private val _referralData = MutableStateFlow(ReferralData())
    val referralData: StateFlow<ReferralData> = _referralData.asStateFlow()

    private val _userProfile = MutableStateFlow(UserProfile())
    val userProfile: StateFlow<UserProfile> = _userProfile.asStateFlow()

    private val _isPhoneVerified = MutableStateFlow(false)
    val isPhoneVerified: StateFlow<Boolean> = _isPhoneVerified.asStateFlow()

    private val _phoneNumber = MutableStateFlow("")
    val phoneNumber: StateFlow<String> = _phoneNumber.asStateFlow()

    private val _transactions = MutableStateFlow<List<TransactionRecord>>(
        listOf(
            TransactionRecord(
                id = "TXN_INIT_1",
                title = "Welcome Bonus Credited",
                amount = 25.0,
                type = TransactionType.BONUS,
                paymentMethod = "WinZO Promotional",
                timestamp = System.currentTimeMillis() - 86400000L * 2
            ),
            TransactionRecord(
                id = "TXN_INIT_2",
                title = "Added Cash via Google Pay",
                amount = 100.0,
                type = TransactionType.DEPOSIT,
                paymentMethod = "GPay UPI",
                timestamp = System.currentTimeMillis() - 86400000L
            ),
            TransactionRecord(
                id = "TXN_INIT_3",
                title = "Knife Up 1v1 Battle Win",
                amount = 18.0,
                type = TransactionType.WINNING,
                paymentMethod = "Game Settlement",
                timestamp = System.currentTimeMillis() - 3600000L * 4
            ),
            TransactionRecord(
                id = "TXN_INIT_4",
                title = "World War Victory Share",
                amount = 45.0,
                type = TransactionType.WINNING,
                paymentMethod = "Team Kohli Pot Share",
                timestamp = System.currentTimeMillis() - 3600000L * 1
            )
        )
    )
    val transactions: StateFlow<List<TransactionRecord>> = _transactions.asStateFlow()

    private val _worldWarRooms = MutableStateFlow<List<WorldWarRoom>>(
        listOf(
            WorldWarRoom(
                id = "WW_101",
                title = "Kohli vs Dhoni Arena",
                gameType = GameType.KNIFE_UP,
                bootFee = 5.0,
                totalPot = 3.12,
                timeLeftSeconds = 185,
                teamA = TeamInfo(
                    teamName = "KOHLI",
                    captainName = "Virat Kohli",
                    avatarEmoji = "👑",
                    memberCount = 150,
                    totalScore = 62983,
                    avgScore = 401,
                    colorHex = 0xFF00B0FF,
                    membersLabel = "150+"
                ),
                teamB = TeamInfo(
                    teamName = "DHONI",
                    captainName = "MS Dhoni",
                    avatarEmoji = "🦁",
                    memberCount = 100,
                    totalScore = 41370,
                    avgScore = 336,
                    colorHex = 0xFFFF9100,
                    membersLabel = "100+"
                ),
                userTeam = "KOHLI",
                userScore = 4336,
                teamsCountLabel = "2 TEAMS",
                ruleLabel = "HIGHSCORE COUNTS",
                entryTypeLabel = "SINGLE ENTRY"
            ),
            WorldWarRoom(
                id = "WW_102",
                title = "Basketball Frenzy",
                gameType = GameType.CRICKET_TRIVIA,
                bootFee = 10.0,
                totalPot = 1323.0,
                timeLeftSeconds = 221,
                teamA = TeamInfo(
                    teamName = "BULLS",
                    captainName = "Jordan Fire",
                    avatarEmoji = "🏀",
                    memberCount = 147,
                    totalScore = 54300,
                    avgScore = 370,
                    colorHex = 0xFF00E5FF,
                    membersLabel = "147"
                ),
                teamB = TeamInfo(
                    teamName = "LAKERS",
                    captainName = "LeBron Gold",
                    avatarEmoji = "⚡",
                    memberCount = 139,
                    totalScore = 49800,
                    avgScore = 358,
                    colorHex = 0xFFFFB300,
                    membersLabel = "139"
                ),
                userTeam = null,
                userScore = 0,
                teamsCountLabel = "4 TEAMS",
                ruleLabel = "ALL SCORES COUNT",
                entryTypeLabel = "MULTIPLE ENTRY"
            ),
            WorldWarRoom(
                id = "WW_103",
                title = "Fruit Samurai Showdown",
                gameType = GameType.FRUIT_SLASH,
                bootFee = 5.0,
                totalPot = 688.5,
                timeLeftSeconds = 85,
                teamA = TeamInfo(
                    teamName = "NINJAS",
                    captainName = "Katana Red",
                    avatarEmoji = "⚔️",
                    memberCount = 150,
                    totalScore = 38200,
                    avgScore = 254,
                    colorHex = 0xFF00E5FF,
                    membersLabel = "150+"
                ),
                teamB = TeamInfo(
                    teamName = "SAMURAIS",
                    captainName = "Blade Gold",
                    avatarEmoji = "🗡️",
                    memberCount = 142,
                    totalScore = 36900,
                    avgScore = 260,
                    colorHex = 0xFFFF5722,
                    membersLabel = "142+"
                ),
                userTeam = null,
                userScore = 0,
                teamsCountLabel = "2 TEAMS",
                ruleLabel = "HIGHSCORE COUNTS",
                entryTypeLabel = "SINGLE"
            ),
            WorldWarRoom(
                id = "WW_104",
                title = "Snakes & Ladders Mega",
                gameType = GameType.METRO_RUNNER,
                bootFee = 5.0,
                totalPot = 324.0,
                timeLeftSeconds = 191,
                teamA = TeamInfo(
                    teamName = "COBRAS",
                    captainName = "Venom Blue",
                    avatarEmoji = "🐍",
                    memberCount = 50,
                    totalScore = 14200,
                    avgScore = 284,
                    colorHex = 0xFF2979FF,
                    membersLabel = "50+"
                ),
                teamB = TeamInfo(
                    teamName = "VIPERS",
                    captainName = "Viper Gold",
                    avatarEmoji = "⚡",
                    memberCount = 48,
                    totalScore = 13800,
                    avgScore = 287,
                    colorHex = 0xFFFFAB00,
                    membersLabel = "48+"
                ),
                userTeam = null,
                userScore = 0,
                teamsCountLabel = "2 TEAMS",
                ruleLabel = "ALL SCORES COUNT",
                entryTypeLabel = "MULTIPLE ENTRY"
            )
        )
    )
    val worldWarRooms: StateFlow<List<WorldWarRoom>> = _worldWarRooms.asStateFlow()

    private val _tournaments = MutableStateFlow<List<TournamentContest>>(
        listOf(
            TournamentContest(
                id = "TOURN_01",
                title = "Fruit Samurai Mega Championship",
                gameType = GameType.FRUIT_SLASH,
                entryFee = 10.0,
                prizePool = 25000.0,
                playersJoined = 1840,
                maxPlayers = 2500,
                endsInMinutes = 24,
                ranks = listOf(
                    TournamentRank("Rank 1", 5000.0, "20%"),
                    TournamentRank("Rank 2 - 5", 1500.0, "24%"),
                    TournamentRank("Rank 6 - 25", 500.0, "40%"),
                    TournamentRank("Rank 26 - 100", 100.0, "16%")
                )
            ),
            TournamentContest(
                id = "TOURN_02",
                title = "Knife Master Sprint Cup",
                gameType = GameType.KNIFE_UP,
                entryFee = 5.0,
                prizePool = 10000.0,
                playersJoined = 1420,
                maxPlayers = 2000,
                endsInMinutes = 12,
                ranks = listOf(
                    TournamentRank("Rank 1", 2500.0, "25%"),
                    TournamentRank("Rank 2 - 10", 500.0, "45%"),
                    TournamentRank("Rank 11 - 50", 100.0, "30%")
                )
            ),
            TournamentContest(
                id = "TOURN_03",
                title = "Metro Runner Grand Prix",
                gameType = GameType.METRO_RUNNER,
                entryFee = 25.0,
                prizePool = 50000.0,
                playersJoined = 1650,
                maxPlayers = 2000,
                endsInMinutes = 45,
                ranks = listOf(
                    TournamentRank("Rank 1", 12500.0, "25%"),
                    TournamentRank("Rank 2", 7500.0, "15%"),
                    TournamentRank("Rank 3 - 10", 2500.0, "40%"),
                    TournamentRank("Rank 11 - 100", 200.0, "20%")
                )
            ),
            TournamentContest(
                id = "TOURN_04",
                title = "Cricket Mania T20 Blast",
                gameType = GameType.CRICKET_TRIVIA,
                entryFee = 5.0,
                prizePool = 8000.0,
                playersJoined = 980,
                maxPlayers = 1600,
                endsInMinutes = 18,
                ranks = listOf(
                    TournamentRank("Rank 1", 2000.0, "25%"),
                    TournamentRank("Rank 2 - 5", 600.0, "30%"),
                    TournamentRank("Rank 6 - 50", 80.0, "45%")
                )
            )
        )
    )
    val tournaments: StateFlow<List<TournamentContest>> = _tournaments.asStateFlow()

    private val samplePlayersCskMi = listOf(
        FantasyPlayer("p1", "MS Dhoni", "CSK", PlayerRole.WICKET_KEEPER, 9.0, 184, "92%", "🦁"),
        FantasyPlayer("p2", "Ishan Kishan", "MI", PlayerRole.WICKET_KEEPER, 8.5, 142, "64%", "⚡"),
        FantasyPlayer("p3", "Rohit Sharma", "MI", PlayerRole.BATSMAN, 9.5, 230, "95%", "🏏"),
        FantasyPlayer("p4", "Ruturaj Gaikwad", "CSK", PlayerRole.BATSMAN, 9.0, 210, "88%", "🔥"),
        FantasyPlayer("p5", "Suryakumar Yadav", "MI", PlayerRole.BATSMAN, 9.5, 260, "96%", "✨"),
        FantasyPlayer("p6", "Shivam Dube", "CSK", PlayerRole.BATSMAN, 8.5, 175, "78%", "💥"),
        FantasyPlayer("p7", "Ravindra Jadeja", "CSK", PlayerRole.ALL_ROUNDER, 9.5, 245, "94%", "⚔️"),
        FantasyPlayer("p8", "Hardik Pandya", "MI", PlayerRole.ALL_ROUNDER, 9.0, 215, "89%", "⚡"),
        FantasyPlayer("p9", "Moeen Ali", "CSK", PlayerRole.ALL_ROUNDER, 8.0, 130, "52%", "🎯"),
        FantasyPlayer("p10", "Jasprit Bumrah", "MI", PlayerRole.BOWLER, 9.5, 280, "98%", "🎳"),
        FantasyPlayer("p11", "Matheesha Pathirana", "CSK", PlayerRole.BOWLER, 8.5, 195, "82%", "⚡"),
        FantasyPlayer("p12", "Gerald Coetzee", "MI", PlayerRole.BOWLER, 8.0, 140, "61%", "🚀"),
        FantasyPlayer("p13", "Deepak Chahar", "CSK", PlayerRole.BOWLER, 8.0, 125, "55%", "🎯"),
        FantasyPlayer("p14", "Tilak Varma", "MI", PlayerRole.BATSMAN, 8.5, 168, "72%", "🏏")
    )

    private val _fantasyMatches = MutableStateFlow<List<FantasyMatch>>(
        listOf(
            FantasyMatch(
                id = "M_IPL_01",
                title = "Chennai Super Kings vs Mumbai Indians",
                teamA = "Chennai Super Kings",
                teamB = "Mumbai Indians",
                teamAShort = "CSK",
                teamBShort = "MI",
                seriesName = "Indian T20 League 2026",
                startTime = "Today, 07:30 PM",
                megaPrizePool = 100000.0,
                entryFee = 25.0,
                firstPrize = 25000.0,
                totalSpots = 5000,
                filledSpots = 3840,
                players = samplePlayersCskMi
            ),
            FantasyMatch(
                id = "M_IPL_02",
                title = "Royal Challengers vs Kolkata Knight Riders",
                teamA = "Royal Challengers",
                teamB = "Kolkata Knight Riders",
                teamAShort = "RCB",
                teamBShort = "KKR",
                seriesName = "Indian T20 League 2026",
                startTime = "Tomorrow, 03:30 PM",
                megaPrizePool = 50000.0,
                entryFee = 15.0,
                firstPrize = 12000.0,
                totalSpots = 4000,
                filledSpots = 2410,
                players = samplePlayersCskMi
            ),
            FantasyMatch(
                id = "M_INT_01",
                title = "India vs Australia T20 Final",
                teamA = "India",
                teamB = "Australia",
                teamAShort = "IND",
                teamBShort = "AUS",
                seriesName = "T20 World Cup Super Clash",
                startTime = "Sun, 07:00 PM",
                megaPrizePool = 250000.0,
                entryFee = 49.0,
                firstPrize = 75000.0,
                totalSpots = 10000,
                filledSpots = 8920,
                players = samplePlayersCskMi
            )
        )
    )
    val fantasyMatches: StateFlow<List<FantasyMatch>> = _fantasyMatches.asStateFlow()

    // WinZO Store Products
    private val _storeProducts = MutableStateFlow<List<StoreProduct>>(
        listOf(
            StoreProduct(
                id = "FF_100",
                title = "Free Fire 100+10 Diamonds",
                subtitle = "Instant In-Game Top-Up with 10% Extra",
                originalPrice = 90.0,
                discountedPrice = 80.0,
                extraBonusText = "+10 FREE DIAMONDS",
                iconEmoji = "💎",
                category = StoreCategory.GAME_CURRENCY
            ),
            StoreProduct(
                id = "FF_310",
                title = "Free Fire 310+31 Diamonds",
                subtitle = "Pro Battle Top-Up Pack",
                originalPrice = 270.0,
                discountedPrice = 240.0,
                extraBonusText = "+31 FREE DIAMONDS",
                iconEmoji = "💎",
                category = StoreCategory.GAME_CURRENCY
            ),
            StoreProduct(
                id = "FF_520",
                title = "Free Fire 520+52 Diamonds",
                subtitle = "Elite Pass Top-Up Bundle",
                originalPrice = 450.0,
                discountedPrice = 400.0,
                extraBonusText = "+52 FREE DIAMONDS",
                iconEmoji = "💎",
                category = StoreCategory.GAME_CURRENCY
            ),
            StoreProduct(
                id = "BGMI_60",
                title = "BGMI 60 Unknown Cash (UC)",
                subtitle = "Instant Battlegrounds Mobile India Delivery",
                originalPrice = 85.0,
                discountedPrice = 75.0,
                extraBonusText = "5% INSTANT OFF",
                iconEmoji = "🪙",
                category = StoreCategory.GAME_CURRENCY
            ),
            StoreProduct(
                id = "BGMI_325",
                title = "BGMI 300+25 Unknown Cash (UC)",
                subtitle = "Royale Pass Starter Pack",
                originalPrice = 420.0,
                discountedPrice = 380.0,
                extraBonusText = "+25 FREE UC",
                iconEmoji = "🪙",
                category = StoreCategory.GAME_CURRENCY
            ),
            StoreProduct(
                id = "GP_100",
                title = "Google Play Recharge Code ₹100",
                subtitle = "Redeemable on Play Store Apps & Games",
                originalPrice = 100.0,
                discountedPrice = 96.0,
                extraBonusText = "4% WALLET CASHBACK",
                iconEmoji = "🛍️",
                category = StoreCategory.GIFT_CARDS
            ),
            StoreProduct(
                id = "GP_250",
                title = "Google Play Recharge Code ₹250",
                subtitle = "Instant Voucher Code SMS Delivery",
                originalPrice = 250.0,
                discountedPrice = 240.0,
                extraBonusText = "4% WALLET CASHBACK",
                iconEmoji = "🛍️",
                category = StoreCategory.GIFT_CARDS
            ),
            StoreProduct(
                id = "OTT_HOTSTAR",
                title = "Disney+ Hotstar 3 Months Mobile",
                subtitle = "Live Cricket & Blockbuster Movies",
                originalPrice = 199.0,
                discountedPrice = 149.0,
                extraBonusText = "25% DISCOUNT",
                iconEmoji = "🎬",
                category = StoreCategory.OTT_SUBSCRIPTIONS
            ),
            StoreProduct(
                id = "OTT_PRIME",
                title = "Amazon Prime Video Voucher",
                subtitle = "Movies, Series & Fast Delivery",
                originalPrice = 349.0,
                discountedPrice = 299.0,
                extraBonusText = "SPECIAL WINZO OFFER",
                iconEmoji = "📦",
                category = StoreCategory.OTT_SUBSCRIPTIONS
            )
        )
    )
    val storeProducts: StateFlow<List<StoreProduct>> = _storeProducts.asStateFlow()

    private val _purchasedVouchers = MutableStateFlow<List<PurchasedVoucher>>(emptyList())
    val purchasedVouchers: StateFlow<List<PurchasedVoucher>> = _purchasedVouchers.asStateFlow()

    // WinZO TV Video Feed
    private val _tvClips = MutableStateFlow<List<WinzoTvClip>>(
        listOf(
            WinzoTvClip(
                id = "TV_01",
                creatorName = "GamerAman",
                creatorAvatar = "⚡",
                title = "Knife Up World Record Stage 14 Complete! 🔥",
                gameTitle = "Knife Up",
                videoEmoji = "🗡️",
                viewsCount = "142.5k",
                likesCount = 8420,
                durationText = "0:45"
            ),
            WinzoTvClip(
                id = "TV_02",
                creatorName = "PoojaQueen",
                creatorAvatar = "👑",
                title = "Insane 68-Fruit Combo in Fruit Samurai! 🍉✨",
                gameTitle = "Fruit Samurai",
                videoEmoji = "🍉",
                viewsCount = "210.8k",
                likesCount = 12450,
                durationText = "0:30"
            ),
            WinzoTvClip(
                id = "TV_03",
                creatorName = "VikasSpeed",
                creatorAvatar = "🦁",
                title = "Subway Track 8000 Meters Run without crashing 🏃‍♂️",
                gameTitle = "Metro Surfer",
                videoEmoji = "🏃‍♂️",
                viewsCount = "98.2k",
                likesCount = 5620,
                durationText = "1:15"
            ),
            WinzoTvClip(
                id = "TV_04",
                creatorName = "DhoniFan99",
                creatorAvatar = "🏏",
                title = "World War ₹50,000 Clutched in last 10 seconds! 🏆",
                gameTitle = "World War",
                videoEmoji = "⚔️",
                viewsCount = "315.4k",
                likesCount = 18900,
                durationText = "0:50"
            )
        )
    )
    val tvClips: StateFlow<List<WinzoTvClip>> = _tvClips.asStateFlow()

    // 10-Day Daily Login Streak
    private val _dailyStreak = MutableStateFlow<List<DailyStreakDay>>(
        listOf(
            DailyStreakDay(1, "₹5 Bonus", 5.0, false, isClaimed = true, isCurrent = false),
            DailyStreakDay(2, "1 Ticket", 10.0, false, isClaimed = true, isCurrent = false),
            DailyStreakDay(3, "₹10 Cash", 10.0, true, isClaimed = false, isCurrent = true),
            DailyStreakDay(4, "₹15 Bonus", 15.0, false, isClaimed = false, isCurrent = false),
            DailyStreakDay(5, "2x Tickets", 20.0, false, isClaimed = false, isCurrent = false),
            DailyStreakDay(6, "₹25 Cash", 25.0, true, isClaimed = false, isCurrent = false),
            DailyStreakDay(7, "Scratch Card", 30.0, false, isClaimed = false, isCurrent = false),
            DailyStreakDay(8, "₹35 Bonus", 35.0, false, isClaimed = false, isCurrent = false),
            DailyStreakDay(9, "Lucky Spin", 40.0, false, isClaimed = false, isCurrent = false),
            DailyStreakDay(10, "₹100 Mega Cash", 100.0, true, isClaimed = false, isCurrent = false)
        )
    )
    val dailyStreak: StateFlow<List<DailyStreakDay>> = _dailyStreak.asStateFlow()

    // Notifications
    private val _notifications = MutableStateFlow<List<AppNotification>>(
        listOf(
            AppNotification("N1", "Mega Fruit Championship Live", "Join now for ₹10 and compete for ₹25,000 guaranteed!", "5m ago", "🏆"),
            AppNotification("N2", "₹55 Referral Reward Ready", "Your friend Rahul installed WinZO! Claim bonus.", "1h ago", "💸"),
            AppNotification("N3", "100% Deposit Cashback Active", "Add ₹100 or more today and get ₹100 extra free bonus.", "3h ago", "🎁")
        )
    )
    val notifications: StateFlow<List<AppNotification>> = _notifications.asStateFlow()

    fun toggleLanguage() {
        _appLanguage.update {
            if (it == AppLanguage.ENGLISH) AppLanguage.HINDI else AppLanguage.ENGLISH
        }
    }

    fun setLanguage(lang: AppLanguage) {
        _appLanguage.value = lang
    }

    fun claimDailyVipPerk(): Boolean {
        if (_vipClubState.value.isDailyPerkClaimed) return false
        _vipClubState.update { it.copy(isDailyPerkClaimed = true, currentXp = it.currentXp + 250) }
        _walletState.update { it.copy(bonusCash = it.bonusCash + 20.0, tickets = it.tickets + 2) }

        val record = TransactionRecord(
            id = "VIP_${System.currentTimeMillis()}",
            title = "VIP Daily Gold Perk Credited",
            amount = 20.0,
            type = TransactionType.BONUS,
            paymentMethod = "VIP Club Rewards",
            status = "SUCCESS"
        )
        _transactions.update { listOf(record) + it }
        return true
    }

    fun simulateFriendReferral(): Double {
        val reward = 55.0
        _referralData.update {
            it.copy(
                friendsJoined = it.friendsJoined + 1,
                totalReferralEarnings = it.totalReferralEarnings + reward
            )
        }
        _walletState.update {
            it.copy(bonusCash = it.bonusCash + reward)
        }
        val record = TransactionRecord(
            id = "REF_${System.currentTimeMillis()}",
            title = "Refer & Earn Bonus (Friend Joined)",
            amount = reward,
            type = TransactionType.BONUS,
            paymentMethod = "Viral Referral",
            status = "SUCCESS"
        )
        _transactions.update { listOf(record) + it }
        return reward
    }

    fun claimTodayStreak(): Pair<Boolean, Double> {
        val currentDay = _dailyStreak.value.find { it.isCurrent && !it.isClaimed } ?: return Pair(false, 0.0)
        _dailyStreak.update { list ->
            list.map {
                if (it.dayNumber == currentDay.dayNumber) it.copy(isClaimed = true)
                else if (it.dayNumber == currentDay.dayNumber + 1) it.copy(isCurrent = true)
                else it
            }
        }

        if (currentDay.isCash) {
            _walletState.update { it.copy(winningsCash = it.winningsCash + currentDay.rewardAmount) }
        } else {
            _walletState.update { it.copy(bonusCash = it.bonusCash + currentDay.rewardAmount) }
        }

        val record = TransactionRecord(
            id = "STRK_${System.currentTimeMillis()}",
            title = "Daily Streak Day ${currentDay.dayNumber} Reward",
            amount = currentDay.rewardAmount,
            type = if (currentDay.isCash) TransactionType.WINNING else TransactionType.BONUS,
            paymentMethod = "WinZO Daily Streak",
            status = "SUCCESS"
        )
        _transactions.update { listOf(record) + it }
        return Pair(true, currentDay.rewardAmount)
    }

    fun verifyPhone(number: String, otp: String): Boolean {
        if (otp == "5599" || otp.length == 4) {
            _isPhoneVerified.value = true
            _phoneNumber.value = number
            _walletState.update { it.copy(bonusCash = it.bonusCash + 20.0) }
            val record = TransactionRecord(
                id = "OTP_${System.currentTimeMillis()}",
                title = "KYC Phone Verification Bonus",
                amount = 20.0,
                type = TransactionType.BONUS,
                paymentMethod = "Mobile Verified",
                status = "SUCCESS"
            )
            _transactions.update { listOf(record) + it }
            return true
        }
        return false
    }

    fun buyStoreProduct(productId: String): Pair<Boolean, PurchasedVoucher?> {
        val product = _storeProducts.value.find { it.id == productId } ?: return Pair(false, null)
        val currentWallet = _walletState.value
        val cost = product.discountedPrice

        if (currentWallet.totalBalance < cost) return Pair(false, null)

        val maxBonus = (cost * 0.10).coerceAtMost(currentWallet.bonusCash)
        val remainder = cost - maxBonus
        val fromDeposit = remainder.coerceAtMost(currentWallet.depositCash)
        val fromWinnings = (remainder - fromDeposit).coerceAtMost(currentWallet.winningsCash)

        if ((fromDeposit + fromWinnings) < remainder) return Pair(false, null)

        _walletState.update {
            it.copy(
                bonusCash = (it.bonusCash - maxBonus).coerceAtLeast(0.0),
                depositCash = (it.depositCash - fromDeposit).coerceAtLeast(0.0),
                winningsCash = (it.winningsCash - fromWinnings).coerceAtLeast(0.0)
            )
        }

        val voucherCode = "WZ-" + (1000..9999).random() + "-" + (1000..9999).random()
        val voucher = PurchasedVoucher(
            id = "VOUCH_${System.currentTimeMillis()}",
            productName = product.title,
            voucherCode = voucherCode,
            amountPaid = cost
        )
        _purchasedVouchers.update { listOf(voucher) + it }

        val record = TransactionRecord(
            id = "STORE_${System.currentTimeMillis()}",
            title = "WinZO Store Purchase - ${product.title}",
            amount = cost,
            type = TransactionType.ENTRY_FEE,
            paymentMethod = "Wallet Balance",
            status = "SUCCESS"
        )
        _transactions.update { listOf(record) + it }
        return Pair(true, voucher)
    }

    fun toggleLikeTvClip(clipId: String) {
        _tvClips.update { list ->
            list.map {
                if (it.id == clipId) {
                    val newLiked = !it.isLiked
                    it.copy(
                        isLiked = newLiked,
                        likesCount = if (newLiked) it.likesCount + 1 else it.likesCount - 1
                    )
                } else it
            }
        }
    }

    fun markNotificationsRead() {
        _notifications.update { list -> list.map { it.copy(isRead = true) } }
    }

    fun joinFantasyMatch(matchId: String, squad: FantasySquad): Boolean {
        val match = _fantasyMatches.value.find { it.id == matchId } ?: return false
        if (!deductEntryFee(match.entryFee, "Fantasy League - ${match.teamAShort} vs ${match.teamBShort}")) {
            return false
        }
        _fantasyMatches.update { list ->
            list.map {
                if (it.id == matchId) it.copy(isJoined = true, filledSpots = it.filledSpots + 1) else it
            }
        }
        return true
    }

    fun deductEntryFee(amount: Double, reason: String): Boolean {
        val current = _walletState.value
        if (current.totalBalance < amount) return false

        val maxBonusDiscount = (amount * 0.10).coerceAtMost(current.bonusCash)
        val cashRemaining = amount - maxBonusDiscount

        val fromDeposit = cashRemaining.coerceAtMost(current.depositCash)
        val fromWinnings = (cashRemaining - fromDeposit).coerceAtMost(current.winningsCash)

        if ((fromDeposit + fromWinnings) < cashRemaining) return false

        _walletState.update {
            it.copy(
                bonusCash = (it.bonusCash - maxBonusDiscount).coerceAtLeast(0.0),
                depositCash = (it.depositCash - fromDeposit).coerceAtLeast(0.0),
                winningsCash = (it.winningsCash - fromWinnings).coerceAtLeast(0.0)
            )
        }

        val record = TransactionRecord(
            id = "TXN_${System.currentTimeMillis()}",
            title = reason,
            amount = amount,
            type = TransactionType.ENTRY_FEE,
            paymentMethod = "Wallet Balance",
            status = "SUCCESS"
        )
        _transactions.update { listOf(record) + it }
        return true
    }

    fun creditWinnings(amount: Double, title: String) {
        _walletState.update {
            it.copy(winningsCash = it.winningsCash + amount)
        }
        _userProfile.update {
            it.copy(
                totalGamesPlayed = it.totalGamesPlayed + 1,
                totalWonAmount = it.totalWonAmount + amount
            )
        }
        val record = TransactionRecord(
            id = "TXN_${System.currentTimeMillis()}",
            title = title,
            amount = amount,
            type = TransactionType.WINNING,
            paymentMethod = "Instant Winnings",
            status = "SUCCESS"
        )
        _transactions.update { listOf(record) + it }
    }

    fun addDepositCash(amount: Double, method: String): TransactionRecord {
        _walletState.update {
            it.copy(depositCash = it.depositCash + amount)
        }
        val record = TransactionRecord(
            id = "TXN_${System.currentTimeMillis()}",
            title = "Cash Added Successfully",
            amount = amount,
            type = TransactionType.DEPOSIT,
            paymentMethod = method,
            status = "SUCCESS"
        )
        _transactions.update { listOf(record) + it }
        return record
    }

    fun withdrawWinnings(amount: Double, upiOrBank: String): Pair<Boolean, TransactionRecord?> {
        val current = _walletState.value
        if (amount > current.winningsCash || amount < 10.0) {
            return Pair(false, null)
        }
        _walletState.update {
            it.copy(winningsCash = it.winningsCash - amount)
        }
        val record = TransactionRecord(
            id = "WTH_${System.currentTimeMillis()}",
            title = "Instant Withdrawal to $upiOrBank",
            amount = amount,
            type = TransactionType.WITHDRAWAL,
            paymentMethod = if (upiOrBank.contains("@")) "UPI Direct Instant" else "IMPS Bank Transfer",
            status = "SUCCESS"
        )
        _transactions.update { listOf(record) + it }
        return Pair(true, record)
    }

    fun claimDailySpin(prizeAmount: Double, isBonus: Boolean): TransactionRecord {
        _walletState.update {
            if (isBonus) {
                it.copy(bonusCash = it.bonusCash + prizeAmount, isSpinAvailable = false)
            } else {
                it.copy(winningsCash = it.winningsCash + prizeAmount, isSpinAvailable = false)
            }
        }
        val record = TransactionRecord(
            id = "SPIN_${System.currentTimeMillis()}",
            title = "WinZO Daily Lucky Spin Reward",
            amount = prizeAmount,
            type = TransactionType.DAILY_SPIN,
            paymentMethod = if (isBonus) "Bonus Credit" else "Instant Cash",
            status = "SUCCESS"
        )
        _transactions.update { listOf(record) + it }
        return record
    }

    fun scratchBonusCard(amount: Double): TransactionRecord {
        _walletState.update {
            it.copy(bonusCash = it.bonusCash + amount)
        }
        val record = TransactionRecord(
            id = "SCRATCH_${System.currentTimeMillis()}",
            title = "Lucky Scratch Card Mystery Cash",
            amount = amount,
            type = TransactionType.BONUS,
            paymentMethod = "Promotional Scratch",
            status = "SUCCESS"
        )
        _transactions.update { listOf(record) + it }
        return record
    }

    fun addBonusCash(amount: Double, reason: String = "AUTO_PUNCH_ATTENDANCE"): TransactionRecord {
        _walletState.update {
            it.copy(bonusCash = it.bonusCash + amount)
        }
        val record = TransactionRecord(
            id = "BONUS_${System.currentTimeMillis()}",
            title = "WinZO Games Auto Punch Bonus",
            amount = amount,
            type = TransactionType.BONUS,
            paymentMethod = "Auto Punch Verified",
            status = "SUCCESS"
        )
        _transactions.update { listOf(record) + it }
        return record
    }

    fun joinWorldWarRoom(roomId: String, teamChosen: String): Boolean {
        val room = _worldWarRooms.value.find { it.id == roomId } ?: return false
        if (!deductEntryFee(room.bootFee, "World War Entry - ${room.title}")) {
            return false
        }
        _worldWarRooms.update { list ->
            list.map {
                if (it.id == roomId) {
                    val updatedTeamA = if (teamChosen == it.teamA.teamName) {
                        it.teamA.copy(memberCount = it.teamA.memberCount + 1)
                    } else it.teamA
                    val updatedTeamB = if (teamChosen == it.teamB.teamName) {
                        it.teamB.copy(memberCount = it.teamB.memberCount + 1)
                    } else it.teamB
                    it.copy(
                        userTeam = teamChosen,
                        totalPot = it.totalPot + (it.bootFee * 1.9),
                        teamA = updatedTeamA,
                        teamB = updatedTeamB
                    )
                } else it
            }
        }
        return true
    }

    fun submitWorldWarGameScore(roomId: String, score: Int): Pair<Boolean, Double> {
        val room = _worldWarRooms.value.find { it.id == roomId } ?: return Pair(false, 0.0)
        val userTeam = room.userTeam ?: return Pair(false, 0.0)

        val teamAScore = room.teamA.totalScore + if (userTeam == room.teamA.teamName) score else (score * 0.85 + Random.nextInt(20, 60)).toInt()
        val teamBScore = room.teamB.totalScore + if (userTeam == room.teamB.teamName) score else (score * 0.85 + Random.nextInt(20, 60)).toInt()

        val isUserTeamWinner = if (userTeam == room.teamA.teamName) teamAScore >= teamBScore else teamBScore >= teamAScore
        val winnerTeam = if (teamAScore >= teamBScore) room.teamA.teamName else room.teamB.teamName

        val prizeShare = if (isUserTeamWinner) room.bootFee * 1.92 else 0.0

        if (prizeShare > 0) {
            creditWinnings(prizeShare, "World War Victory Share - ${room.title}")
        }

        _worldWarRooms.update { list ->
            list.map {
                if (it.id == roomId) {
                    it.copy(
                        userScore = score,
                        isCompleted = true,
                        winnerTeam = winnerTeam,
                        teamA = it.teamA.copy(totalScore = teamAScore),
                        teamB = it.teamB.copy(totalScore = teamBScore)
                    )
                } else it
            }
        }

        return Pair(isUserTeamWinner, prizeShare)
    }

    fun submitTournamentScore(tournamentId: String, score: Int) {
        _tournaments.update { list ->
            list.map { tourney ->
                if (tourney.id == tournamentId) {
                    val rank = when {
                        score > 400 -> 1
                        score > 250 -> 4
                        score > 150 -> 14
                        else -> 32
                    }
                    tourney.copy(userScore = score, userRank = rank)
                } else tourney
            }
        }
    }
}
