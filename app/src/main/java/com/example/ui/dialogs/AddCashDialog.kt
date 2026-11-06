package com.example.ui.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.theme.WinzoBgDark
import com.example.ui.theme.WinzoEmerald
import com.example.ui.theme.WinzoGold

@Composable
fun AddCashDialog(
    onAddConfirmed: (amount: Double, method: String) -> Unit,
    onDismiss: () -> Unit
) {
    val presetAmounts = listOf(50.0, 100.0, 200.0, 500.0, 1000.0)
    var selectedAmount by remember { mutableDoubleStateOf(100.0) }
    var customAmountText by remember { mutableStateOf("100") }
    var selectedPaymentMethod by remember { mutableStateOf("Google Pay UPI") }

    val paymentOptions = listOf(
        Pair("Google Pay UPI", "⚡"),
        Pair("PhonePe UPI", "🟣"),
        Pair("Paytm UPI", "🔵"),
        Pair("Net Banking / IMPS", "🏛️"),
        Pair("Debit / Credit Card", "💳")
    )

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = WinzoBgDark,
            modifier = Modifier
                .fillMaxWidth()
                .border(2.dp, WinzoGold, RoundedCornerShape(24.dp))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(22.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "ADD CASH TO WALLET",
                        color = WinzoGold,
                        fontWeight = FontWeight.Black,
                        fontSize = 17.sp
                    )
                    Text(
                        text = "100% SECURE 🔒",
                        color = WinzoEmerald,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Amount text input
                OutlinedTextField(
                    value = customAmountText,
                    onValueChange = {
                        customAmountText = it.filter { ch -> ch.isDigit() }
                        val num = customAmountText.toDoubleOrNull() ?: 0.0
                        selectedAmount = num
                    },
                    label = { Text("Amount in Rupees (₹)", color = Color.LightGray) },
                    prefix = { Text("₹ ", color = WinzoGold, fontWeight = FontWeight.Bold) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = WinzoGold,
                        unfocusedBorderColor = Color(0xFF453A70)
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Quick Preset Chips
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    presetAmounts.forEach { amt ->
                        val isSelected = selectedAmount == amt
                        Surface(
                            color = if (isSelected) WinzoGold else Color(0xFF261E47),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .clickable {
                                    selectedAmount = amt
                                    customAmountText = amt.toInt().toString()
                                }
                        ) {
                            Text(
                                text = "₹${amt.toInt()}",
                                color = if (isSelected) Color.Black else Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "SELECT PAYMENT METHOD",
                    color = Color.LightGray,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    paymentOptions.forEach { (method, icon) ->
                        val isSelected = selectedPaymentMethod == method
                        Surface(
                            color = if (isSelected) Color(0xFF332760) else Color(0xFF1C1635),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(
                                    1.dp,
                                    if (isSelected) WinzoGold else Color(0xFF352B5C),
                                    RoundedCornerShape(12.dp)
                                )
                                .clickable { selectedPaymentMethod = method }
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(icon, fontSize = 20.sp)
                                Spacer(modifier = Modifier.size(10.dp))
                                Text(
                                    text = method,
                                    color = Color.White,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 14.sp,
                                    modifier = Modifier.weight(1f)
                                )
                                if (isSelected) {
                                    Text("✓", color = WinzoEmerald, fontWeight = FontWeight.Black, fontSize = 16.sp)
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(22.dp))

                // Actions
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = onDismiss,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E2550)),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("CANCEL", color = Color.LightGray)
                    }

                    Button(
                        onClick = {
                            if (selectedAmount >= 10.0) {
                                onAddConfirmed(selectedAmount, selectedPaymentMethod)
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = WinzoEmerald),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.weight(1.6f)
                    ) {
                        Text(
                            text = "PAY ₹${selectedAmount.toInt()}",
                            fontWeight = FontWeight.Black,
                            color = Color.Black
                        )
                    }
                }
            }
        }
    }
}
