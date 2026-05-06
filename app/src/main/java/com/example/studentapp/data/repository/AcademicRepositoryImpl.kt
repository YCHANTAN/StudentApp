package com.example.studentapp.data.repository

import com.example.studentapp.data.remote.CourseResponse
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
}
