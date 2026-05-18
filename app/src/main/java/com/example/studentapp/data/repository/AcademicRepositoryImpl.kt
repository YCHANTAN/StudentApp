package com.example.studentapp.data.repository

import com.example.studentapp.data.remote.CourseResponse
import com.example.studentapp.data.remote.CreateEvaluationRequest
import com.example.studentapp.data.remote.EvaluationResponse
import com.example.studentapp.data.remote.GradeRecordResponse
import com.example.studentapp.data.remote.NetworkModule
import com.example.studentapp.data.remote.ProgramResponse
import com.example.studentapp.data.remote.ScheduleEntryResponse
import com.example.studentapp.domain.repository.AcademicRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AcademicRepositoryImpl : AcademicRepository {
    override suspend fun getPrograms(category: String?): List<ProgramResponse> = withContext(Dispatchers.IO) {
        try {
            val response = NetworkModule.academicApi.getPrograms(category)
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

    override suspend fun getCourses(): List<CourseResponse> = withContext(Dispatchers.IO) {
        try {
            val response = NetworkModule.academicApi.getCourses()
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

    override suspend fun getGradeRecords(studentId: String): List<GradeRecordResponse> = withContext(Dispatchers.IO) {
        try {
            val response = NetworkModule.academicApi.getGradeRecords(studentId)
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

    override suspend fun getScheduleEntries(studentId: String): List<ScheduleEntryResponse> = withContext(Dispatchers.IO) {
        try {
            val response = NetworkModule.academicApi.getScheduleEntries(studentId)
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

    override suspend fun getEvaluations(studentId: String): List<EvaluationResponse> = withContext(Dispatchers.IO) {
        try {
            val response = NetworkModule.academicApi.getEvaluations(studentId)
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

    override suspend fun submitEvaluation(
        studentId: String,
        courseId: String,
        teachingQuality: Int,
        courseMaterials: Int,
        punctuality: Int,
        comments: String?
    ): Boolean = withContext(Dispatchers.IO) {
        try {
            val request = CreateEvaluationRequest(
                studentId,
                courseId,
                teachingQuality,
                courseMaterials,
                punctuality,
                comments
            )
            val response = NetworkModule.academicApi.submitEvaluation(request)
            if (response.isSuccessful) {
                val apiResponse = response.body()
                apiResponse?.success == true
            } else {
                false
            }
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun downloadProspectus(url: String): okhttp3.ResponseBody? = withContext(Dispatchers.IO) {
        try {
            val response = NetworkModule.academicApi.downloadFile(url)
            if (response.isSuccessful) {
                response.body()
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }
}
