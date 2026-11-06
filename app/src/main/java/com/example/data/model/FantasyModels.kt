package com.example.data.model

enum class PlayerRole(val label: String, val shortName: String, val min: Int, val max: Int) {
    WICKET_KEEPER("Wicket Keeper", "WK", 1, 4),
    BATSMAN("Batsman", "BAT", 3, 6),
    ALL_ROUNDER("All Rounder", "AR", 1, 4),
    BOWLER("Bowler", "BOWL", 3, 6)
}

data class FantasyPlayer(
    val id: String,
    val name: String,
    val teamName: String,
    val role: PlayerRole,
    val credits: Double,
    val points: Int,
    val selectionRate: String,
    val avatarEmoji: String
)

data class FantasyMatch(
    val id: String,
    val title: String,
    val teamA: String,
    val teamB: String,
    val teamAShort: String,
    val teamBShort: String,
    val seriesName: String,
    val startTime: String,
    val megaPrizePool: Double,
    val entryFee: Double,
    val firstPrize: Double,
    val totalSpots: Int,
    val filledSpots: Int,
    val isJoined: Boolean = false,
    val players: List<FantasyPlayer>
)

data class FantasySquad(
    val matchId: String,
    val selectedPlayerIds: Set<String>,
    val captainId: String?,
    val viceCaptainId: String?
)
