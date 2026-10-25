package com.example.data

import android.graphics.Bitmap
import android.net.Uri

data class ImagePage(
    val uri: Uri,
    val rotation: Float = 0f
)

data class PdfPageItem(
    val id: String, // Unique identifier because indices can change on delete/reorder
    val pageIndex: Int,
    val bitmap: Bitmap,
    val rotation: Float = 0f
)
