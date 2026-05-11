package com.example.studentapp.data.repository

import com.example.studentapp.data.remote.AssessmentResponse
import com.example.studentapp.data.remote.NetworkModule
import com.example.studentapp.data.remote.PaymentSlipResponse
import com.example.studentapp.data.remote.TransactionResponse
import com.example.studentapp.domain.repository.FinanceRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FinanceRepositoryImpl : FinanceRepository {
    override suspend fun getBalance(studentId: String): Double? = withContext(Dispatchers.IO) {
        try {
            val response = NetworkModule.financeApi.getBalance(studentId)
            if (response.isSuccessful) {
                val apiResponse = response.body()
                if (apiResponse?.success == true) {
                    apiResponse.data.balance
                } else {
                    null
                }
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun getTransactionHistory(studentId: String): List<TransactionResponse> = withContext(Dispatchers.IO) {
        try {
            val response = NetworkModule.financeApi.getTransactionHistory(studentId)
            if (response.isSuccessful) {
                val apiResponse = response.body()
                if (apiResponse?.success == true) {
                    apiResponse.data
                } else {
                    emptyList()
                }
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun getAssessment(studentId: String): AssessmentResponse? = withContext(Dispatchers.IO) {
        try {
            val response = NetworkModule.financeApi.getAssessment(studentId)
            if (response.isSuccessful) {
                val apiResponse = response.body()
                if (apiResponse?.success == true) {
                    apiResponse.data
                } else {
                    null
                }
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun getPaymentSlip(studentId: String): PaymentSlipResponse? = withContext(Dispatchers.IO) {
        try {
            val response = NetworkModule.financeApi.getPaymentSlip(studentId)
            if (response.isSuccessful) {
                val apiResponse = response.body()
                if (apiResponse?.success == true) {
                    apiResponse.data
                } else {
                    null
                }
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }
}
