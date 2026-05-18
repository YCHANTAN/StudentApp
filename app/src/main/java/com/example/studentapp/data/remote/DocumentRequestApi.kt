package com.example.studentapp.data.remote

import com.google.gson.annotations.SerializedName
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

data class DocumentRequestResponse(
    @SerializedName("id") val id: String,
    @SerializedName("studentId") val studentId: String,
    @SerializedName("type") val type: String,
    @SerializedName("purpose") val purpose: String,
    @SerializedName("status") val status: String,
    @SerializedName("submittedAt") val submittedAt: String,
    @SerializedName("reference") val reference: String
)

data class CreateDocumentRequestRequest(
    @SerializedName("studentId") val studentId: String,
    @SerializedName("type") val type: String,
    @SerializedName("purpose") val purpose: String,
    @SerializedName("program") val program: String? = null,
    @SerializedName("yearLevel") val yearLevel: String? = null,
    @SerializedName("copies") val copies: Int? = null,
    @SerializedName("deliveryMethod") val deliveryMethod: String? = null
)

interface DocumentRequestApi {
    @GET("document-requests")
    suspend fun getDocumentRequests(
        @Query("studentId") studentId: String
    ): Response<ApiResponse<List<DocumentRequestResponse>>>

    @POST("document-requests")
    suspend fun createDocumentRequest(
        @Body request: CreateDocumentRequestRequest
    ): Response<ApiResponse<DocumentRequestResponse>>
}
