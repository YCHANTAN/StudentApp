package com.example.studentapp.ui.screens.programs

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studentapp.data.repository.AcademicRepositoryImpl
import com.example.studentapp.domain.repository.AcademicRepository
import com.example.studentapp.ui.screens.programs.models.ProgramBadgeVariant
import com.example.studentapp.ui.screens.programs.models.ProgramCategory
import com.example.studentapp.ui.screens.programs.models.ProgramEntry
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

class ProgramsViewModel : ViewModel() {
    private val academicRepository: AcademicRepository = AcademicRepositoryImpl()
    
    var allPrograms by mutableStateOf<List<ProgramEntry>>(emptyList())
        private set
    
    var isLoading by mutableStateOf(false)
        private set

    init {
        loadPrograms()
    }

    fun loadPrograms() {
        viewModelScope.launch {
            isLoading = true
            val programs = academicRepository.getPrograms()
            allPrograms = programs.map { response ->
                ProgramEntry(
                    id = response.id,
                    title = response.title,
                    badgeText = "Enrollment Open", // Default since backend might not have this specific string
                    badgeVariant = ProgramBadgeVariant.Success,
                    scheduleLine = response.scheduleLine,
                    description = response.description,
                    category = if (response.category == "Postgraduate") ProgramCategory.Postgraduate else ProgramCategory.Undergraduate,
                    prospectusUrl = response.prospectusUrl ?: "https://www.w3.org/WAI/ER/tests/xhtml/testfiles/resources/pdf/dummy.pdf"
                )
            }
            isLoading = false
        }
    }

    fun downloadProspectus(context: android.content.Context, entry: ProgramEntry, onComplete: (File?) -> Unit) {
        val url = entry.prospectusUrl ?: return
        viewModelScope.launch {
            val responseBody = academicRepository.downloadProspectus(url)
            if (responseBody != null) {
                val fileName = "prospectus_${entry.title.replace(" ", "_")}.pdf"
                val file = savePdfToFile(context, responseBody, fileName)
                onComplete(file)
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
