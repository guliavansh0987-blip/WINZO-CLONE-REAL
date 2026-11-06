package com.example.data.model

import androidx.compose.ui.graphics.Color

enum class VipTier(
    val title: String,
    val badgeEmoji: String,
    val badgeColorHex: Long,
    val minLevel: Int,
    val depositCashbackPercent: String,
    val dailyFreeTickets: Int
) {
    BRONZE("Bronze VIP", "🥉", 0xFFCD7F32, 1, "2%", 1),
    SILVER("Silver VIP", "🥈", 0xFFC0C0C0, 5, "4%", 2),
    GOLD("Gold VIP", "🥇", 0xFFFFD700, 12, "6%", 3),
    PLATINUM("Platinum VIP", "💎", 0xFF00E5FF, 25, "8%", 5),
    DIAMOND("Crown Diamond", "👑", 0xFFFF1744, 50, "12%", 10)
}

data class VipClubState(
    val currentTier: VipTier = VipTier.GOLD,
    val currentXp: Int = 1840,
    val nextTierXp: Int = 3000,
    val isDailyPerkClaimed: Boolean = false
)

data class ReferralData(
    val referralCode: String = "WINZO99PRO",
    val friendsJoined: Int = 6,
    val rewardPerInstall: Double = 55.0,
    val totalReferralEarnings: Double = 330.0,
    val lifetimeCommissionPercent: String = "2.5%"
)
