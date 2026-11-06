package com.example.data.model

import androidx.annotation.DrawableRes
import com.example.R

enum class GameType(
    val title: String,
    val category: String,
    val description: String,
    val iconEmoji: String,
    val defaultBoot: Double,
    val activePlayers: String
) {
    KNIFE_UP(
        title = "Knife Up",
        category = "Arcade Action",
        description = "Hit the rotating target with precision. Don't hit other knives!",
        iconEmoji = "🗡️",
        defaultBoot = 5.0,
        activePlayers = "42.8k"
    ),
    FRUIT_SLASH(
        title = "Fruit Samurai",
        category = "Reflex Slicer",
        description = "Slash juicy flying fruits with finger swipes. Avoid the explosive bombs!",
        iconEmoji = "🍉",
        defaultBoot = 10.0,
        activePlayers = "68.2k"
    ),
    METRO_RUNNER(
        title = "Metro Surfer",
        category = "Endless Runner",
        description = "Dash through subway tracks, dodge oncoming trains and collect gold!",
        iconEmoji = "🏃‍♂️",
        defaultBoot = 5.0,
        activePlayers = "55.1k"
    ),
    BUBBLE_SHOOTER(
        title = "Bubble Blitz",
        category = "Puzzle Match",
        description = "Aim and shoot matching colored bubbles to trigger massive chain reactions!",
        iconEmoji = "🔮",
        defaultBoot = 2.0,
        activePlayers = "34.6k"
    ),
    ARCHERY_BATTLE(
        title = "Archery Master",
        category = "Skill Target",
        description = "Compensate for crosswinds and hit the 10-ring bullseye!",
        iconEmoji = "🎯",
        defaultBoot = 10.0,
        activePlayers = "29.4k"
    ),
    CRICKET_TRIVIA(
        title = "Cricket Mania",
        category = "Brain Speed",
        description = "Rapid fire IPL and World Cup questions. Score runs for correct answers!",
        iconEmoji = "🏏",
        defaultBoot = 5.0,
        activePlayers = "51.3k"
    )
}

enum class TransactionType {
    DEPOSIT,
    WINNING,
    ENTRY_FEE,
    WITHDRAWAL,
    BONUS,
    DAILY_SPIN
}

data class TransactionRecord(
    val id: String,
    val title: String,
    val amount: Double,
    val type: TransactionType,
    val timestamp: Long = System.currentTimeMillis(),
    val status: String = "SUCCESS",
    val paymentMethod: String = "Instant UPI",
    val utrNumber: String = "UPI/" + (100000000000L..999999999999L).random()
)

data class TeamInfo(
    val teamName: String,
    val captainName: String,
    val avatarEmoji: String,
    val memberCount: Int,
    val totalScore: Int,
    val avgScore: Int,
    val colorHex: Long,
    val membersLabel: String = "$memberCount+"
)

data class WorldWarRoom(
    val id: String,
    val title: String,
    val gameType: GameType,
    val bootFee: Double,
    val totalPot: Double,
    val timeLeftSeconds: Int,
    val teamA: TeamInfo,
    val teamB: TeamInfo,
    val userTeam: String? = null,
    val userScore: Int = 4336,
    val isCompleted: Boolean = false,
    val winnerTeam: String? = null,
    val teamsCountLabel: String = "2 TEAMS",
    val ruleLabel: String = "ALL SCORES COUNT",
    val entryTypeLabel: String = "MULTIPLE ENTRY"
)

data class TournamentRank(
    val rankRange: String,
    val prizeAmount: Double,
    val percentage: String
)

data class TournamentContest(
    val id: String,
    val title: String,
    val gameType: GameType,
    val entryFee: Double,
    val prizePool: Double,
    val playersJoined: Int,
    val maxPlayers: Int,
    val endsInMinutes: Int,
    val ranks: List<TournamentRank>,
    val userRank: Int? = null,
    val userScore: Int = 0
)

data class UserProfile(
    val name: String = "WinzoPlayer_99",
    val level: Int = 18,
    val totalGamesPlayed: Int = 142,
    val totalWonAmount: Double = 3450.0,
    val avatarEmoji: String = "👑",
    val winRate: String = "74%"
)
