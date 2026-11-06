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
import androidx.compose.foundation.shape.CircleShape
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
fun PhoneOtpVerificationDialog(
    onVerifyConfirmed: (phone: String, otp: String) -> Unit,
    onDismiss: () -> Unit
) {
    var phoneNumber by remember { mutableStateOf("9876543210") }
    var otpCode by remember { mutableStateOf("5599") }
    var isOtpSent by remember { mutableStateOf(false) }

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
                    .padding(22.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Shield
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .background(Color(0xFF281E52), CircleShape)
                        .border(2.dp, WinzoEmerald, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text("🛡️", fontSize = 32.sp)
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "PLAYER VERIFICATION (KYC)",
                    color = WinzoGold,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Black
                )

                Text(
                    text = "Verify your mobile number to unlock instant withdrawals and get ₹20 Free Bonus Cash!",
                    color = Color.LightGray,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 4.dp),
                    lineHeight = 16.sp
                )

                Spacer(modifier = Modifier.height(18.dp))

                if (!isOtpSent) {
                    OutlinedTextField(
                        value = phoneNumber,
                        onValueChange = { phoneNumber = it.filter { c -> c.isDigit() }.take(10) },
                        label = { Text("10-Digit Mobile Number", color = Color.LightGray) },
                        prefix = { Text("+91 ", color = WinzoGold, fontWeight = FontWeight.Bold) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = WinzoGold,
                            unfocusedBorderColor = Color(0xFF433470)
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    Button(
                        onClick = {
                            if (phoneNumber.length == 10) {
                                isOtpSent = true
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = WinzoEmerald),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        enabled = phoneNumber.length == 10
                    ) {
                        Text("SEND 4-DIGIT OTP 📨", fontWeight = FontWeight.Black, color = Color.Black)
                    }
                } else {
                    Text(
                        text = "OTP sent to +91 $phoneNumber",
                        color = Color.LightGray,
                        fontSize = 12.sp
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = otpCode,
                        onValueChange = { otpCode = it.filter { c -> c.isDigit() }.take(4) },
                        label = { Text("Enter 4-Digit OTP", color = Color.LightGray) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = WinzoEmerald,
                            unfocusedBorderColor = Color(0xFF433470)
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Demo autofill chip
                    Surface(
                        color = Color(0xFF261D4C),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.clickable { otpCode = "5599" }
                    ) {
                        Text(
                            text = "💡 Auto-fill demo OTP: 5599",
                            color = WinzoGold,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    Button(
                        onClick = {
                            onVerifyConfirmed(phoneNumber, otpCode)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = WinzoEmerald),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        enabled = otpCode.length == 4
                    ) {
                        Text("VERIFY & CLAIM ₹20 BONUS ✓", fontWeight = FontWeight.Black, color = Color.Black)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("CANCEL", color = Color.LightGray, fontSize = 12.sp)
                }
            }
        }
    }
}
