package com.example.studentapp.data.repository

import com.example.studentapp.data.remote.BorrowRequest
import com.example.studentapp.data.remote.LibraryApi
import com.example.studentapp.data.remote.ReturnRequest
import com.example.studentapp.ui.screens.library.models.LibraryBook

class LibraryRepository(private val api: LibraryApi) {
    suspend fun getLibraryBooks(tab: String? = null) = api.getLibraryBooks(tab)
    suspend fun borrowBook(bookId: String, userId: String) = 
        api.borrowBook(bookId, BorrowRequest(userId))
    suspend fun returnBook(bookId: String, userId: String) = 
        api.returnBook(bookId, ReturnRequest(userId))
    suspend fun getBorrowHistory(userId: String) = api.getBorrowHistory(userId)
}
