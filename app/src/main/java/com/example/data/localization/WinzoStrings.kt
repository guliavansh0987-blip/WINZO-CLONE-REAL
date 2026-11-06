package com.example.data.localization

enum class AppLanguage(val code: String, val displayName: String) {
    ENGLISH("en", "English"),
    HINDI("hi", "हिंदी")
}

object WinzoStrings {
    private val en = mapOf(
        "app_title" to "WinZO Games",
        "tab_baazi" to "WinZO Baazi",
        "tab_world_war" to "World War",
        "tab_fantasy" to "Fantasy Arena",
        "tab_wallet" to "Wallet",
        "level_pro" to "Level 18 Pro",
        "vip_club" to "VIP Club",
        "add_cash" to "Add Cash",
        "withdraw" to "Withdraw",
        "instant_withdrawal" to "Instant Withdrawal",
        "daily_spin" to "Daily Spin",
        "scratch_card" to "Scratch Card",
        "refer_earn" to "Refer & Earn",
        "refer_tagline" to "Get ₹55 on every friend install!",
        "play_1v1" to "Play 1v1 Battle",
        "practice_free" to "Practice Free",
        "select_boot" to "SELECT 1v1 BOOT:",
        "world_war_title" to "WINZO WORLD WAR",
        "world_war_desc" to "Pick a card to join a team and split massive prize pots!",
        "pick_card_join" to "Pick Card & Join",
        "play_for_team" to "Play For Team",
        "fantasy_title" to "WINZO FANTASY CRICKET",
        "fantasy_desc" to "Pick your dream 11 squad, choose Captain 2X & Vice-Captain 1.5X, and win mega prizes!",
        "create_team" to "Create Dream 11 Team",
        "join_contest" to "Join Contest",
        "total_wallet_balance" to "TOTAL WALLET BALANCE",
        "winnings_cash" to "Winnings Cash",
        "deposit_cash" to "Unutilized Deposit Cash",
        "bonus_cash" to "Promotional Cash Bonus",
        "passbook" to "Transaction Passbook",
        "live_players" to "Live Players",
        "prize_pool" to "Prize Pool",
        "boot_fee" to "Boot Fee",
        "game_stories" to "FEATURED GAMES"
    )

    private val hi = mapOf(
        "app_title" to "विंजो गेम्स",
        "tab_baazi" to "विंजो बाज़ी",
        "tab_world_war" to "वर्ल्ड वॉर",
        "tab_fantasy" to "फैंटेसी अखाड़ा",
        "tab_wallet" to "वॉलेट",
        "level_pro" to "लेवल 18 प्रो",
        "vip_club" to "वीआईपी क्लब",
        "add_cash" to "पैसे जोड़ें",
        "withdraw" to "पैसे निकालें",
        "instant_withdrawal" to "तुरंत निकासी",
        "daily_spin" to "डेली स्पिन",
        "scratch_card" to "स्क्रैच कार्ड",
        "refer_earn" to "रेफर और कमाएं",
        "refer_tagline" to "हर दोस्त के इंस्टॉल पर पाएं ₹55 फ्री!",
        "play_1v1" to "1v1 मुकाबला खेलें",
        "practice_free" to "फ्री प्रैक्टिस",
        "select_boot" to "1v1 बूट चुनें:",
        "world_war_title" to "विंजो वर्ल्ड वॉर",
        "world_war_desc" to "कार्ड चुनें, टीम में शामिल हों और बड़े प्राइज पूल जीतें!",
        "pick_card_join" to "कार्ड चुनें और जुड़ें",
        "play_for_team" to "टीम के लिए खेलें",
        "fantasy_title" to "विंजो फैंटेसी क्रिकेट",
        "fantasy_desc" to "अपनी 11 खिलाड़ियों की टीम बनाएं, कप्तान (2x) चुनें और लाखों जीतें!",
        "create_team" to "ड्रीम 11 टीम बनाएं",
        "join_contest" to "कॉन्टेस्ट जॉइन करें",
        "total_wallet_balance" to "कुल वॉलेट बैलेंस",
        "winnings_cash" to "जीती हुई राशि",
        "deposit_cash" to "जमा राशि (डिपॉजिट)",
        "bonus_cash" to "बोनस कैश",
        "passbook" to "लेन-देन पासबुक",
        "live_players" to "लाइव खिलाड़ी",
        "prize_pool" to "इनाम राशि",
        "boot_fee" to "बूट फीस",
        "game_stories" to "टॉप गेम्स"
    )

    fun get(key: String, language: AppLanguage): String {
        val dict = if (language == AppLanguage.HINDI) hi else en
        return dict[key] ?: en[key] ?: key
    }
}
