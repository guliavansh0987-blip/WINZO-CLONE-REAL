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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.TransactionRecord
import com.example.data.model.TransactionType
import com.example.ui.theme.WinzoBgDark
import com.example.ui.theme.WinzoCrimson
import com.example.ui.theme.WinzoEmerald
import com.example.ui.theme.WinzoGold
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * 1:1 Authentic Transactions History matching Reference 2 (Screenshot_20261106_194048_088618ee.jpg):
 * - Purple header with Back arrow, "TRANSACTIONS", and 3-dots action
 * - Dual Tabs: "ALL TRANSACTIONS" and "MY PURCHASES"
 * - Filter Pills: "All", "Tds Transactions", "Add Cash/Withdrawal"
 * - Informational notice: "You can only view transactions for the last 30 days in this section"
 * - Transaction item card with GST breakdown tags, timestamp, +₹100 in green, and "Help" icon button
 */
@Composable
fun TransactionsScreen(
    transactions: List<TransactionRecord>,
    onBack: () -> Unit
) {
    var selectedTopTab by remember { mutableIntStateOf(0) } // 0 = ALL TRANSACTIONS, 1 = MY PURCHASES
    var selectedFilterPill by remember { mutableStateOf("All") } // "All", "Tds Transactions", "Add Cash/Withdrawal"
    val filterPills = listOf("All", "Tds Transactions", "Add Cash/Withdrawal")

    val filteredList = when (selectedFilterPill) {
        "Add Cash/Withdrawal" -> transactions.filter {
            it.type == TransactionType.DEPOSIT || it.type == TransactionType.WITHDRAWAL
        }
        "Tds Transactions" -> transactions.filter {
            it.type == TransactionType.WITHDRAWAL
        }
        else -> transactions
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF4F4F8))
    ) {
        // Purple Top Bar (Reference 2)
        Surface(
            color = Color(0xFF4A148C),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }

                    Text(
                        text = "TRANSACTIONS",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp,
                        modifier = Modifier.weight(1f)
                    )
                }

                // Sub-tabs: ALL TRANSACTIONS & MY PURCHASES
                TabRow(
                    selectedTabIndex = selectedTopTab,
                    containerColor = Color(0xFF4A148C),
                    contentColor = Color.White,
                    indicator = { tabPositions ->
                        TabRowDefaults.SecondaryIndicator(
                            Modifier.tabIndicatorOffset(tabPositions[selectedTopTab]),
                            color = Color.White,
                            height = 3.dp
                        )
                    }
                ) {
                    Tab(
                        selected = selectedTopTab == 0,
                        onClick = { selectedTopTab = 0 },
                        text = {
                            Text(
                                "ALL TRANSACTIONS",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = if (selectedTopTab == 0) Color.White else Color.White.copy(alpha = 0.6f)
                            )
                        }
                    )
                    Tab(
                        selected = selectedTopTab == 1,
                        onClick = { selectedTopTab = 1 },
                        text = {
                            Text(
                                "MY PURCHASES",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = if (selectedTopTab == 1) Color.White else Color.White.copy(alpha = 0.6f)
                            )
                        }
                    )
                }
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 14.dp),
            contentPadding = PaddingValues(top = 12.dp, bottom = 80.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Filter Pills Row (Reference 2)
            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(filterPills) { pill ->
                        val isSelected = selectedFilterPill == pill
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = if (isSelected) Color(0xFF311B92) else Color.White,
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                if (isSelected) Color(0xFF311B92) else Color(0xFFE0E0E0)
                            ),
                            modifier = Modifier.clickable { selectedFilterPill = pill }
                        ) {
                            Text(
                                text = pill,
                                color = if (isSelected) Color.White else Color(0xFF424242),
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                            )
                        }
                    }
                }
            }

            // 30-Day Notice Banner (Reference 2)
            item {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0xFFF3E5F5),
                    border = androidx.compose.foundation.BorderStroke(0.8.dp, Color(0xFFE1BEE7)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "You can only view transactions for the last 30 days in this section",
                        color = Color(0xFF6A1B9A),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(vertical = 10.dp, horizontal = 12.dp)
                    )
                }
            }

            // Transaction Cards List (Reference 2)
            items(filteredList) { txn ->
                AuthenticTransactionCard(txn)
            }

            if (filteredList.isEmpty()) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 40.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            Icons.Default.ReceiptLong,
                            contentDescription = null,
                            tint = Color.Gray,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "No transactions found in this filter",
                            color = Color.Gray,
                            fontSize = 13.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun AuthenticTransactionCard(txn: TransactionRecord) {
    val isCredit = txn.type == TransactionType.WINNING ||
            txn.type == TransactionType.DEPOSIT ||
            txn.type == TransactionType.BONUS ||
            txn.type == TransactionType.DAILY_SPIN

    val dateFormat = SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault())
    val formattedDate = dateFormat.format(Date(txn.timestamp))

    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = androidx.compose.foundation.BorderStroke(0.8.dp, Color(0xFFE0E0E0)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Wallet Icon with badge
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color(0xFF3F51B5), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text("💳", fontSize = 20.sp)
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = if (txn.type == TransactionType.DEPOSIT) "Add Cash (incl GST)" else txn.title,
                        color = Color(0xFF212121),
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                    Text(
                        text = formattedDate,
                        color = Color.Gray,
                        fontSize = 11.sp
                    )
                }

                // Amount
                Text(
                    text = "${if (isCredit) "+" else "-"}₹${txn.amount.toInt()}",
                    color = if (isCredit) Color(0xFF00C853) else Color(0xFFD50000),
                    fontWeight = FontWeight.Black,
                    fontSize = 17.sp
                )

                Spacer(modifier = Modifier.width(10.dp))

                // Help Button
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = Color(0xFFFAFAFA),
                    border = androidx.compose.foundation.BorderStroke(0.8.dp, Color(0xFFBDBDBD))
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp)
                    ) {
                        Icon(
                            Icons.Filled.HelpOutline,
                            contentDescription = "Help",
                            tint = Color.Gray,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(2.dp))
                        Text(
                            text = "Help",
                            color = Color.DarkGray,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Expandable Show More Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Ref: ${txn.utrNumber}",
                    color = Color.LightGray,
                    fontSize = 10.sp
                )
            }
        }
    }
}
