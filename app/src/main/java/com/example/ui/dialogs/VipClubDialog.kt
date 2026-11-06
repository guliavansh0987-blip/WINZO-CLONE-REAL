package com.example.ui.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.VipClubState
import com.example.data.model.VipTier
import com.example.ui.theme.WinzoBgDark
import com.example.ui.theme.WinzoCrimson
import com.example.ui.theme.WinzoEmerald
import com.example.ui.theme.WinzoGold

@Composable
fun VipClubDialog(
    vipState: VipClubState,
    onClaimDailyPerk: () -> Unit,
    onDismiss: () -> Unit
) {
    val tier = vipState.currentTier
    val progress = (vipState.currentXp.toFloat() / vipState.nextTierXp.toFloat()).coerceIn(0f, 1f)

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = WinzoBgDark,
            modifier = Modifier
                .fillMaxWidth()
                .border(2.dp, Color(tier.badgeColorHex), RoundedCornerShape(24.dp))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(22.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Tier Badge
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .background(Color(tier.badgeColorHex).copy(alpha = 0.2f), CircleShape)
                        .border(2.dp, Color(tier.badgeColorHex), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(tier.badgeEmoji, fontSize = 38.sp)
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "WINZO VIP CLUB",
                    color = WinzoGold,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Black
                )

                Text(
                    text = tier.title,
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.ExtraBold
                )

                Spacer(modifier = Modifier.height(14.dp))

                // XP Progress to next tier
                Surface(
                    color = Color(0xFF221A45),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Tier XP Progress", color = Color.LightGray, fontSize = 12.sp)
                            Text(
                                "${vipState.currentXp} / ${vipState.nextTierXp} XP",
                                color = WinzoGold,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        LinearProgressIndicator(
                            progress = { progress },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp)),
                            color = Color(tier.badgeColorHex),
                            trackColor = Color(0xFF150F2F)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Active VIP Privileges
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    VipPerkItem("🎟️ ${tier.dailyFreeTickets} Free Tournament Tickets daily")
                    VipPerkItem("💸 ${tier.depositCashbackPercent} Deposit Cashback boost")
                    VipPerkItem("⚡ Priority Instant UPI Withdrawals")
                    VipPerkItem("👑 Exclusive VIP Crown Chat Badge")
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = onDismiss,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E2452)),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("CLOSE", color = Color.LightGray)
                    }

                    Button(
                        onClick = onClaimDailyPerk,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (vipState.isDailyPerkClaimed) Color.DarkGray else WinzoEmerald
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(1.8f),
                        enabled = !vipState.isDailyPerkClaimed
                    ) {
                        Text(
                            text = if (vipState.isDailyPerkClaimed) "CLAIMED TODAY ✓" else "CLAIM VIP PERK 🎁",
                            fontWeight = FontWeight.Black,
                            color = Color.Black,
                            fontSize = 13.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun VipPerkItem(text: String) {
    Surface(
        color = Color(0xFF1B1536),
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = text,
            color = Color.White,
            fontWeight = FontWeight.Medium,
            fontSize = 13.sp,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
        )
    }
}
