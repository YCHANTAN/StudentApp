package com.example.studentapp.domain.repository

import com.example.studentapp.data.remote.AssessmentResponse
import com.example.studentapp.data.remote.PaymentSlipResponse
import com.example.studentapp.data.remote.TransactionResponse

interface FinanceRepository {
    suspend fun getBalance(studentId: String): Double?
    suspend fun getTransactionHistory(studentId: String): List<TransactionResponse>
    suspend fun getAssessment(studentId: String): AssessmentResponse?
    suspend fun getPaymentSlip(studentId: String): PaymentSlipResponse?
}
