package com.example.studentapp.ui.screens.library.models

import androidx.compose.runtime.Immutable
import com.google.gson.annotations.SerializedName

@Immutable
enum class LibraryTab(val label: String) {
    Available("Available"),
    Return("Return"),
    History("History")
}

@Immutable
enum class StockStatus {
    Available,
    Limited,
    OutOfStock
}

@Immutable
data class LibraryBook(
    @SerializedName("id") val id: String,
    @SerializedName("title") val title: String,
    @SerializedName("author") val author: String,
    @SerializedName("rating") val rating: Double,
    @SerializedName("genre") val genre: String,
    @SerializedName("stockLabel") val stockLabel: String,
    @SerializedName("stockStatus") val stockStatus: StockStatus,
    @SerializedName("isNew") val isNew: Boolean = false,
    @SerializedName("tab") val tab: LibraryTab,
    @SerializedName("availableCopies") val availableCopies: Int = 1,
    @SerializedName("totalCopies") val totalCopies: Int = 1,
    @SerializedName("parentId") val parentId: String? = null
)

@Immutable
data class FilterChip(
    val id: String,
    val label: String,
    val selected: Boolean = false
)
