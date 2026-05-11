package com.example.studentapp.data.remote

import com.google.gson.annotations.SerializedName
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

data class ComplaintResponse(
    @SerializedName("id") val id: String,
    @SerializedName("studentId") val studentId: String,
    @SerializedName("title") val title: String,
    @SerializedName("status") val status: String,
    @SerializedName("createdAt") val createdAt: String
)

interface ComplaintApi {
    @GET("complaints")
    suspend fun getComplaints(
        @Query("studentId") studentId: String,
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 50
    ): Response<PaginatedResponse<ComplaintResponse>>
}
