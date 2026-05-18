package com.example.studentapp.data.remote

import com.example.studentapp.ui.screens.library.models.LibraryBook
import com.google.gson.annotations.SerializedName
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

data class BorrowRequest(
    @SerializedName("userId") val userId: String
)

data class ReturnRequest(
    @SerializedName("userId") val userId: String
)

interface LibraryApi {
    @GET("library-books")
    suspend fun getLibraryBooks(
        @Query("tab") tab: String? = null,
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 20
    ): Response<ApiResponse<List<LibraryBook>>>

    @POST("library-books/{id}/borrow")
    suspend fun borrowBook(
        @Path("id") bookId: String,
        @Body request: BorrowRequest
    ): Response<ApiResponse<LibraryBook>>

    @POST("library-books/{id}/return")
    suspend fun returnBook(
        @Path("id") bookId: String,
        @Body request: ReturnRequest
    ): Response<ApiResponse<LibraryBook>>

    @GET("library-books/history/{userId}")
    suspend fun getBorrowHistory(
        @Path("userId") userId: String
    ): Response<ApiResponse<List<LibraryBook>>>
}
