package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pdf_history")
data class PdfHistory(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val fileName: String,
    val filePath: String,
    val fileSize: Long,
    val pageCount: Int,
    val operationType: String, // "CONVERT", "MERGE", "SPLIT", "MODIFY"
    val timestamp: Long = System.currentTimeMillis()
)
