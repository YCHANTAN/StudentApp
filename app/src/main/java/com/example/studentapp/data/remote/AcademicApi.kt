package com.example.studentapp.data.remote

import com.google.gson.annotations.SerializedName
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

data class GradeRecordResponse(
    @SerializedName("id") val id: String,
    @SerializedName("studentId") val studentId: String,
    @SerializedName("title") val title: String,
    @SerializedName("codeCredits") val codeCredits: String,
    @SerializedName("gradePoint") val gradePoint: String,
    @SerializedName("status") val status: String,
    @SerializedName("semesterLabel") val semesterLabel: String?,
    @SerializedName("remarks") val remarks: String?
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
    @SerializedName("status") val status: String?,
    @SerializedName("tuition") val tuition: Double?,
    @SerializedName("remainingSlots") val remainingSlots: Int?
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

data class EvaluationResponse(
    @SerializedName("id") val id: String,
    @SerializedName("studentId") val studentId: String,
    @SerializedName("courseId") val courseId: String,
    @SerializedName("teachingQuality") val teachingQuality: Int,
    @SerializedName("courseMaterials") val courseMaterials: Int,
    @SerializedName("punctuality") val punctuality: Int,
    @SerializedName("comments") val comments: String?,
    @SerializedName("createdAt") val createdAt: String
)

data class CreateEvaluationRequest(
    @SerializedName("studentId") val studentId: String,
    @SerializedName("courseId") val courseId: String,
    @SerializedName("teachingQuality") val teachingQuality: Int,
    @SerializedName("courseMaterials") val courseMaterials: Int,
    @SerializedName("punctuality") val punctuality: Int,
    @SerializedName("comments") val comments: String?
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

    @GET("evaluations/student/{studentId}")
    suspend fun getEvaluations(
        @Path("studentId") studentId: String
    ): Response<ApiResponse<List<EvaluationResponse>>>

    @retrofit2.http.POST("evaluations")
    suspend fun submitEvaluation(
        @retrofit2.http.Body request: CreateEvaluationRequest
    ): Response<ApiResponse<EvaluationResponse>>
}
