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
                    prospectusUrl = response.prospectusUrl
                )
            }
            isLoading = false
        }
    }
}
