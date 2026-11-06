package com.example.data.model

enum class StoreCategory(val label: String) {
    GAME_CURRENCY("Game Top-Ups"),
    GIFT_CARDS("Gift Vouchers"),
    OTT_SUBSCRIPTIONS("OTT & Entertainment")
}

data class StoreProduct(
    val id: String,
    val title: String,
    val subtitle: String,
    val originalPrice: Double,
    val discountedPrice: Double,
    val extraBonusText: String,
    val iconEmoji: String,
    val category: StoreCategory
)

data class PurchasedVoucher(
    val id: String,
    val productName: String,
    val voucherCode: String,
    val amountPaid: Double,
    val timestamp: Long = System.currentTimeMillis()
)

data class WinzoTvClip(
    val id: String,
    val creatorName: String,
    val creatorAvatar: String,
    val title: String,
    val gameTitle: String,
    val videoEmoji: String,
    val viewsCount: String,
    var likesCount: Int,
    var isLiked: Boolean = false,
    val durationText: String
)

data class DailyStreakDay(
    val dayNumber: Int,
    val rewardText: String,
    val rewardAmount: Double,
    val isCash: Boolean,
    var isClaimed: Boolean = false,
    val isCurrent: Boolean = false
)

data class AppNotification(
    val id: String,
    val title: String,
    val message: String,
    val timeAgo: String,
    val iconEmoji: String,
    var isRead: Boolean = false
)
