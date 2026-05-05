package com.example.studentapp.domain.repository

import com.example.studentapp.data.remote.TransactionResponse

interface FinanceRepository {
    suspend fun getBalance(studentId: String): Double?
    suspend fun getTransactionHistory(studentId: String): List<TransactionResponse>
}
