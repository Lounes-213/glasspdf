package com.example.ui

import android.app.Application
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.graphics.pdf.PdfDocument
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.ImagePage
import com.example.data.PdfDatabase
import com.example.data.PdfHistory
import com.example.data.PdfHistoryRepository
import com.example.data.PdfPageItem
import com.example.ui.localization.AppLanguage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.util.UUID

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val database = PdfDatabase.getDatabase(application)
    private val repository = PdfHistoryRepository(database.pdfHistoryDao())

    val historyState: StateFlow<List<PdfHistory>> = repository.allHistory
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // General App Settings
    private val _currentLanguage = MutableStateFlow(AppLanguage.EN)
    val currentLanguage: StateFlow<AppLanguage> = _currentLanguage.asStateFlow()

    init {
        val prefs = application.getSharedPreferences("glasspdf_prefs", Context.MODE_PRIVATE)
        val savedLang = prefs.getString("selected_language", null)
        if (savedLang != null) {
            try {
                _currentLanguage.value = AppLanguage.valueOf(savedLang)
            } catch (e: Exception) {
                // Ignore
            }
        }
    }

    private val _currentTab = MutableStateFlow("convert") // "convert", "merge", "split", "modify", "history"
    val currentTab: StateFlow<String> = _currentTab.asStateFlow()

    // Processing & Feedback States
    private val _isProcessing = MutableStateFlow(false)
    val isProcessing: StateFlow<Boolean> = _isProcessing.asStateFlow()

    private val _processingProgress = MutableStateFlow("")
    val processingProgress: StateFlow<String> = _processingProgress.asStateFlow()

    private val _statusMessage = MutableStateFlow<String?>(null)
    val statusMessage: StateFlow<String?> = _statusMessage.asStateFlow()

    private val _isSuccess = MutableStateFlow(true)
    val isSuccess: StateFlow<Boolean> = _isSuccess.asStateFlow()

    // --- CONVERT TAB STATE ---
    private val _selectedImages = MutableStateFlow<List<ImagePage>>(emptyList())
    val selectedImages: StateFlow<List<ImagePage>> = _selectedImages.asStateFlow()

    private val _pdfName = MutableStateFlow("Converted_Images")
    val pdfName: StateFlow<String> = _pdfName.asStateFlow()

    // --- MERGE TAB STATE ---
    private val _selectedPdfUris = MutableStateFlow<List<Uri>>(emptyList())
    val selectedPdfUris: StateFlow<List<Uri>> = _selectedPdfUris.asStateFlow()

    private val _mergePdfName = MutableStateFlow("Merged_Documents")
    val mergePdfName: StateFlow<String> = _mergePdfName.asStateFlow()

    // --- SPLIT TAB STATE ---
    private val _splitPdfUri = MutableStateFlow<Uri?>(null)
    val splitPdfUri: StateFlow<Uri?> = _splitPdfUri.asStateFlow()

    private val _splitPdfName = MutableStateFlow("Split_Document")
    val splitPdfName: StateFlow<String> = _splitPdfName.asStateFlow()

    private val _splitPageRange = MutableStateFlow("")
    val splitPageRange: StateFlow<String> = _splitPageRange.asStateFlow()

    private val _splitPdfTotalPages = MutableStateFlow(0)
    val splitPdfTotalPages: StateFlow<Int> = _splitPdfTotalPages.asStateFlow()

    private val _splitPages = MutableStateFlow<List<PdfPageItem>>(emptyList())
    val splitPages: StateFlow<List<PdfPageItem>> = _splitPages.asStateFlow()

    // --- MODIFY TAB STATE ---
    private val _modifyPdfUri = MutableStateFlow<Uri?>(null)
    val modifyPdfUri: StateFlow<Uri?> = _modifyPdfUri.asStateFlow()

    private val _modifyPdfName = MutableStateFlow("Modified_Document")
    val modifyPdfName: StateFlow<String> = _modifyPdfName.asStateFlow()

    private val _modifyPages = MutableStateFlow<List<PdfPageItem>>(emptyList())
    val modifyPages: StateFlow<List<PdfPageItem>> = _modifyPages.asStateFlow()


    // --- LAST GENERATED STATE ---
    private val _lastGeneratedPdfUri = MutableStateFlow<Uri?>(null)
    val lastGeneratedPdfUri: StateFlow<Uri?> = _lastGeneratedPdfUri.asStateFlow()

    private val _lastGeneratedPdfName = MutableStateFlow("")
    val lastGeneratedPdfName: StateFlow<String> = _lastGeneratedPdfName.asStateFlow()

    private val _lastGeneratedPageCount = MutableStateFlow(0)
    val lastGeneratedPageCount: StateFlow<Int> = _lastGeneratedPageCount.asStateFlow()

    private val _lastGeneratedOperation = MutableStateFlow("")
    val lastGeneratedOperation: StateFlow<String> = _lastGeneratedOperation.asStateFlow()

    private val _showDownloadAnimation = MutableStateFlow(false)
    val showDownloadAnimation: StateFlow<Boolean> = _showDownloadAnimation.asStateFlow()

    private val _hasNewHistoryBadge = MutableStateFlow(false)
    val hasNewHistoryBadge: StateFlow<Boolean> = _hasNewHistoryBadge.asStateFlow()

    fun triggerDownloadAnimation() {
        _showDownloadAnimation.value = true
        _hasNewHistoryBadge.value = true
    }

    fun clearDownloadAnimation() {
        _showDownloadAnimation.value = false
    }

    fun clearHistoryBadge() {
        _hasNewHistoryBadge.value = false
    }

    fun updateSelectedImages(newImages: List<ImagePage>) {
        _selectedImages.value = newImages
    }

    fun updateSelectedPdfUris(newUris: List<Uri>) {
        _selectedPdfUris.value = newUris
    }

    fun updateModifyPages(newPages: List<PdfPageItem>) {
        _modifyPages.value = newPages
    }

    fun clearLastGenerated() {
        _lastGeneratedPdfUri.value = null
        _lastGeneratedPdfName.value = ""
        _lastGeneratedPageCount.value = 0
        _lastGeneratedOperation.value = ""
    }

    fun updatePageBitmap(id: String, newBitmap: Bitmap) {
        _modifyPages.value = _modifyPages.value.map {
            if (it.id == id) {
                it.copy(bitmap = newBitmap)
            } else {
                it
            }
        }
    }

    fun downloadGeneratedPdf(desiredFileName: String) {
        val cacheUri = _lastGeneratedPdfUri.value ?: return
        val operation = _lastGeneratedOperation.value
        val pageCount = _lastGeneratedPageCount.value
        val context = getApplication<Application>()

        viewModelScope.launch {
            _isProcessing.value = true
            _processingProgress.value = "Saving PDF to Downloads..."
            try {
                withContext(Dispatchers.IO) {
                    val cleanName = desiredFileName.trim()
                    val finalName = if (cleanName.lowercase().endsWith(".pdf")) cleanName else "$cleanName.pdf"
                    val inputStream = context.contentResolver.openInputStream(cacheUri)
                    if (inputStream != null) {
                        val savedUri: Uri? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                            val resolver = context.contentResolver
                            val contentValues = ContentValues().apply {
                                put(MediaStore.MediaColumns.DISPLAY_NAME, finalName)
                                put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf")
                                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS + "/PDFConverter")
                            }
                            val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)
                            if (uri != null) {
                                resolver.openOutputStream(uri)?.use { outputStream ->
                                    inputStream.copyTo(outputStream)
                                }
                            }
                            uri
                        } else {
                            val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                            val appDir = File(downloadsDir, "PDFConverter")
                            if (!appDir.exists()) {
                                appDir.mkdirs()
                            }
                            val file = File(appDir, finalName)
                            file.outputStream().use { outputStream ->
                                inputStream.copyTo(outputStream)
                            }
                            Uri.fromFile(file)
                        }

                        inputStream.close()

                        if (savedUri != null) {
                            val fileLength = getFileLengthFromUri(context, savedUri)
                            repository.insert(
                                PdfHistory(
                                    fileName = finalName,
                                    filePath = savedUri.toString(),
                                    fileSize = fileLength,
                                    pageCount = pageCount,
                                    operationType = operation
                                )
                            )
                            withContext(Dispatchers.Main) {
                                showToast("PDF downloaded/saved successfully!", true)
                                triggerDownloadAnimation()
                                _lastGeneratedPdfUri.value = null
                            }
                        } else {
                            withContext(Dispatchers.Main) {
                                showToast("Failed to save PDF", false)
                            }
                        }
                    } else {
                        withContext(Dispatchers.Main) {
                            showToast("Draft file not found", false)
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("PDF_CONVERTER", "Error saving PDF from cache", e)
                showToast("An error occurred: ${e.localizedMessage}", false)
            } finally {
                _isProcessing.value = false
                _processingProgress.value = ""
            }
        }
    }


    // --- LANGUAGE & NAVIGATION ACTIONS ---
    fun selectLanguage(language: AppLanguage) {
        _currentLanguage.value = language
        val prefs = getApplication<Application>().getSharedPreferences("glasspdf_prefs", Context.MODE_PRIVATE)
        prefs.edit().putString("selected_language", language.name).apply()
    }

    fun selectTab(tab: String) {
        _currentTab.value = tab
        if (tab == "history") {
            _hasNewHistoryBadge.value = false
        }
    }

    fun clearStatusMessage() {
        _statusMessage.value = null
    }

    private fun showToast(message: String, success: Boolean) {
        _statusMessage.value = message
        _isSuccess.value = success
    }

    // --- CONVERT METHODS ---
    fun addImages(uris: List<Uri>) {
        val currentList = _selectedImages.value.toMutableList()
        uris.forEach { uri ->
            currentList.add(ImagePage(uri = uri))
        }
        _selectedImages.value = currentList
    }

    fun removeImage(index: Int) {
        val currentList = _selectedImages.value.toMutableList()
        if (index in currentList.indices) {
            currentList.removeAt(index)
            _selectedImages.value = currentList
        }
    }

    fun rotateImage(index: Int) {
        val currentList = _selectedImages.value.toMutableList()
        if (index in currentList.indices) {
            val item = currentList[index]
            val nextRotation = (item.rotation + 90f) % 360f
            currentList[index] = item.copy(rotation = nextRotation)
            _selectedImages.value = currentList
        }
    }

    fun moveImageUp(index: Int) {
        val currentList = _selectedImages.value.toMutableList()
        if (index > 0 && index < currentList.size) {
            val temp = currentList[index]
            currentList[index] = currentList[index - 1]
            currentList[index - 1] = temp
            _selectedImages.value = currentList
        }
    }

    fun moveImageDown(index: Int) {
        val currentList = _selectedImages.value.toMutableList()
        if (index >= 0 && index < currentList.size - 1) {
            val temp = currentList[index]
            currentList[index] = currentList[index + 1]
            currentList[index + 1] = temp
            _selectedImages.value = currentList
        }
    }

    fun updatePdfName(name: String) {
        _pdfName.value = name
    }

    fun clearConvertState() {
        _selectedImages.value = emptyList()
        _pdfName.value = "Converted_Images"
    }

    fun convertImagesToPdf() {
        val images = _selectedImages.value
        val name = _pdfName.value.trim()
        if (images.isEmpty()) {
            showToast("No images selected", false)
            return
        }

        viewModelScope.launch {
            _isProcessing.value = true
            try {
                withContext(Dispatchers.IO) {
                    val pdfDocument = PdfDocument()
                    val context = getApplication<Application>()

                    images.forEachIndexed { index, imgPage ->
                        _processingProgress.value = "Processing image ${index + 1} of ${images.size}..."
                        val rawBitmap = loadScaledBitmap(context, imgPage.uri)
                        if (rawBitmap != null) {
                            val rotatedBitmap = if (imgPage.rotation != 0f) {
                                rotateBitmap(rawBitmap, imgPage.rotation)
                            } else {
                                rawBitmap
                            }

                            val pageInfo = PdfDocument.PageInfo.Builder(
                                rotatedBitmap.width,
                                rotatedBitmap.height,
                                index
                            ).create()
                            val page = pdfDocument.startPage(pageInfo)
                            page.canvas.drawBitmap(rotatedBitmap, 0f, 0f, null)
                            pdfDocument.finishPage(page)

                            // Recycle bitmaps to save native memory
                            if (rotatedBitmap != rawBitmap) {
                                rotatedBitmap.recycle()
                            }
                            rawBitmap.recycle()
                        }
                    }

                    _processingProgress.value = "Generating draft..."
                    val cacheFile = File(context.cacheDir, "draft_convert_${System.currentTimeMillis()}.pdf")
                    cacheFile.outputStream().use { outputStream ->
                        pdfDocument.writeTo(outputStream)
                    }
                    val cacheUri = Uri.fromFile(cacheFile)
                    pdfDocument.close()

                    withContext(Dispatchers.Main) {
                        _lastGeneratedPdfUri.value = cacheUri
                        _lastGeneratedPdfName.value = name
                        _lastGeneratedPageCount.value = images.size
                        _lastGeneratedOperation.value = "CONVERT"
                        showToast("PDF generated! See bottom to preview & save.", true)
                        clearConvertState()
                    }
                }
            } catch (e: Exception) {
                Log.e("PDF_CONVERTER", "Conversion Error", e)
                showToast("An error occurred: ${e.localizedMessage}", false)
            } finally {
                _isProcessing.value = false
                _processingProgress.value = ""
            }
        }
    }


    // --- MERGE METHODS ---
    fun addPdfForMerge(uris: List<Uri>) {
        _selectedPdfUris.value = _selectedPdfUris.value + uris
    }

    fun removePdfFromMerge(index: Int) {
        val list = _selectedPdfUris.value.toMutableList()
        if (index in list.indices) {
            list.removeAt(index)
            _selectedPdfUris.value = list
        }
    }

    fun movePdfUp(index: Int) {
        val list = _selectedPdfUris.value.toMutableList()
        if (index > 0 && index < list.size) {
            val temp = list[index]
            list[index] = list[index - 1]
            list[index - 1] = temp
            _selectedPdfUris.value = list
        }
    }

    fun movePdfDown(index: Int) {
        val list = _selectedPdfUris.value.toMutableList()
        if (index >= 0 && index < list.size - 1) {
            val temp = list[index]
            list[index] = list[index + 1]
            list[index + 1] = temp
            _selectedPdfUris.value = list
        }
    }

    fun updateMergeName(name: String) {
        _mergePdfName.value = name
    }

    fun clearMergeState() {
        _selectedPdfUris.value = emptyList()
        _mergePdfName.value = "Merged_Documents"
    }

    fun mergePdfs() {
        val uris = _selectedPdfUris.value
        val name = _mergePdfName.value.trim()
        if (uris.size < 2) {
            showToast("Please select at least 2 PDFs to merge", false)
            return
        }

        viewModelScope.launch {
            _isProcessing.value = true
            try {
                withContext(Dispatchers.IO) {
                    val pdfDocument = PdfDocument()
                    val context = getApplication<Application>()
                    var pageIndexCounter = 0

                    uris.forEachIndexed { pdfIndex, pdfUri ->
                        _processingProgress.value = "Loading PDF ${pdfIndex + 1} of ${uris.size}..."
                        val parcelFileDescriptor = context.contentResolver.openFileDescriptor(pdfUri, "r")
                        if (parcelFileDescriptor != null) {
                            val renderer = PdfRenderer(parcelFileDescriptor)
                            val pageCount = renderer.pageCount
                            for (i in 0 until pageCount) {
                                _processingProgress.value = "Rendering page ${i + 1}/$pageCount from document ${pdfIndex + 1}..."
                                renderer.openPage(i).use { page ->
                                    val ratio = page.width.toFloat() / page.height.toFloat()
                                    val maxDim = 1200
                                    val destWidth = if (page.width > page.height) maxDim else (maxDim * ratio).toInt()
                                    val destHeight = if (page.width > page.height) (maxDim / ratio).toInt() else maxDim

                                    val bitmap = Bitmap.createBitmap(destWidth, destHeight, Bitmap.Config.ARGB_8888)
                                    val canvas = android.graphics.Canvas(bitmap)
                                    canvas.drawColor(android.graphics.Color.WHITE)
                                    page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)

                                    val pageInfo = PdfDocument.PageInfo.Builder(destWidth, destHeight, pageIndexCounter++).create()
                                    val newPage = pdfDocument.startPage(pageInfo)
                                    newPage.canvas.drawBitmap(bitmap, 0f, 0f, null)
                                    pdfDocument.finishPage(newPage)
                                    bitmap.recycle()
                                }
                            }
                            renderer.close()
                            parcelFileDescriptor.close()
                        }
                    }

                    _processingProgress.value = "Generating merged draft..."
                    val cacheFile = File(context.cacheDir, "draft_merge_${System.currentTimeMillis()}.pdf")
                    cacheFile.outputStream().use { outputStream ->
                        pdfDocument.writeTo(outputStream)
                    }
                    val cacheUri = Uri.fromFile(cacheFile)
                    pdfDocument.close()

                    withContext(Dispatchers.Main) {
                        _lastGeneratedPdfUri.value = cacheUri
                        _lastGeneratedPdfName.value = name
                        _lastGeneratedPageCount.value = pageIndexCounter
                        _lastGeneratedOperation.value = "MERGE"
                        showToast("PDF merged! See bottom to preview & save.", true)
                        clearMergeState()
                    }
                }
            } catch (e: Exception) {
                Log.e("PDF_CONVERTER", "Merge Error", e)
                showToast("An error occurred: ${e.localizedMessage}", false)
            } finally {
                _isProcessing.value = false
                _processingProgress.value = ""
            }
        }
    }


    // --- SPLIT METHODS ---
    fun selectSplitPdf(uri: Uri, fileName: String) {
        _splitPdfUri.value = uri
        // Set default output name based on original name
        val baseName = fileName.substringBeforeLast(".")
        _splitPdfName.value = "${baseName}_split"
        
        viewModelScope.launch {
            _isProcessing.value = true
            _processingProgress.value = "Loading PDF pages..."
            try {
                val context = getApplication<Application>()
                val pages = withContext(Dispatchers.IO) {
                    loadPdfPages(context, uri)
                }
                _splitPages.value = pages
                _splitPdfTotalPages.value = pages.size
            } catch (e: Exception) {
                Log.e("PDF_CONVERTER", "Error reading split PDF metadata", e)
                showToast("An error occurred: ${e.localizedMessage}", false)
            } finally {
                _isProcessing.value = false
                _processingProgress.value = ""
            }
        }
    }

    fun updateSplitPageRange(range: String) {
        _splitPageRange.value = range
    }

    fun updateSplitName(name: String) {
        _splitPdfName.value = name
    }

    fun clearSplitState() {
        _splitPdfUri.value = null
        _splitPageRange.value = ""
        _splitPdfName.value = "Split_Document"
        _splitPdfTotalPages.value = 0
        _splitPages.value = emptyList()
    }

    fun splitPdf() {
        val uri = _splitPdfUri.value ?: return
        val rangeStr = _splitPageRange.value.trim()
        val name = _splitPdfName.value.trim()
        val totalPages = _splitPdfTotalPages.value

        if (rangeStr.isEmpty()) {
            showToast("Invalid page numbers entered", false)
            return
        }

        val targetPages = parsePageRange(rangeStr, totalPages)
        if (targetPages.isEmpty()) {
            showToast("Invalid page numbers entered", false)
            return
        }

        viewModelScope.launch {
            _isProcessing.value = true
            try {
                withContext(Dispatchers.IO) {
                    val pdfDocument = PdfDocument()
                    val context = getApplication<Application>()
                    var outputPageIndex = 0

                    val parcelFileDescriptor = context.contentResolver.openFileDescriptor(uri, "r")
                    if (parcelFileDescriptor != null) {
                        val renderer = PdfRenderer(parcelFileDescriptor)
                        targetPages.forEach { pageIdx ->
                            _processingProgress.value = "Extracting page ${pageIdx + 1}..."
                            renderer.openPage(pageIdx).use { page ->
                                val ratio = page.width.toFloat() / page.height.toFloat()
                                val maxDim = 1200
                                val destWidth = if (page.width > page.height) maxDim else (maxDim * ratio).toInt()
                                val destHeight = if (page.width > page.height) (maxDim / ratio).toInt() else maxDim

                                val bitmap = Bitmap.createBitmap(destWidth, destHeight, Bitmap.Config.ARGB_8888)
                                val canvas = android.graphics.Canvas(bitmap)
                                canvas.drawColor(android.graphics.Color.WHITE)
                                page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)

                                val pageInfo = PdfDocument.PageInfo.Builder(destWidth, destHeight, outputPageIndex++).create()
                                val newPage = pdfDocument.startPage(pageInfo)
                                newPage.canvas.drawBitmap(bitmap, 0f, 0f, null)
                                pdfDocument.finishPage(newPage)
                                bitmap.recycle()
                            }
                        }
                        renderer.close()
                        parcelFileDescriptor.close()
                    }

                    _processingProgress.value = "Generating split draft..."
                    val cacheFile = File(context.cacheDir, "draft_split_${System.currentTimeMillis()}.pdf")
                    cacheFile.outputStream().use { outputStream ->
                        pdfDocument.writeTo(outputStream)
                    }
                    val cacheUri = Uri.fromFile(cacheFile)
                    pdfDocument.close()

                    withContext(Dispatchers.Main) {
                        _lastGeneratedPdfUri.value = cacheUri
                        _lastGeneratedPdfName.value = name
                        _lastGeneratedPageCount.value = targetPages.size
                        _lastGeneratedOperation.value = "SPLIT"
                        showToast("PDF split! See bottom to preview & save.", true)
                        clearSplitState()
                    }
                }
            } catch (e: Exception) {
                Log.e("PDF_CONVERTER", "Split Error", e)
                showToast("An error occurred: ${e.localizedMessage}", false)
            } finally {
                _isProcessing.value = false
                _processingProgress.value = ""
            }
        }
    }


    // --- MODIFY METHODS ---
    fun selectModifyPdf(uri: Uri, fileName: String) {
        _modifyPdfUri.value = uri
        val baseName = fileName.substringBeforeLast(".")
        _modifyPdfName.value = "${baseName}_edited"

        viewModelScope.launch {
            _isProcessing.value = true
            _processingProgress.value = "Loading PDF pages..."
            try {
                val pages = withContext(Dispatchers.IO) {
                    loadPdfPages(getApplication(), uri)
                }
                _modifyPages.value = pages
            } catch (e: Exception) {
                Log.e("PDF_CONVERTER", "Modify load error", e)
                showToast("An error occurred: ${e.localizedMessage}", false)
            } finally {
                _isProcessing.value = false
                _processingProgress.value = ""
            }
        }
    }

    fun removeModifyPage(id: String) {
        _modifyPages.value = _modifyPages.value.filter { it.id != id }
    }

    fun rotateModifyPage(id: String) {
        _modifyPages.value = _modifyPages.value.map {
            if (it.id == id) {
                it.copy(rotation = (it.rotation + 90f) % 360f)
            } else {
                it
            }
        }
    }

    fun moveModifyPageUp(index: Int) {
        val list = _modifyPages.value.toMutableList()
        if (index > 0 && index < list.size) {
            val temp = list[index]
            list[index] = list[index - 1]
            list[index - 1] = temp
            _modifyPages.value = list
        }
    }

    fun moveModifyPageDown(index: Int) {
        val list = _modifyPages.value.toMutableList()
        if (index >= 0 && index < list.size - 1) {
            val temp = list[index]
            list[index] = list[index + 1]
            list[index + 1] = temp
            _modifyPages.value = list
        }
    }

    fun updateModifyPageBitmap(pageId: String, newBitmap: Bitmap) {
        val list = _modifyPages.value.map { item ->
            if (item.id == pageId) {
                item.copy(bitmap = newBitmap)
            } else {
                item
            }
        }
        _modifyPages.value = list
    }

    fun updateModifyName(name: String) {
        _modifyPdfName.value = name
    }

    fun clearModifyState() {
        _modifyPdfUri.value = null
        _modifyPages.value = emptyList()
        _modifyPdfName.value = "Modified_Document"
    }

    fun saveModifiedPdf() {
        val pages = _modifyPages.value
        val name = _modifyPdfName.value.trim()
        if (pages.isEmpty()) {
            showToast("No pages selected", false)
            return
        }

        viewModelScope.launch {
            _isProcessing.value = true
            try {
                withContext(Dispatchers.IO) {
                    val pdfDocument = PdfDocument()
                    val context = getApplication<Application>()

                    pages.forEachIndexed { index, pageItem ->
                        _processingProgress.value = "Saving page ${index + 1} of ${pages.size}..."
                        val rotatedBitmap = if (pageItem.rotation != 0f) {
                            rotateBitmap(pageItem.bitmap, pageItem.rotation)
                        } else {
                            pageItem.bitmap
                        }

                        val pageInfo = PdfDocument.PageInfo.Builder(
                            rotatedBitmap.width,
                            rotatedBitmap.height,
                            index
                        ).create()
                        val page = pdfDocument.startPage(pageInfo)
                        page.canvas.drawBitmap(rotatedBitmap, 0f, 0f, null)
                        pdfDocument.finishPage(page)

                        if (rotatedBitmap != pageItem.bitmap) {
                            rotatedBitmap.recycle()
                        }
                    }

                    _processingProgress.value = "Generating modified draft..."
                    val cacheFile = File(context.cacheDir, "draft_modify_${System.currentTimeMillis()}.pdf")
                    cacheFile.outputStream().use { outputStream ->
                        pdfDocument.writeTo(outputStream)
                    }
                    val cacheUri = Uri.fromFile(cacheFile)
                    pdfDocument.close()

                    withContext(Dispatchers.Main) {
                        _lastGeneratedPdfUri.value = cacheUri
                        _lastGeneratedPdfName.value = name
                        _lastGeneratedPageCount.value = pages.size
                        _lastGeneratedOperation.value = "MODIFY"
                        showToast("PDF modified! See bottom to preview & save.", true)
                        clearModifyState()
                    }
                }
            } catch (e: Exception) {
                Log.e("PDF_CONVERTER", "Modify saving error", e)
                showToast("An error occurred: ${e.localizedMessage}", false)
            } finally {
                _isProcessing.value = false
                _processingProgress.value = ""
            }
        }
    }

    fun deleteHistoryItem(item: PdfHistory) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.delete(item)
            // Attempt to delete physical file if path is Uri
            try {
                val context = getApplication<Application>()
                val uri = Uri.parse(item.filePath)
                context.contentResolver.delete(uri, null, null)
            } catch (e: Exception) {
                Log.e("PDF_CONVERTER", "Could not delete physical file", e)
            }
        }
    }

    fun clearAllHistory() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.clearAll()
        }
    }


    // --- HELPERS ---

    private fun loadScaledBitmap(context: Context, uri: Uri, maxDim: Int = 1800): Bitmap? {
        return try {
            val options = BitmapFactory.Options().apply { inJustDecodeBounds = true }
            context.contentResolver.openInputStream(uri).use { stream ->
                BitmapFactory.decodeStream(stream, null, options)
            }
            val srcWidth = options.outWidth
            val srcHeight = options.outHeight

            var inSampleSize = 1
            while (srcWidth / inSampleSize > maxDim || srcHeight / inSampleSize > maxDim) {
                inSampleSize *= 2
            }

            val decodeOptions = BitmapFactory.Options().apply {
                this.inSampleSize = inSampleSize
            }

            val rawBitmap = context.contentResolver.openInputStream(uri).use { stream ->
                BitmapFactory.decodeStream(stream, null, decodeOptions)
            } ?: return null

            if (rawBitmap.width > maxDim || rawBitmap.height > maxDim) {
                val ratio = rawBitmap.width.toFloat() / rawBitmap.height.toFloat()
                val newWidth = if (rawBitmap.width > rawBitmap.height) maxDim else (maxDim * ratio).toInt()
                val newHeight = if (rawBitmap.width > rawBitmap.height) (maxDim / ratio).toInt() else maxDim
                val scaled = Bitmap.createScaledBitmap(rawBitmap, newWidth, newHeight, true)
                if (scaled != rawBitmap) {
                    rawBitmap.recycle()
                }
                scaled
            } else {
                rawBitmap
            }
        } catch (e: Exception) {
            Log.e("PDF_CONVERTER", "Error decoding bitmap", e)
            null
        }
    }

    private fun rotateBitmap(bitmap: Bitmap, degrees: Float): Bitmap {
        val matrix = Matrix().apply { postRotate(degrees) }
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }

    private fun savePdfToDownloads(context: Context, pdfDocument: PdfDocument, fileName: String): Uri? {
        val finalName = if (fileName.lowercase().endsWith(".pdf")) fileName else "$fileName.pdf"
        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val resolver = context.contentResolver
                val contentValues = ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, finalName)
                    put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf")
                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS + "/PDFConverter")
                }
                val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues) ?: return null
                resolver.openOutputStream(uri)?.use { outputStream ->
                    pdfDocument.writeTo(outputStream)
                }
                uri
            } else {
                val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                val appDir = File(downloadsDir, "PDFConverter")
                if (!appDir.exists()) {
                    appDir.mkdirs()
                }
                val file = File(appDir, finalName)
                file.outputStream().use { outputStream ->
                    pdfDocument.writeTo(outputStream)
                }
                Uri.fromFile(file)
            }
        } catch (e: Exception) {
            Log.e("PDF_CONVERTER", "Error saving PDF file", e)
            null
        }
    }

    fun loadPdfPages(context: Context, pdfUri: Uri, maxDim: Int = 1200): List<PdfPageItem> {
        val pageList = mutableListOf<PdfPageItem>()
        try {
            val contentResolver = context.contentResolver
            val parcelFileDescriptor = contentResolver.openFileDescriptor(pdfUri, "r") ?: return emptyList()
            val renderer = PdfRenderer(parcelFileDescriptor)
            val pageCount = renderer.pageCount

            for (i in 0 until pageCount) {
                renderer.openPage(i).use { page ->
                    val ratio = page.width.toFloat() / page.height.toFloat()
                    val destWidth = if (page.width > page.height) maxDim else (maxDim * ratio).toInt()
                    val destHeight = if (page.width > page.height) (maxDim / ratio).toInt() else maxDim

                    val bitmap = Bitmap.createBitmap(destWidth, destHeight, Bitmap.Config.ARGB_8888)
                    val canvas = android.graphics.Canvas(bitmap)
                    canvas.drawColor(android.graphics.Color.WHITE)

                    page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
                    pageList.add(
                        PdfPageItem(
                            id = UUID.randomUUID().toString(),
                            pageIndex = i,
                            bitmap = bitmap,
                            rotation = 0f
                        )
                    )
                }
            }
            renderer.close()
            parcelFileDescriptor.close()
        } catch (e: Exception) {
            Log.e("PDF_CONVERTER", "Error loading PDF pages", e)
        }
        return pageList
    }

    private fun parsePageRange(rangeStr: String, maxPages: Int): List<Int> {
        val pages = mutableListOf<Int>()
        val clean = rangeStr.replace(" ", "")
        if (clean.isEmpty()) return emptyList()

        val parts = clean.split(",")
        for (part in parts) {
            try {
                if (part.contains("-")) {
                    val subParts = part.split("-")
                    if (subParts.size == 2) {
                        val start = subParts[0].toIntOrNull() ?: continue
                        val end = subParts[1].toIntOrNull() ?: continue
                        val min = kotlin.math.min(start, end)
                        val max = kotlin.math.max(start, end)
                        for (p in min..max) {
                            if (p in 1..maxPages) {
                                pages.add(p - 1)
                            }
                        }
                    }
                } else {
                    val p = part.toIntOrNull() ?: continue
                    if (p in 1..maxPages) {
                        pages.add(p - 1)
                    }
                }
            } catch (e: Exception) {
                Log.e("PDF_CONVERTER", "Error parsing range part: $part", e)
            }
        }
        return pages.distinct().sorted()
    }

    private fun getFileLengthFromUri(context: Context, uri: Uri): Long {
        return try {
            context.contentResolver.openAssetFileDescriptor(uri, "r")?.use { afd ->
                afd.length
            } ?: 0L
        } catch (e: Exception) {
            0L
        }
    }
}
