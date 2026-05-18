package com.example.studentapp.domain.repository

import com.example.studentapp.data.remote.CourseResponse
import com.example.studentapp.data.remote.GradeRecordResponse
import com.example.studentapp.data.remote.ProgramResponse
import com.example.studentapp.data.remote.ScheduleEntryResponse
import com.example.studentapp.data.remote.EvaluationResponse

interface AcademicRepository {
    suspend fun getPrograms(category: String? = null): List<ProgramResponse>
    suspend fun getCourses(): List<CourseResponse>
    suspend fun getGradeRecords(studentId: String): List<GradeRecordResponse>
    suspend fun getScheduleEntries(studentId: String): List<ScheduleEntryResponse>
    suspend fun getEvaluations(studentId: String): List<EvaluationResponse>
    suspend fun submitEvaluation(
        studentId: String,
        courseId: String,
        teachingQuality: Int,
        courseMaterials: Int,
        punctuality: Int,
        comments: String?
    ): Boolean
    suspend fun downloadProspectus(url: String): okhttp3.ResponseBody?
}
