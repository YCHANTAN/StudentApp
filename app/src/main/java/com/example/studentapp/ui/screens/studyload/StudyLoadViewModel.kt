package com.example.studentapp.ui.screens.studyload

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studentapp.domain.repository.AcademicRepository
import com.example.studentapp.domain.repository.AuthRepository
import com.example.studentapp.domain.repository.EnrollmentRepository
import com.example.studentapp.ui.screens.studyload.models.StudyLoadItem
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

class StudyLoadViewModel(
    private val authRepository: AuthRepository = com.example.studentapp.data.repository.AuthRepositoryImpl(),
    private val academicRepository: AcademicRepository = com.example.studentapp.data.repository.AcademicRepositoryImpl(),
    private val enrollmentRepository: EnrollmentRepository = com.example.studentapp.data.repository.EnrollmentRepositoryImpl()
) : ViewModel() {
    var subjects by mutableStateOf<List<StudyLoadItem>>(emptyList())
        private set
    
    var totalUnits by mutableStateOf(0)
        private set
        
    var semesterLabel by mutableStateOf("N/A")
        private set

    var isLoading by mutableStateOf(false)
        private set

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            val profile = authRepository.getProfile()
            if (profile != null) {
                loadStudyLoad(profile.accountId)
            } else {
                semesterLabel = "No Student Profile"
            }
        }
    }

    fun loadStudyLoad(studentId: String) {
        viewModelScope.launch {
            isLoading = true
            try {
                val studyLoad = enrollmentRepository.getStudyLoad(studentId)

                if (studyLoad != null && studyLoad.courses.isNotEmpty()) {
                    subjects = studyLoad.courses
                        .distinctBy { it.code }
                        .map { response ->
                        StudyLoadItem(
                            title = response.title,
                            code = response.code,
                            schedule = response.schedule ?: "TBA",
                            room = response.location ?: "TBA",
                            instructor = response.instructor ?: "TBA",
                            units = response.units ?: 0,
                            status = "Enrolled"
                        )
                    }
                    
                    totalUnits = studyLoad.totalUnits
                    semesterLabel = studyLoad.semesterLabel
                } else {
                    subjects = emptyList()
                    totalUnits = 0
                    semesterLabel = if (studyLoad != null) studyLoad.semesterLabel else "No Active Enrollment"
                }
            } catch (e: Exception) {
                // Handle error state
            } finally {
                isLoading = false
            }
        }
    }

    fun downloadPdf(context: android.content.Context, onComplete: (File?) -> Unit) {
        viewModelScope.launch {
            val profile = authRepository.getProfile()
            if (profile != null) {
                val responseBody = enrollmentRepository.getStudyLoadPdf(profile.id)
                if (responseBody != null) {
                    val file = savePdfToFile(context, responseBody, "study_load_${profile.accountId}.pdf")
                    onComplete(file)
                } else {
                    onComplete(null)
                }
            } else {
                onComplete(null)
            }
        }
    }

    private fun savePdfToFile(context: android.content.Context, body: ResponseBody, fileName: String): File? {
        return try {
            val file = File(context.getExternalFilesDir(null), fileName)
            var inputStream: InputStream? = null
            var outputStream: FileOutputStream? = null
            try {
                inputStream = body.byteStream()
                outputStream = FileOutputStream(file)
                val buffer = ByteArray(4096)
                var bytesRead: Int
                while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                    outputStream.write(buffer, 0, bytesRead)
                }
                outputStream.flush()
                file
            } finally {
                inputStream?.close()
                outputStream?.close()
            }
        } catch (e: Exception) {
            null
        }
    }
}
