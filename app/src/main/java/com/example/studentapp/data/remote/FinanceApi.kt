package com.example.studentapp.data.remote

import com.google.gson.annotations.SerializedName
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

data class TransactionResponse(
    @SerializedName("id") val id: String,
    @SerializedName("title") val title: String,
    @SerializedName("type") val type: String,
    @SerializedName("amount") val amount: String,
    @SerializedName("method") val method: String,
    @SerializedName("status") val status: String,
    @SerializedName("referenceId") val referenceId: String,
    @SerializedName("date") val date: String,
    @SerializedName("isPaid") val isPaid: Boolean
)

data class BalanceResponse(
    @SerializedName("studentId") val studentId: String,
    @SerializedName("balance") val balance: Double,
    @SerializedName("currency") val currency: String
)

interface FinanceApi {
    @GET("finance/balance/{studentId}")
    suspend fun getBalance(@Path("studentId") studentId: String): Response<ApiResponse<BalanceResponse>>

    @GET("finance/history/{studentId}")
    suspend fun getTransactionHistory(@Path("studentId") studentId: String): Response<ApiResponse<List<TransactionResponse>>>
}
