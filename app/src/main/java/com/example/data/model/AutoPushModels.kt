package com.example.data.model

enum class AutoPushActionType {
    SPIN,
    SCRATCH,
    ADD_CASH,
    WORLD_WAR,
    ONE_VS_ONE,
    DAILY_PUNCH,
    VIP,
    FANTASY
}

data class AutoPushNotification(
    val id: String = "PUSH_" + System.currentTimeMillis(),
    val appName: String = "WinZO Games",
    val title: String,
    val message: String,
    val targetUser: String,
    val actionLabel: String = "CLAIM NOW",
    val actionType: AutoPushActionType = AutoPushActionType.SPIN,
    val timestamp: Long = System.currentTimeMillis(),
    val tag: String = "OFFICIAL PUSH"
)

data class AutoPunchState(
    val appName: String = "WinZO Games",
    val userName: String = "WinzoPlayer_99",
    val isPunchedToday: Boolean = true,
    val punchTimestamp: String = "Today, 07:35 AM",
    val streakDay: Int = 4,
    val totalStreakDays: Int = 10,
    val rewardAmount: Double = 20.0,
    val rewardClaimed: Boolean = true
)
