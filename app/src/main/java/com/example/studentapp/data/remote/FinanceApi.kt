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

data class SubjectFeeResponse(
    @SerializedName("code") val code: String,
    @SerializedName("title") val title: String,
    @SerializedName("units") val units: Int,
    @SerializedName("tuition") val tuition: Double
)

data class MiscFeeResponse(
    @SerializedName("description") val description: String,
    @SerializedName("amount") val amount: Double
)

data class AssessmentResponse(
    @SerializedName("studentName") val studentName: String,
    @SerializedName("studentId") val studentId: String,
    @SerializedName("program") val program: String,
    @SerializedName("semester") val semester: String,
    @SerializedName("schoolYear") val schoolYear: String,
    @SerializedName("subjects") val subjects: List<SubjectFeeResponse>,
    @SerializedName("totalUnits") val totalUnits: Int,
    @SerializedName("totalTuition") val totalTuition: Double,
    @SerializedName("miscellaneousFees") val miscellaneousFees: List<MiscFeeResponse>,
    @SerializedName("totalAssessment") val totalAssessment: Double
)

data class PaymentSlipResponse(
    @SerializedName("studentName") val studentName: String,
    @SerializedName("studentId") val studentId: String,
    @SerializedName("program") val program: String,
    @SerializedName("semester") val semester: String,
    @SerializedName("schoolYear") val schoolYear: String,
    @SerializedName("subjects") val subjects: List<SubjectFeeResponse>,
    @SerializedName("totalUnits") val totalUnits: Int,
    @SerializedName("totalTuition") val totalTuition: Double,
    @SerializedName("miscellaneousFees") val miscellaneousFees: List<MiscFeeResponse>,
    @SerializedName("totalAssessment") val totalAssessment: Double,
    @SerializedName("totalPaid") val totalPaid: Double,
    @SerializedName("remainingBalance") val remainingBalance: Double,
    @SerializedName("officialReceiptNumber") val officialReceiptNumber: String?,
    @SerializedName("paymentDate") val paymentDate: String?
)

interface FinanceApi {
    @GET("finance/balance/{studentId}")
    suspend fun getBalance(@Path("studentId") studentId: String): Response<ApiResponse<BalanceResponse>>

    @GET("finance/history/{studentId}")
    suspend fun getTransactionHistory(@Path("studentId") studentId: String): Response<ApiResponse<List<TransactionResponse>>>

    @GET("finance/assessment/{studentId}")
    suspend fun getAssessment(@Path("studentId") studentId: String): Response<ApiResponse<AssessmentResponse>>

    @GET("finance/payment-slip/{studentId}")
    suspend fun getPaymentSlip(@Path("studentId") studentId: String): Response<ApiResponse<PaymentSlipResponse>>
}
