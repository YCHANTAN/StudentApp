package com.example.studentapp.ui.screens.finance

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.School
import androidx.compose.ui.graphics.Color
import com.example.studentapp.domain.repository.AuthRepository
import com.example.studentapp.domain.repository.FinanceRepository
import com.example.studentapp.ui.screens.finance.models.Transaction
import kotlinx.coroutines.launch

class FinanceViewModel(
    private val authRepository: AuthRepository = com.example.studentapp.data.repository.AuthRepositoryImpl(),
    private val financeRepository: FinanceRepository = com.example.studentapp.data.repository.FinanceRepositoryImpl()
) : ViewModel() {
    var balance by mutableStateOf(0.0)
        private set
    
    var transactions by mutableStateOf<List<Transaction>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
        private set

    init {
        loadFinanceData()
    }

    fun loadFinanceData() {
        viewModelScope.launch {
            isLoading = true
            val profile = authRepository.getProfile()
            if (profile != null) {
                val studentId = profile.accountId
                balance = financeRepository.getBalance(studentId) ?: 0.0
                val history = financeRepository.getTransactionHistory(studentId)
                
                transactions = history.map { response ->
                    val isFee = response.type.uppercase() == "FEE"
                    Transaction(
                        title = response.title,
                        date = response.date.split("T").firstOrNull() ?: response.date,
                        amount = if (isFee) "-₱${response.amount}" else "+₱${response.amount}",
                        isPaid = response.status.uppercase() == "COMPLETED",
                        icon = if (isFee) Icons.Default.School else Icons.Default.MenuBook,
                        iconBg = if (isFee) Color(0xFFFEF3C7) else Color(0xFFD1FAE5),
                        iconTint = if (isFee) Color(0xFFF8A824) else Color(0xFF059669)
                    )
                }
            }
            isLoading = false
        }
    }
}
