package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.TransactionRecord
import com.example.data.model.TransactionType
import com.example.data.repository.WalletState
import com.example.ui.theme.WinzoBgDark
import com.example.ui.theme.WinzoCrimson
import com.example.ui.theme.WinzoEmerald
import com.example.ui.theme.WinzoGold
import com.example.ui.theme.WinzoSurfaceCard
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun WalletScreen(
    walletState: WalletState,
    transactions: List<TransactionRecord>,
    onOpenAddCash: () -> Unit,
    onOpenWithdraw: () -> Unit,
    onOpenDailySpin: () -> Unit,
    onOpenScratchCard: () -> Unit,
    onOpenTransactions: () -> Unit = {}
) {
    var selectedFilter by remember { mutableStateOf("ALL") }
    val filterTabs = listOf("ALL", "WON", "ADDED", "WITHDRAWN", "BONUS")

    val filteredTransactions = when (selectedFilter) {
        "WON" -> transactions.filter { it.type == TransactionType.WINNING }
        "ADDED" -> transactions.filter { it.type == TransactionType.DEPOSIT }
        "WITHDRAWN" -> transactions.filter { it.type == TransactionType.WITHDRAWAL }
        "BONUS" -> transactions.filter { it.type == TransactionType.BONUS || it.type == TransactionType.DAILY_SPIN }
        else -> transactions
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(WinzoBgDark)
            .padding(horizontal = 14.dp),
        contentPadding = PaddingValues(bottom = 85.dp)
    ) {
        // Total Balance Header
        item {
            Spacer(modifier = Modifier.height(12.dp))
            Card(
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF221845)),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF433475)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Default.AccountBalanceWallet,
                                contentDescription = "Wallet",
                                tint = WinzoGold,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "TOTAL WALLET BALANCE",
                                color = Color.LightGray,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Surface(
                            color = WinzoEmerald.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    Icons.Default.Lock,
                                    contentDescription = "Safe",
                                    tint = WinzoEmerald,
                                    modifier = Modifier.size(12.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "100% SECURE",
                                    color = WinzoEmerald,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Black
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "₹${String.format("%.2f", walletState.totalBalance)}",
                        color = Color.White,
                        fontSize = 34.sp,
                        fontWeight = FontWeight.Black
                    )
                }
            }
        }

        // 3 Dedicated Breakdown Cards
        item {
            Spacer(modifier = Modifier.height(14.dp))
            Text(
                text = "BALANCE BREAKDOWN",
                color = WinzoGold,
                fontSize = 13.sp,
                fontWeight = FontWeight.Black
            )
            Spacer(modifier = Modifier.height(8.dp))

            // 1. Withdrawable Winnings Cash
            BalanceItemCard(
                title = "Winnings Cash",
                amount = walletState.winningsCash,
                subtitle = "Directly withdrawable to any UPI ID or Bank account.",
                badgeText = "INSTANT WITHDRAWAL",
                badgeColor = WinzoEmerald,
                actionButtonText = "WITHDRAW ⚡",
                actionButtonColor = WinzoEmerald,
                actionButtonTextColor = Color.Black,
                onAction = onOpenWithdraw
            )

            Spacer(modifier = Modifier.height(10.dp))

            // 2. Unutilized Deposit Cash
            BalanceItemCard(
                title = "Unutilized Deposit Cash",
                amount = walletState.depositCash,
                subtitle = "Usable for all 1v1 battle and World War entries.",
                badgeText = "0% DEDUCTION",
                badgeColor = Color(0xFF00E5FF),
                actionButtonText = "ADD CASH +",
                actionButtonColor = Color(0xFF00E5FF),
                actionButtonTextColor = Color.Black,
                onAction = onOpenAddCash
            )

            Spacer(modifier = Modifier.height(10.dp))

            // 3. Promotional Bonus Cash
            BalanceItemCard(
                title = "Promotional Cash Bonus",
                amount = walletState.bonusCash,
                subtitle = "Up to 10% auto-applied discount on contest boot fees.",
                badgeText = "DISCOUNT COUPONS",
                badgeColor = WinzoGold,
                actionButtonText = "EARN BONUS 🎁",
                actionButtonColor = WinzoGold,
                actionButtonTextColor = Color.Black,
                onAction = onOpenDailySpin
            )
        }

        // Passbook / Transaction Ledger
        item {
            Spacer(modifier = Modifier.height(20.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onOpenTransactions() },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "TRANSACTION PASSBOOK",
                        color = WinzoGold,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Black
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "VIEW ALL ›",
                        color = Color(0xFF00E5FF),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Text(
                    text = "${filteredTransactions.size} Records",
                    color = Color.Gray,
                    fontSize = 12.sp
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(filterTabs) { tab ->
                    val isSelected = selectedFilter == tab
                    Surface(
                        color = if (isSelected) WinzoCrimson else Color(0xFF221A44),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.clickable { selectedFilter = tab }
                    ) {
                        Text(
                            text = tab,
                            color = if (isSelected) Color.White else Color.LightGray,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
        }

        if (filteredTransactions.isEmpty()) {
            item {
                Surface(
                    color = WinzoSurfaceCard,
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("📜", fontSize = 28.sp)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "No transactions found in this category",
                            color = Color.LightGray,
                            fontSize = 13.sp
                        )
                    }
                }
            }
        } else {
            items(filteredTransactions) { txn ->
                TransactionPassbookItem(txn)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
private fun BalanceItemCard(
    title: String,
    amount: Double,
    subtitle: String,
    badgeText: String,
    badgeColor: Color,
    actionButtonText: String,
    actionButtonColor: Color,
    actionButtonTextColor: Color,
    onAction: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = WinzoSurfaceCard),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF382C68)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(title, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Text(subtitle, color = Color.LightGray, fontSize = 11.sp)
                }
                Surface(
                    color = badgeColor.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = badgeText,
                        color = badgeColor,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Black,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "₹${String.format("%.2f", amount)}",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.ExtraBold
                )

                Button(
                    onClick = onAction,
                    colors = ButtonDefaults.buttonColors(containerColor = actionButtonColor),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.height(38.dp)
                ) {
                    Text(
                        text = actionButtonText,
                        color = actionButtonTextColor,
                        fontWeight = FontWeight.Black,
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun TransactionPassbookItem(txn: TransactionRecord) {
    val isCredit = txn.type == TransactionType.WINNING ||
            txn.type == TransactionType.DEPOSIT ||
            txn.type == TransactionType.BONUS ||
            txn.type == TransactionType.DAILY_SPIN

    val amountColor = if (isCredit) WinzoEmerald else WinzoCrimson
    val sign = if (isCredit) "+" else "-"

    val dateFormat = SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault())
    val formattedDate = dateFormat.format(Date(txn.timestamp))

    Surface(
        color = Color(0xFF1E173C),
        shape = RoundedCornerShape(14.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF2C2255)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .background(
                        if (isCredit) WinzoEmerald.copy(alpha = 0.2f) else WinzoCrimson.copy(alpha = 0.2f),
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = when (txn.type) {
                        TransactionType.WINNING -> "🏆"
                        TransactionType.DEPOSIT -> "📥"
                        TransactionType.WITHDRAWAL -> "📤"
                        TransactionType.DAILY_SPIN -> "🎯"
                        TransactionType.BONUS -> "🎁"
                        else -> "⚔️"
                    },
                    fontSize = 18.sp
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = txn.title,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )
                Text(
                    text = "${txn.paymentMethod} • $formattedDate",
                    color = Color.Gray,
                    fontSize = 11.sp
                )
                Text(
                    text = "Ref: ${txn.utrNumber}",
                    color = Color.LightGray.copy(alpha = 0.7f),
                    fontSize = 9.sp
                )
            }

            Text(
                text = "$sign₹${String.format("%.2f", txn.amount)}",
                color = amountColor,
                fontWeight = FontWeight.Black,
                fontSize = 15.sp
            )
        }
    }
}
