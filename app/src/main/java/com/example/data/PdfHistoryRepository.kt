package com.example.data

import kotlinx.coroutines.flow.Flow

class PdfHistoryRepository(private val dao: PdfHistoryDao) {
    val allHistory: Flow<List<PdfHistory>> = dao.getAllHistory()

    suspend fun insert(history: PdfHistory): Long = dao.insertHistory(history)

    suspend fun delete(history: PdfHistory) = dao.deleteHistory(history)

    suspend fun deleteById(id: Int) = dao.deleteHistoryById(id)

    suspend fun clearAll() = dao.clearAll()
}
