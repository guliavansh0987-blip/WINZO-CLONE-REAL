package com.example

import com.example.data.repository.WinzoRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {
    @Test
    fun wallet_deductAndCredit_isCorrect() {
        val repo = WinzoRepository()
        val initialBalance = repo.walletState.value.totalBalance

        // Add Deposit
        repo.addDepositCash(100.0, "Google Pay UPI")
        assertEquals(initialBalance + 100.0, repo.walletState.value.totalBalance, 0.01)

        // Deduct entry fee
        val deducted = repo.deductEntryFee(20.0, "Test Match")
        assertTrue(deducted)

        // Credit winnings
        repo.creditWinnings(38.0, "Test Win")
        assertTrue(repo.walletState.value.winningsCash > 0)

        // Withdraw
        val (withdrawn, _) = repo.withdrawWinnings(25.0, "gamer@upi")
        assertTrue(withdrawn)
    }
}
