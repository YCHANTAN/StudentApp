package com.example.studentapp.data.remote

import com.google.gson.annotations.SerializedName
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

data class GradeRecordResponse(
    @SerializedName("id") val id: String,
    @SerializedName("studentId") val studentId: String,
    @SerializedName("title") val title: String,
    @SerializedName("codeCredits") val codeCredits: String,
    @SerializedName("gradePoint") val gradePoint: String,
    @SerializedName("status") val status: String,
    @SerializedName("semesterLabel") val semesterLabel: String?
)

data class CourseResponse(
    @SerializedName("id") val id: String,
    @SerializedName("code") val code: String,
    @SerializedName("title") val title: String,
    @SerializedName("semesterTitle") val semesterTitle: String?,
    @SerializedName("instructor") val instructor: String?,
    @SerializedName("units") val units: Int?,
    @SerializedName("schedule") val schedule: String?,
    @SerializedName("location") val location: String?,
    @SerializedName("grade") val grade: String?,
    @SerializedName("waitlistStatus") val waitlistStatus: String?,
    @SerializedName("progress") val progress: Float?,
    @SerializedName("status") val status: String?
)

data class ScheduleEntryResponse(
    @SerializedName("id") val id: String,
    @SerializedName("studentId") val studentId: String,
    @SerializedName("courseCode") val courseCode: String,
    @SerializedName("courseTitle") val courseTitle: String,
    @SerializedName("day") val day: String,
    @SerializedName("timeRange") val timeRange: String,
    @SerializedName("room") val room: String,
    @SerializedName("instructor") val instructor: String
)

data class PaginatedResponse<T>(
    @SerializedName("success") val success: Boolean,
    @SerializedName("data") val data: List<T>,
    @SerializedName("meta") val meta: PaginationMeta
)

data class PaginationMeta(
    @SerializedName("total") val total: Int,
    @SerializedName("page") val page: Int,
    @SerializedName("limit") val limit: Int,
    @SerializedName("totalPages") val totalPages: Int
)

data class ProgramResponse(
    @SerializedName("id") val id: String,
    @SerializedName("title") val title: String,
    @SerializedName("category") val category: String,
    @SerializedName("description") val description: String,
    @SerializedName("scheduleLine") val scheduleLine: String,
    @SerializedName("prospectusUrl") val prospectusUrl: String?
)

interface AcademicApi {
    @GET("programs")
    suspend fun getPrograms(
        @Query("category") category: String? = null,
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 100
    ): Response<PaginatedResponse<ProgramResponse>>

    @GET("courses")
    suspend fun getCourses(
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 100
    ): Response<PaginatedResponse<CourseResponse>>

    @GET("grade-records")
    suspend fun getGradeRecords(
        @Query("studentId") studentId: String,
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 50
    ): Response<PaginatedResponse<GradeRecordResponse>>

    @GET("schedule-entries")
    suspend fun getScheduleEntries(
        @Query("studentId") studentId: String,
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 50
    ): Response<PaginatedResponse<ScheduleEntryResponse>>
}
