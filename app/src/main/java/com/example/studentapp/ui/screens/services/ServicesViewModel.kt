package com.example.studentapp.ui.screens.services

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studentapp.domain.repository.AuthRepository
import com.example.studentapp.domain.repository.ComplaintRepository
import com.example.studentapp.domain.repository.DocumentRequestRepository
import com.example.studentapp.ui.screens.services.models.Complaint
import com.example.studentapp.ui.screens.services.models.ComplaintStatus
import com.example.studentapp.ui.screens.services.models.DocumentRequestItem
import kotlinx.coroutines.launch

class ServicesViewModel(
    private val authRepository: AuthRepository = com.example.studentapp.data.repository.AuthRepositoryImpl(),
    private val documentRequestRepository: DocumentRequestRepository = com.example.studentapp.data.repository.DocumentRequestRepositoryImpl(),
    private val complaintRepository: ComplaintRepository = com.example.studentapp.data.repository.ComplaintRepositoryImpl()
) : ViewModel() {
    var documentRequests by mutableStateOf<List<DocumentRequestItem>>(emptyList())
        private set

    var complaints by mutableStateOf<List<Complaint>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
        private set

    init {
        loadServicesData()
    }

    fun loadServicesData() {
        viewModelScope.launch {
            isLoading = true
            val profile = authRepository.getProfile()
            if (profile != null) {
                val studentId = profile.accountId
                
                // Fetch document requests
                val requests = documentRequestRepository.getDocumentRequests(studentId)
                documentRequests = requests.map { response ->
                    DocumentRequestItem(
                        id = response.id,
                        type = response.type,
                        status = response.status,
                        date = response.submittedAt.split("T").firstOrNull() ?: response.submittedAt,
                        reference = response.reference
                    )
                }

                // Fetch complaints
                val complaintResponses = complaintRepository.getComplaints(studentId)
                complaints = complaintResponses.map { response ->
                    Complaint(
                        title = response.title,
                        status = when (response.status.uppercase()) {
                            "IN_REVIEW" -> ComplaintStatus.IN_REVIEW
                            "RESOLVED" -> ComplaintStatus.RESOLVED
                            else -> ComplaintStatus.IN_REVIEW
                        }
                    )
                }
            }
            isLoading = false
        }
    }
}
