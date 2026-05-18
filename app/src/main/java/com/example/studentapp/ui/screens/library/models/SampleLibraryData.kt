package com.example.studentapp.ui.screens.library.models

val sampleLibraryFilters = listOf(
    FilterChip(id = "genre", label = "Genre", selected = true),
    FilterChip(id = "author", label = "Author"),
    FilterChip(id = "year", label = "Year"),
    FilterChip(id = "language", label = "Language")
)

val sampleLibraryBooks = listOf(
    LibraryBook(
        id = "book-1",
        title = "Principles of Modern Design",
        author = "Alex Carter",
        rating = 4.8,
        genre = "Art & Design",
        stockLabel = "2 Copies Available",
        stockStatus = StockStatus.Limited,
        isNew = true,
        tab = LibraryTab.Available,
        availableCopies = 2,
        totalCopies = 2
    ),
    LibraryBook(
        id = "book-2",
        title = "Data Structures & Logic",
        author = "Dr. Sarah J. Miller",
        rating = 4.5,
        genre = "Computer Science",
        stockLabel = "5 Copies Available",
        stockStatus = StockStatus.Available,
        tab = LibraryTab.Available,
        availableCopies = 5,
        totalCopies = 5
    ),
    LibraryBook(
        id = "book-3",
        title = "Macroeconomics 101",
        author = "Marcus Thorne",
        rating = 4.2,
        genre = "Finance",
        stockLabel = "Out of Stock",
        stockStatus = StockStatus.OutOfStock,
        tab = LibraryTab.Available,
        availableCopies = 0,
        totalCopies = 1
    ),
    LibraryBook(
        id = "book-4",
        title = "The Biology of Cells",
        author = "Elena Vance",
        rating = 4.9,
        genre = "Science",
        stockLabel = "Available",
        stockStatus = StockStatus.Available,
        tab = LibraryTab.Available,
        availableCopies = 10,
        totalCopies = 10
    )
)
