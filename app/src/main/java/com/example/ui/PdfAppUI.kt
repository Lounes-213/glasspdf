package com.example.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.offset
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntOffset
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import android.graphics.Bitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import android.util.Log
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import com.example.R
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.CallMerge
import androidx.compose.material.icons.filled.CallSplit
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.MergeType
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.RotateRight
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ZoomIn
import androidx.compose.material3.Button
import androidx.compose.material3.TextButton
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.material.icons.filled.DragHandle
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Language
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.PdfHistory
import com.example.ui.localization.AppLanguage
import com.example.ui.localization.Loc
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// --- TRANS-ROUND GLASS CARD ---
@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 24.dp,
    borderAlpha: Float = 0.2f,
    content: @Composable ColumnScope.() -> Unit
) {
    Box(
        modifier = modifier
            .shadow(
                elevation = 16.dp,
                shape = RoundedCornerShape(cornerRadius),
                clip = false,
                ambientColor = Color.Black.copy(alpha = 0.2f),
                spotColor = Color.Black.copy(alpha = 0.4f)
            )
            .background(
                color = Color(0x19FFFFFF), // white with 10% opacity for premium iOS glassmorphism
                shape = RoundedCornerShape(cornerRadius)
            )
            .border(
                width = 1.dp,
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.White.copy(alpha = borderAlpha),
                        Color.White.copy(alpha = borderAlpha * 0.2f)
                    )
                ),
                shape = RoundedCornerShape(cornerRadius)
            )
            .padding(18.dp)
    ) {
        Column {
            content()
        }
    }
}

// --- COSMIC AMBIENT BACKDROP ---
@Composable
fun CosmicGlassBackground(modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition(label = "backdrop_anim")

    val x1 by infiniteTransition.animateFloat(
        initialValue = 0.1f,
        targetValue = 0.9f,
        animationSpec = infiniteRepeatable(tween(16000, easing = LinearEasing), RepeatMode.Reverse),
        label = "x1"
    )
    val y1 by infiniteTransition.animateFloat(
        initialValue = 0.15f,
        targetValue = 0.85f,
        animationSpec = infiniteRepeatable(tween(20000, easing = LinearEasing), RepeatMode.Reverse),
        label = "y1"
    )
    val x2 by infiniteTransition.animateFloat(
        initialValue = 0.9f,
        targetValue = 0.1f,
        animationSpec = infiniteRepeatable(tween(18000, easing = LinearEasing), RepeatMode.Reverse),
        label = "x2"
    )
    val y2 by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 0.2f,
        animationSpec = infiniteRepeatable(tween(14000, easing = LinearEasing), RepeatMode.Reverse),
        label = "y2"
    )

    Canvas(modifier = modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height

        // Dark Apple iOS Space Base (#0F0F1A)
        drawRect(color = Color(0xFF0F0F1A))

        // Glowing Blue Orb (bg-blue-600/30)
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(Color(0xFF2563EB).copy(alpha = 0.35f), Color.Transparent),
                center = Offset(w * x1, h * y1),
                radius = w * 0.8f
            ),
            radius = w * 0.8f,
            center = Offset(w * x1, h * y1)
        )

        // Glowing Purple Orb (bg-purple-600/20)
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(Color(0xFF9333EA).copy(alpha = 0.25f), Color.Transparent),
                center = Offset(w * x2, h * y2),
                radius = w * 0.7f
            ),
            radius = w * 0.7f,
            center = Offset(w * x2, h * y2)
        )

        // Glowing Emerald Orb (bg-emerald-500/10)
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(Color(0xFF10B981).copy(alpha = 0.15f), Color.Transparent),
                center = Offset(w * (1f - x1), h * (1f - y2)),
                radius = w * 0.6f
            ),
            radius = w * 0.6f,
            center = Offset(w * (1f - x1), h * (1f - y2))
        )
    }
}

// --- DATA TYPES ---
data class TabItem(
    val id: String,
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)

// --- MAIN CONTAINER ---
@Composable
fun PdfAppUI(viewModel: MainViewModel) {
    val currentTab by viewModel.currentTab.collectAsState()
    val currentLang by viewModel.currentLanguage.collectAsState()
    val isProcessing by viewModel.isProcessing.collectAsState()
    val progressMessage by viewModel.processingProgress.collectAsState()
    val statusMessage by viewModel.statusMessage.collectAsState()
    val isSuccess by viewModel.isSuccess.collectAsState()

    val lastGeneratedPdfUri by viewModel.lastGeneratedPdfUri.collectAsState()
    val lastGeneratedPdfName by viewModel.lastGeneratedPdfName.collectAsState()
    val lastGeneratedOperation by viewModel.lastGeneratedOperation.collectAsState()

    val showDownloadAnimation by viewModel.showDownloadAnimation.collectAsState()
    val hasNewHistoryBadge by viewModel.hasNewHistoryBadge.collectAsState()

    var showDownloadDialog by remember { mutableStateOf(false) }
    var previewPdfUri by remember { mutableStateOf<Uri?>(null) }
    var showHelpDialog by remember { mutableStateOf(false) }

    val context = LocalContext.current

    // Set RTL layout direction if language is Arabic
    val direction = if (currentLang.isRtl) LayoutDirection.Rtl else LayoutDirection.Ltr

    androidx.compose.runtime.CompositionLocalProvider(
        LocalLayoutDirection provides direction
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Background
            CosmicGlassBackground()

            Scaffold(
                containerColor = Color.Transparent,
                bottomBar = {
                    val tabs = listOf(
                        TabItem("convert", Loc.get("tabConvert", currentLang), Icons.Filled.Image, Icons.Filled.Image),
                        TabItem("merge", Loc.get("tabMerge", currentLang), Icons.Filled.CallMerge, Icons.Filled.CallMerge),
                        TabItem("split", Loc.get("tabSplit", currentLang), Icons.Filled.CallSplit, Icons.Filled.CallSplit),
                        TabItem("modify", Loc.get("tabModify", currentLang), Icons.Filled.Edit, Icons.Filled.Edit),
                        TabItem("history", Loc.get("tabHistory", currentLang), Icons.Filled.History, Icons.Filled.History)
                    )
                    GlassBottomBar(
                        tabs = tabs,
                        currentTab = currentTab,
                        hasNewHistoryBadge = hasNewHistoryBadge,
                        onTabSelected = { viewModel.selectTab(it) }
                    )
                }
            ) { innerPadding ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = innerPadding.calculateBottomPadding())
                        .statusBarsPadding()
                ) {
                    // Header Bar
                    HeaderBar(
                        currentLang = currentLang,
                        onLanguageSelected = { viewModel.selectLanguage(it) },
                        onHelpClick = { showHelpDialog = true }
                    )

                    // Tab Contents
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    ) {
                        when (currentTab) {
                            "convert" -> ConvertScreen(viewModel, currentLang)
                            "merge" -> MergeScreen(viewModel, currentLang)
                            "split" -> SplitScreen(viewModel, currentLang)
                            "modify" -> ModifyScreen(viewModel, currentLang)
                            "history" -> HistoryScreen(viewModel, currentLang)
                        }
                    }
                }
            }

            // Glass Processing Overlay
            AnimatedVisibility(
                visible = isProcessing,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.65f))
                        .clickable(enabled = false) {},
                    contentAlignment = Alignment.Center
                ) {
                    GlassCard(
                        modifier = Modifier
                            .width(280.dp)
                            .padding(16.dp),
                        cornerRadius = 28.dp
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            CircularProgressIndicator(
                                color = Color(0xFF00C6FF),
                                strokeWidth = 4.dp,
                                modifier = Modifier.size(52.dp)
                            )
                            Spacer(modifier = Modifier.height(20.dp))
                            Text(
                                text = Loc.get("processing", currentLang),
                                style = MaterialTheme.typography.titleMedium,
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            )
                            if (progressMessage.isNotEmpty()) {
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = progressMessage,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color.White.copy(alpha = 0.7f),
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }
            }

            // Slide-in Glass Toast
            AnimatedVisibility(
                visible = statusMessage != null,
                enter = slideInVertically(initialOffsetY = { -it }) + fadeIn(),
                exit = slideOutVertically(targetOffsetY = { -it }) + fadeOut(),
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .statusBarsPadding()
                    .padding(top = 12.dp)
            ) {
                statusMessage?.let { msg ->
                    val toastBg = if (isSuccess) Color(0xCE0F3316) else Color(0xCE4E1818)
                    val toastBorder = if (isSuccess) Color(0x4300FF22) else Color(0x43FF3B30)
                    val displayMsg = if (isSuccess) Loc.get("statusSuccess", currentLang) else "${Loc.get("statusError", currentLang)} $msg"

                    LaunchedEffect(msg) {
                        kotlinx.coroutines.delay(4000)
                        viewModel.clearStatusMessage()
                    }

                    Box(
                        modifier = Modifier
                            .padding(horizontal = 24.dp)
                            .shadow(12.dp, RoundedCornerShape(20.dp), clip = false)
                            .background(toastBg, RoundedCornerShape(20.dp))
                            .border(1.dp, toastBorder, RoundedCornerShape(20.dp))
                            .padding(horizontal = 16.dp, vertical = 12.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = if (isSuccess) Icons.Filled.Info else Icons.Filled.Close,
                                contentDescription = null,
                                tint = if (isSuccess) Color(0xFF00FF66) else Color(0xFFFF4D4D),
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = displayMsg,
                                color = Color.White,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }

            // Centered dialog for generated PDF draft (instead of a bottom banner)
            if (lastGeneratedPdfUri != null) {
                GeneratedPdfCenterDialog(
                    pdfName = lastGeneratedPdfName,
                    operation = lastGeneratedOperation,
                    onPreview = { previewPdfUri = lastGeneratedPdfUri },
                    onDownload = { showDownloadDialog = true },
                    onDismiss = { viewModel.clearLastGenerated() }
                )
            }

            // Developer Help & Privacy Dialog
            if (showHelpDialog) {
                HelpPrivacyDialog(
                    onDismiss = { showHelpDialog = false }
                )
            }

            if (previewPdfUri != null) {
                PdfPagesPreviewDialog(
                    pdfUri = previewPdfUri!!,
                    viewModel = viewModel,
                    onDismiss = { previewPdfUri = null }
                )
            }

            if (showDownloadDialog && lastGeneratedPdfUri != null) {
                DownloadFileNameDialog(
                    initialName = lastGeneratedPdfName,
                    onDismiss = { showDownloadDialog = false },
                    onSave = { name ->
                        viewModel.downloadGeneratedPdf(name)
                        showDownloadDialog = false
                    }
                )
            }

            // Beautiful flying animation to the bottom navigation bar
            FlyingDownloadAnimation(
                visible = showDownloadAnimation,
                onAnimationEnd = { viewModel.clearDownloadAnimation() }
            )
        }
    }
}

// --- HEADER BAR COMPONENT ---
@Composable
fun HeaderBar(
    currentLang: AppLanguage,
    onLanguageSelected: (AppLanguage) -> Unit,
    onHelpClick: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // App Identity with Custom Logo and Red 2026 Badge
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(Color(0xFF3B82F6), Color(0xFF8B5CF6))
                        ),
                        shape = RoundedCornerShape(10.dp)
                    )
                    .border(1.dp, Color.White.copy(alpha = 0.2f), RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "PDF",
                    color = Color.White,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Black
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "GlassPDF",
                        color = Color.White,
                        fontSize = 19.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 0.5.sp
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    // Small "2026" in a translucent glass badge without frame
                    Box(
                        modifier = Modifier
                            .background(Color.White.copy(alpha = 0.12f), RoundedCornerShape(6.dp))
                            .padding(horizontal = 5.dp, vertical = 1.5.dp)
                    ) {
                        Text(
                            text = "2026",
                            color = Color(0xFF00C6FF), // Neon cyan fits glass-morphic theme wonderfully
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 0.5.sp
                        )
                    }
                }
                Text(
                    text = "Professional Offline Editor",
                    color = Color.White.copy(alpha = 0.5f),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            // Help/About question button
            IconButton(
                onClick = onHelpClick,
                modifier = Modifier
                    .size(40.dp)
                    .background(Color(0x1A00C6FF), CircleShape)
                    .border(1.dp, Color(0xFF00C6FF).copy(alpha = 0.3f), CircleShape)
            ) {
                Text(
                    text = "?",
                    color = Color(0xFF00C6FF),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.ExtraBold,
                    modifier = Modifier.padding(bottom = 1.dp) // optical centering
                )
            }
            Spacer(modifier = Modifier.width(22.dp))

            // Language Select Button
            Box {
                IconButton(
                    onClick = { expanded = true },
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color(0x22FFFFFF), CircleShape)
                        .border(1.dp, Color.White.copy(alpha = 0.1f), CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Language,
                        contentDescription = Loc.get("language", currentLang),
                        tint = Color.White
                    )
                }

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier
                        .background(Color(0xFF161529))
                        .border(1.dp, Color.White.copy(alpha = 0.18f), RoundedCornerShape(16.dp))
                ) {
                    AppLanguage.values().forEach { lang ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = lang.displayName,
                                    color = if (currentLang == lang) Color(0xFF3B82F6) else Color.White
                                )
                            },
                            onClick = {
                                onLanguageSelected(lang)
                                expanded = false
                            }
                        )
                    }
                }
            }
        }
    }
}

// --- BOTTOM NAVIGATION BAR ---
@Composable
fun GlassBottomBar(
    tabs: List<TabItem>,
    currentTab: String,
    hasNewHistoryBadge: Boolean,
    onTabSelected: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, bottom = 12.dp)
            .windowInsetsPadding(WindowInsets.navigationBars)
            .shadow(20.dp, RoundedCornerShape(32.dp), clip = false)
            .background(Color(0x22FFFFFF), RoundedCornerShape(32.dp)) // bg-white/10 equivalent
            .border(
                width = 1.dp,
                brush = Brush.verticalGradient(
                    listOf(Color.White.copy(alpha = 0.2f), Color.White.copy(alpha = 0.05f))
                ),
                shape = RoundedCornerShape(32.dp)
            )
            .padding(6.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            tabs.forEach { tab ->
                val isSelected = currentTab == tab.id
                val scale by animateFloatAsState(
                    targetValue = if (isSelected) 1.1f else 1.0f,
                    animationSpec = spring(dampingRatio = 0.55f)
                )
                val color = if (isSelected) Color(0xFF3B82F6) else Color.White.copy(alpha = 0.45f) // IOS blue active tab color

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(20.dp))
                        .clickable { onTabSelected(tab.id) }
                        .padding(vertical = 10.dp)
                        .graphicsLayer(scaleX = scale, scaleY = scale),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(contentAlignment = Alignment.TopEnd) {
                        Icon(
                            imageVector = tab.selectedIcon,
                            contentDescription = tab.title,
                            tint = color,
                            modifier = Modifier.size(24.dp)
                        )
                        if (tab.id == "history" && hasNewHistoryBadge) {
                            Box(
                                modifier = Modifier
                                    .offset(x = 4.dp, y = (-4).dp)
                                    .size(9.dp)
                                    .background(Color(0xFFFF3B30), CircleShape)
                                    .border(1.5.dp, Color(0xFF131124), CircleShape)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = tab.title,
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                        color = color,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

// --- CONVERT TAB SCREEN ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConvertScreen(viewModel: MainViewModel, currentLang: AppLanguage) {
    val selectedImages by viewModel.selectedImages.collectAsState()
    val pdfName by viewModel.pdfName.collectAsState()
    val context = LocalContext.current

    var previewImageUri by remember { mutableStateOf<Uri?>(null) }
    var previewImageRotation by remember { mutableStateOf(0f) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents()
    ) { uris ->
        if (uris.isNotEmpty()) {
            viewModel.addImages(uris)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
    ) {
        GlassCard(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = Loc.get("tabConvert", currentLang),
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = Loc.get("reorderTip", currentLang),
                        color = Color.White.copy(alpha = 0.5f),
                        fontSize = 11.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                // Glassy compact Import Button
                Button(
                    onClick = { launcher.launch("image/*") },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0.08f)),
                    border = BorderStroke(1.dp, Color.White.copy(alpha = 0.15f)),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.height(34.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = "Import Images",
                            tint = Color(0xFF3B82F6),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Import", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            if (selectedImages.isEmpty()) {
                // Empty State
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .clickable { launcher.launch("image/*") },
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.img_pdf_empty_state),
                        contentDescription = "Empty State Illustration",
                        modifier = Modifier
                            .size(130.dp)
                            .clip(RoundedCornerShape(24.dp))
                            .border(1.dp, Color.White.copy(alpha = 0.15f), RoundedCornerShape(24.dp)),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = Loc.get("selectImages", currentLang),
                        color = Color(0xFF3B82F6),
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            } else {
                // Images Grid
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(bottom = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    itemsIndexed(selectedImages) { index, imgPage ->
                        var dragOffset by remember { mutableStateOf(0f) }
                        Box(
                            modifier = Modifier
                                .size(110.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .border(1.dp, Color.White.copy(alpha = 0.15f), RoundedCornerShape(16.dp))
                                .background(Color(0x33000000))
                                .offset(x = (dragOffset / LocalDensity.current.density).dp)
                                .pointerInput(Unit) {
                                    detectHorizontalDragGestures(
                                        onDragEnd = { dragOffset = 0f },
                                        onDragCancel = { dragOffset = 0f },
                                        onHorizontalDrag = { change, dragAmount ->
                                            change.consume()
                                            dragOffset += dragAmount
                                            val thresholdPx = 150f
                                            if (dragOffset > thresholdPx) {
                                                if (index < selectedImages.size - 1) {
                                                    viewModel.moveImageDown(index)
                                                    dragOffset = 0f
                                                }
                                            } else if (dragOffset < -thresholdPx) {
                                                if (index > 0) {
                                                    viewModel.moveImageUp(index)
                                                    dragOffset = 0f
                                                }
                                            }
                                        }
                                    )
                                }
                        ) {
                            AsyncImage(
                                model = imgPage.uri,
                                contentDescription = null,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .rotate(imgPage.rotation)
                                    .clickable {
                                        previewImageUri = imgPage.uri
                                        previewImageRotation = imgPage.rotation
                                    },
                                contentScale = ContentScale.Crop
                            )

                            // Order badge (top-left) - highly visible and glowing
                            Box(
                                modifier = Modifier
                                    .align(Alignment.TopStart)
                                    .padding(4.dp)
                                    .background(Color(0xFF00C6FF), RoundedCornerShape(6.dp))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "#${index + 1}",
                                    color = Color.Black,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Black
                                )
                            }

                            // Quick Rotate overlay badge (top right)
                            Box(
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .padding(4.dp)
                                    .size(24.dp)
                                    .background(Color.Black.copy(alpha = 0.6f), CircleShape)
                                    .clickable { viewModel.rotateImage(index) },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.RotateRight,
                                    contentDescription = Loc.get("rotate", currentLang),
                                    tint = Color.White,
                                    modifier = Modifier.size(14.dp)
                                )
                            }

                            // Quick Delete overlay badge (bottom right)
                            Box(
                                modifier = Modifier
                                    .align(Alignment.BottomEnd)
                                    .padding(4.dp)
                                    .size(24.dp)
                                    .background(Color(0xFFEE2D2D).copy(alpha = 0.85f), CircleShape)
                                    .clickable { viewModel.removeImage(index) },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Delete,
                                    contentDescription = Loc.get("remove", currentLang),
                                    tint = Color.White,
                                    modifier = Modifier.size(14.dp)
                                )
                            }

                            // Horizontal Drag Hint Handle (bottom left)
                            Box(
                                modifier = Modifier
                                    .align(Alignment.BottomStart)
                                    .padding(4.dp)
                                    .size(24.dp)
                                    .background(Color.Black.copy(alpha = 0.6f), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.SwapHoriz,
                                    contentDescription = "Drag horizontal to reorder",
                                    tint = Color(0xFF00C6FF),
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                        }
                    }
                    item {
                        // Glassy small "Add more" tile inside the grid
                        Box(
                            modifier = Modifier
                                .size(110.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color.White.copy(alpha = 0.08f))
                                .border(1.dp, Color.White.copy(alpha = 0.12f), RoundedCornerShape(16.dp))
                                .clickable { launcher.launch("image/*") },
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    imageVector = Icons.Filled.Add,
                                    contentDescription = "Add More Images",
                                    tint = Color(0xFF3B82F6),
                                    modifier = Modifier.size(26.dp)
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Add",
                                    color = Color.White.copy(alpha = 0.8f),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }
            }

            if (previewImageUri != null) {
                FullScreenImageDialog(
                    imageUri = previewImageUri,
                    rotation = previewImageRotation,
                    onDismiss = { previewImageUri = null }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Conversion Settings Panel
        AnimatedVisibility(visible = selectedImages.isNotEmpty()) {
            GlassCard(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = pdfName,
                    onValueChange = { viewModel.updatePdfName(it) },
                    label = { Text(Loc.get("placeholderName", currentLang), color = Color.White.copy(alpha = 0.6f)) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF3B82F6),
                        unfocusedBorderColor = Color.White.copy(alpha = 0.2f),
                        focusedLabelColor = Color(0xFF3B82F6),
                        unfocusedLabelColor = Color.White.copy(alpha = 0.6f),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    ),
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("pdf_name_input"),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                ) {
                    // Clear Btn
                    Button(
                        onClick = { viewModel.clearConvertState() },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0x22FFFFFF)),
                        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.1f)),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight(),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text(Loc.get("clearBtn", currentLang), color = Color.White, fontSize = 13.sp)
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    // Action Btn (Dynamic Gradient Glow)
                    Button(
                        onClick = { viewModel.convertImagesToPdf() },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .weight(2.0f)
                            .fillMaxHeight()
                            .testTag("generate_pdf_button")
                            .clip(RoundedCornerShape(12.dp))
                            .background(
                                brush = Brush.horizontalGradient(listOf(Color(0xFF2563EB), Color(0xFF8B5CF6))),
                                shape = RoundedCornerShape(12.dp)
                            ),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Icon(Icons.Filled.PictureAsPdf, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(Loc.get("convertBtn", currentLang), color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

// --- MERGE TAB SCREEN ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MergeScreen(viewModel: MainViewModel, currentLang: AppLanguage) {
    val selectedPdfUris by viewModel.selectedPdfUris.collectAsState()
    val mergePdfName by viewModel.mergePdfName.collectAsState()
    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents()
    ) { uris ->
        if (uris.isNotEmpty()) {
            viewModel.addPdfForMerge(uris)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
    ) {
        GlassCard(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = Loc.get("tabMerge", currentLang),
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Combine multiple PDFs together",
                        color = Color.White.copy(alpha = 0.5f),
                        fontSize = 11.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                // Glassy compact Import Button
                Button(
                    onClick = { launcher.launch("application/pdf") },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0.08f)),
                    border = BorderStroke(1.dp, Color.White.copy(alpha = 0.15f)),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.height(34.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = "Import PDFs",
                            tint = Color(0xFF3B82F6),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Import", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            if (selectedPdfUris.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .clickable { launcher.launch("application/pdf") },
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.img_pdf_empty_state),
                        contentDescription = "Empty State Illustration",
                        modifier = Modifier
                            .size(130.dp)
                            .clip(RoundedCornerShape(24.dp))
                            .border(1.dp, Color.White.copy(alpha = 0.15f), RoundedCornerShape(24.dp)),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = Loc.get("selectPdfs", currentLang),
                        color = Color(0xFF3B82F6),
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(bottom = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    itemsIndexed(selectedPdfUris) { index, uri ->
                        var dragOffset by remember { mutableStateOf(0f) }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .offset(y = (dragOffset / LocalDensity.current.density).dp)
                                .background(Color(0x1F000000), RoundedCornerShape(14.dp))
                                .border(1.dp, Color.White.copy(alpha = 0.08f), RoundedCornerShape(14.dp))
                                .padding(horizontal = 12.dp, vertical = 10.dp)
                                .pointerInput(Unit) {
                                    detectVerticalDragGestures(
                                        onDragEnd = { dragOffset = 0f },
                                        onDragCancel = { dragOffset = 0f },
                                        onVerticalDrag = { change, dragAmount ->
                                            change.consume()
                                            dragOffset += dragAmount
                                            val thresholdPx = 120f
                                            if (dragOffset > thresholdPx) {
                                                if (index < selectedPdfUris.size - 1) {
                                                    viewModel.movePdfDown(index)
                                                    dragOffset = 0f
                                                }
                                            } else if (dragOffset < -thresholdPx) {
                                                if (index > 0) {
                                                    viewModel.movePdfUp(index)
                                                    dragOffset = 0f
                                                }
                                            }
                                        }
                                    )
                                },
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Order badge - highly visible and beautiful
                            Box(
                                modifier = Modifier
                                    .background(Color(0xFF00C6FF), RoundedCornerShape(8.dp))
                                    .padding(horizontal = 8.dp, vertical = 4.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "#${index + 1}",
                                    color = Color.Black,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Black
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))

                            Icon(
                                imageVector = Icons.Filled.PictureAsPdf,
                                contentDescription = null,
                                tint = Color(0xFFFF4949),
                                modifier = Modifier.size(32.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = getFileNameFromUri(context, uri) ?: "Document_${index + 1}.pdf",
                                color = Color.White,
                                style = MaterialTheme.typography.bodyMedium,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.weight(1f)
                            )
                            
                            // Reorder Drag Hint Handle (vertical scroll indicator)
                            Icon(
                                imageVector = Icons.Filled.SwapVert,
                                contentDescription = "Drag up/down to reorder",
                                tint = Color(0xFF00C6FF),
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))

                            IconButton(onClick = { viewModel.removePdfFromMerge(index) }) {
                                Icon(
                                    imageVector = Icons.Filled.Close,
                                    contentDescription = Loc.get("remove", currentLang),
                                    tint = Color.White.copy(alpha = 0.6f)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
                // Add more PDF button
                Button(
                    onClick = { launcher.launch("application/pdf") },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0x1A3B82F6)),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.Add, contentDescription = null, tint = Color(0xFF3B82F6))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Add PDF", color = Color(0xFF3B82F6))
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Actions (Only show when at least 2 PDFs are loaded)
        AnimatedVisibility(visible = selectedPdfUris.size >= 2) {
            GlassCard(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = mergePdfName,
                    onValueChange = { viewModel.updateMergeName(it) },
                    label = { Text(Loc.get("placeholderName", currentLang), color = Color.White.copy(alpha = 0.6f)) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF3B82F6),
                        unfocusedBorderColor = Color.White.copy(alpha = 0.2f),
                        focusedLabelColor = Color(0xFF3B82F6),
                        unfocusedLabelColor = Color.White.copy(alpha = 0.6f),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    ),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                ) {
                    Button(
                        onClick = { viewModel.clearMergeState() },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0x22FFFFFF)),
                        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.1f)),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .weight(1.0f)
                            .fillMaxHeight(),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text(Loc.get("clearBtn", currentLang), color = Color.White, fontSize = 13.sp)
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Button(
                        onClick = { viewModel.mergePdfs() },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .weight(2.0f)
                            .fillMaxHeight()
                            .clip(RoundedCornerShape(12.dp))
                            .background(
                                brush = Brush.horizontalGradient(listOf(Color(0xFF2563EB), Color(0xFF8B5CF6))),
                                shape = RoundedCornerShape(12.dp)
                            ),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Icon(Icons.Filled.CallMerge, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(Loc.get("mergeBtn", currentLang), color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

// --- SPLIT TAB SCREEN ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SplitScreen(viewModel: MainViewModel, currentLang: AppLanguage) {
    val splitPdfUri by viewModel.splitPdfUri.collectAsState()
    val splitPdfName by viewModel.splitPdfName.collectAsState()
    val splitPageRange by viewModel.splitPageRange.collectAsState()
    val splitPdfTotalPages by viewModel.splitPdfTotalPages.collectAsState()

    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            val name = getFileNameFromUri(context, it) ?: "Document.pdf"
            viewModel.selectSplitPdf(it, name)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
    ) {
        GlassCard(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = Loc.get("tabSplit", currentLang),
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Extract specific pages from a PDF",
                        color = Color.White.copy(alpha = 0.5f),
                        fontSize = 11.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                // Glassy compact Import Button
                Button(
                    onClick = { launcher.launch("application/pdf") },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0.08f)),
                    border = BorderStroke(1.dp, Color.White.copy(alpha = 0.15f)),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.height(34.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = "Import PDF to Split",
                            tint = Color(0xFF3B82F6),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Import", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            if (splitPdfUri == null) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .clickable { launcher.launch("application/pdf") },
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.img_pdf_empty_state),
                        contentDescription = "Empty State Illustration",
                        modifier = Modifier
                            .size(130.dp)
                            .clip(RoundedCornerShape(24.dp))
                            .border(1.dp, Color.White.copy(alpha = 0.15f), RoundedCornerShape(24.dp)),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = Loc.get("selectPdfToSplit", currentLang),
                        color = Color(0xFF3B82F6),
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            } else {
                // Loaded state: Show a beautiful grid of PDF pages for visual selection
                val splitPages by viewModel.splitPages.collectAsState()
                var previewPageBitmap by remember { mutableStateOf<Bitmap?>(null) }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    // Header metadata & change button
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White.copy(alpha = 0.05f), RoundedCornerShape(12.dp))
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = getFileNameFromUri(context, splitPdfUri!!) ?: "Document.pdf",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = "${Loc.get("pagesTotal", currentLang)} $splitPdfTotalPages",
                                color = Color.White.copy(alpha = 0.5f),
                                fontSize = 11.sp
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        TextButton(
                            onClick = { viewModel.clearSplitState() },
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp),
                            modifier = Modifier.height(28.dp)
                        ) {
                            Text("Clear", color = Color(0xFFFF4949), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Tap pages to select/deselect them for extraction:",
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Parse selected indices from splitPageRange text
                    val selectedIndices = remember(splitPageRange, splitPdfTotalPages) {
                        parsePageRange(splitPageRange, splitPdfTotalPages)
                    }

                    if (splitPages.isEmpty()) {
                        // Loading indicator if pages are being converted to bitmaps
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = Color(0xFF00C6FF))
                        }
                    } else {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(3),
                            modifier = Modifier.weight(1f),
                            contentPadding = PaddingValues(bottom = 8.dp),
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            itemsIndexed(splitPages) { index, pageItem ->
                                val pageNum = index + 1
                                val isSelected = selectedIndices.contains(pageNum)
                                val borderCol = if (isSelected) Color(0xFF00C6FF) else Color.White.copy(alpha = 0.15f)
                                val borderThickness = if (isSelected) 3.dp else 1.dp

                                Box(
                                    modifier = Modifier
                                        .aspectRatio(0.72f)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(Color(0x33000000))
                                        .border(borderThickness, borderCol, RoundedCornerShape(12.dp))
                                        .clickable {
                                            val newSet = selectedIndices.toMutableSet()
                                            if (newSet.contains(pageNum)) {
                                                newSet.remove(pageNum)
                                            } else {
                                                newSet.add(pageNum)
                                            }
                                            viewModel.updateSplitPageRange(formatPageRange(newSet))
                                        }
                                ) {
                                    Image(
                                        bitmap = pageItem.bitmap.asImageBitmap(),
                                        contentDescription = "Page $pageNum",
                                        modifier = Modifier.fillMaxSize(),
                                        contentScale = ContentScale.Fit
                                    )

                                    // Page Number indicator (bottom center badge)
                                    Box(
                                        modifier = Modifier
                                            .align(Alignment.BottomCenter)
                                            .fillMaxWidth()
                                            .background(Color.Black.copy(alpha = 0.6f))
                                            .padding(vertical = 4.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "Page $pageNum",
                                            color = Color.White,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }

                                    // Selection badge overlay (top right)
                                    Box(
                                        modifier = Modifier
                                            .align(Alignment.TopEnd)
                                            .padding(6.dp)
                                            .size(20.dp)
                                            .background(
                                                color = if (isSelected) Color(0xFF00C6FF) else Color.Black.copy(alpha = 0.5f),
                                                shape = CircleShape
                                            )
                                            .border(1.dp, Color.White.copy(alpha = 0.3f), CircleShape),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        if (isSelected) {
                                            Icon(
                                                imageVector = Icons.Filled.Check,
                                                contentDescription = "Selected",
                                                tint = Color.Black,
                                                modifier = Modifier.size(12.dp)
                                            )
                                        }
                                    }

                                    // Magnify button overlay (top left)
                                    Box(
                                        modifier = Modifier
                                            .align(Alignment.TopStart)
                                            .padding(6.dp)
                                            .size(20.dp)
                                            .background(Color.Black.copy(alpha = 0.5f), CircleShape)
                                            .clickable { previewPageBitmap = pageItem.bitmap },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Filled.ZoomIn,
                                            contentDescription = "Zoom Page",
                                            tint = Color.White,
                                            modifier = Modifier.size(12.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }

                    if (previewPageBitmap != null) {
                        FullScreenImageDialog(
                            imageUri = null,
                            imageBitmap = previewPageBitmap,
                            onDismiss = { previewPageBitmap = null }
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Configuration Card
        AnimatedVisibility(visible = splitPdfUri != null) {
            GlassCard(modifier = Modifier.fillMaxWidth()) {
                // Pages to extract field
                OutlinedTextField(
                    value = splitPageRange,
                    onValueChange = { viewModel.updateSplitPageRange(it) },
                    label = { Text(Loc.get("pagesRange", currentLang), color = Color.White.copy(alpha = 0.6f)) },
                    placeholder = { Text("e.g. 1-3, 5", color = Color.White.copy(alpha = 0.3f)) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF3B82F6),
                        unfocusedBorderColor = Color.White.copy(alpha = 0.2f),
                        focusedLabelColor = Color(0xFF3B82F6),
                        unfocusedLabelColor = Color.White.copy(alpha = 0.6f),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    ),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Output File Name
                OutlinedTextField(
                    value = splitPdfName,
                    onValueChange = { viewModel.updateSplitName(it) },
                    label = { Text(Loc.get("placeholderName", currentLang), color = Color.White.copy(alpha = 0.6f)) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF3B82F6),
                        unfocusedBorderColor = Color.White.copy(alpha = 0.2f),
                        focusedLabelColor = Color(0xFF3B82F6),
                        unfocusedLabelColor = Color.White.copy(alpha = 0.6f),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    ),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { viewModel.splitPdf() },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            brush = Brush.horizontalGradient(listOf(Color(0xFF2563EB), Color(0xFF8B5CF6))),
                            shape = RoundedCornerShape(12.dp)
                        ),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Icon(Icons.Filled.CallSplit, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(Loc.get("splitBtn", currentLang), color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

// --- MODIFY TAB SCREEN ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModifyScreen(viewModel: MainViewModel, currentLang: AppLanguage) {
    val modifyPdfUri by viewModel.modifyPdfUri.collectAsState()
    val modifyPdfName by viewModel.modifyPdfName.collectAsState()
    val modifyPages by viewModel.modifyPages.collectAsState()

    var editingPage by remember { mutableStateOf<com.example.data.PdfPageItem?>(null) }

    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            val name = getFileNameFromUri(context, it) ?: "Document.pdf"
            viewModel.selectModifyPdf(it, name)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
    ) {
        GlassCard(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = Loc.get("tabModify", currentLang),
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = Loc.get("reorderTip", currentLang),
                        color = Color.White.copy(alpha = 0.5f),
                        fontSize = 11.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                // Glassy compact Import Button
                Button(
                    onClick = { launcher.launch("application/pdf") },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0.08f)),
                    border = BorderStroke(1.dp, Color.White.copy(alpha = 0.15f)),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.height(34.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = "Import PDF to Modify",
                            tint = Color(0xFF3B82F6),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Import", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            if (modifyPdfUri == null) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .clickable { launcher.launch("application/pdf") },
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.img_pdf_empty_state),
                        contentDescription = "Empty State Illustration",
                        modifier = Modifier
                            .size(130.dp)
                            .clip(RoundedCornerShape(24.dp))
                            .border(1.dp, Color.White.copy(alpha = 0.15f), RoundedCornerShape(24.dp)),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = Loc.get("selectPdfToModify", currentLang),
                        color = Color(0xFF3B82F6),
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            } else {
                // Pages Layout
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(bottom = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    itemsIndexed(modifyPages, key = { _, page -> page.id }) { index, page ->
                        var dragOffset by remember { mutableStateOf(0f) }
                        Box(
                            modifier = Modifier
                                .height(160.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .border(1.dp, Color.White.copy(alpha = 0.1f), RoundedCornerShape(16.dp))
                                .background(Color(0x33000000))
                                .offset(x = (dragOffset / LocalDensity.current.density).dp)
                                .pointerInput(Unit) {
                                    detectHorizontalDragGestures(
                                        onDragEnd = { dragOffset = 0f },
                                        onDragCancel = { dragOffset = 0f },
                                        onHorizontalDrag = { change, dragAmount ->
                                            change.consume()
                                            dragOffset += dragAmount
                                            val thresholdPx = 150f
                                            if (dragOffset > thresholdPx) {
                                                if (index < modifyPages.size - 1) {
                                                    viewModel.moveModifyPageDown(index)
                                                    dragOffset = 0f
                                                }
                                            } else if (dragOffset < -thresholdPx) {
                                                if (index > 0) {
                                                    viewModel.moveModifyPageUp(index)
                                                    dragOffset = 0f
                                                }
                                            }
                                        }
                                    )
                                }
                                .clickable { editingPage = page }
                        ) {
                            // Thumbnail Page
                            Canvas(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .rotate(page.rotation)
                            ) {
                                drawImage(page.bitmap.asImageBitmap())
                            }

                            // Write overlay indicator (Center)
                            Box(
                                modifier = Modifier
                                    .align(Alignment.Center)
                                    .background(Color.Black.copy(alpha = 0.65f), RoundedCornerShape(12.dp))
                                    .padding(horizontal = 10.dp, vertical = 6.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Filled.Edit, contentDescription = null, tint = Color(0xFF00C6FF), modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Write / Edit", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                            }

                            // Glowing Page Order Badge (Bottom Center)
                            Box(
                                modifier = Modifier
                                    .align(Alignment.BottomCenter)
                                    .fillMaxWidth()
                                    .background(Color.Black.copy(alpha = 0.65f))
                                    .padding(vertical = 4.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Filled.SwapHoriz,
                                        contentDescription = null,
                                        tint = Color(0xFF00C6FF),
                                        modifier = Modifier.size(12.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "Page ${index + 1}",
                                        color = Color.White,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Black
                                    )
                                }
                            }

                            // Delete Action Badge (Top Right)
                            Box(
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .padding(6.dp)
                                    .size(24.dp)
                                    .background(Color(0xFFEE2D2D).copy(alpha = 0.85f), CircleShape)
                                    .clickable { viewModel.removeModifyPage(page.id) },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Delete,
                                    contentDescription = Loc.get("remove", currentLang),
                                    tint = Color.White,
                                    modifier = Modifier.size(14.dp)
                                )
                            }

                            // Rotate Action Badge (Top Left)
                            Box(
                                modifier = Modifier
                                    .align(Alignment.TopStart)
                                    .padding(6.dp)
                                    .size(24.dp)
                                    .background(Color.Black.copy(alpha = 0.6f), CircleShape)
                                    .clickable { viewModel.rotateModifyPage(page.id) },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.RotateRight,
                                    contentDescription = Loc.get("rotate", currentLang),
                                    tint = Color.White,
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Save Modifications Panel
        AnimatedVisibility(visible = modifyPdfUri != null) {
            GlassCard(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = modifyPdfName,
                    onValueChange = { viewModel.updateModifyName(it) },
                    label = { Text(Loc.get("placeholderName", currentLang), color = Color.White.copy(alpha = 0.6f)) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF3B82F6),
                        unfocusedBorderColor = Color.White.copy(alpha = 0.2f),
                        focusedLabelColor = Color(0xFF3B82F6),
                        unfocusedLabelColor = Color.White.copy(alpha = 0.6f),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    ),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                ) {
                    Button(
                        onClick = { viewModel.clearModifyState() },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0x22FFFFFF)),
                        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.1f)),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .weight(1.0f)
                            .fillMaxHeight(),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text(Loc.get("clearBtn", currentLang), color = Color.White, fontSize = 13.sp)
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Button(
                        onClick = { viewModel.saveModifiedPdf() },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .weight(2.0f)
                            .fillMaxHeight()
                            .clip(RoundedCornerShape(12.dp))
                            .background(
                                brush = Brush.horizontalGradient(listOf(Color(0xFF2563EB), Color(0xFF8B5CF6))),
                                shape = RoundedCornerShape(12.dp)
                            ),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Icon(Icons.Filled.PictureAsPdf, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(Loc.get("modifyBtn", currentLang), color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
    }

    if (editingPage != null) {
        PageEditorDialog(
            page = editingPage!!,
            onDismiss = { editingPage = null },
            onSave = { updatedBitmap ->
                viewModel.updateModifyPageBitmap(editingPage!!.id, updatedBitmap)
                editingPage = null
            }
        )
    }
}

// --- HISTORY TAB SCREEN ---
@Composable
fun HistoryScreen(viewModel: MainViewModel, currentLang: AppLanguage) {
    val history by viewModel.historyState.collectAsState()
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = Loc.get("historyTitle", currentLang),
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            if (history.isNotEmpty()) {
                IconButton(
                    onClick = { viewModel.clearAllHistory() },
                    modifier = Modifier
                        .size(36.dp)
                        .background(Color(0x11FFFFFF), CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Refresh,
                        contentDescription = "Clear All History",
                        tint = Color.White.copy(alpha = 0.7f),
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (history.isEmpty()) {
            // Empty State
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Image(
                        painter = painterResource(id = R.drawable.img_pdf_empty_state),
                        contentDescription = "Empty State Illustration",
                        modifier = Modifier
                            .size(130.dp)
                            .clip(RoundedCornerShape(24.dp))
                            .border(1.dp, Color.White.copy(alpha = 0.15f), RoundedCornerShape(24.dp)),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = Loc.get("noHistory", currentLang),
                        color = Color.White.copy(alpha = 0.5f),
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 24.dp)
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(bottom = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(history, key = { it.id }) { item ->
                    HistoryCard(
                        item = item,
                        currentLang = currentLang,
                        onShare = { sharePdf(context, item.filePath) },
                        onDelete = { viewModel.deleteHistoryItem(item) },
                        onOpen = { openPdf(context, item.filePath) }
                    )
                }
            }
        }
    }
}

// --- HISTORY ITEM CARD ---
@Composable
fun HistoryCard(
    item: PdfHistory,
    currentLang: AppLanguage,
    onShare: () -> Unit,
    onDelete: () -> Unit,
    onOpen: () -> Unit
) {
    val dateString = remember(item.timestamp) {
        val sdf = SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault())
        sdf.format(Date(item.timestamp))
    }

    val typeColor = when (item.operationType) {
        "CONVERT" -> Color(0xFF00FF66)
        "MERGE" -> Color(0xFF00C6FF)
        "SPLIT" -> Color(0xFFFF9500)
        "MODIFY" -> Color(0xFFAF52DE)
        else -> Color.White
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(8.dp, RoundedCornerShape(24.dp), clip = false)
            .background(Color(0x11FFFFFF), RoundedCornerShape(24.dp))
            .border(1.dp, Color.White.copy(alpha = 0.12f), RoundedCornerShape(24.dp))
            .clickable { onOpen() }
            .padding(14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Filled.PictureAsPdf,
                contentDescription = null,
                tint = Color(0xFFFF3B30),
                modifier = Modifier.size(36.dp)
            )

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.fileName,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(3.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .background(typeColor.copy(alpha = 0.15f), RoundedCornerShape(6.dp))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = item.operationType,
                            color = typeColor,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "${item.pageCount} ${Loc.get("pageCount", currentLang)} • ${formatFileSize(item.fileSize)}",
                        color = Color.White.copy(alpha = 0.5f),
                        fontSize = 11.sp
                    )
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = dateString,
                    color = Color.White.copy(alpha = 0.35f),
                    fontSize = 10.sp
                )
            }

            // Action Row
            Row {
                IconButton(onClick = onShare) {
                    Icon(
                        imageVector = Icons.Filled.Share,
                        contentDescription = Loc.get("share", currentLang),
                        tint = Color(0xFF3B82F6),
                        modifier = Modifier.size(18.dp)
                    )
                }
                IconButton(onClick = onDelete) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = Loc.get("delete", currentLang),
                        tint = Color(0xFFFF4D4D),
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}


// --- URI HELPERS ---
fun getFileNameFromUri(context: Context, uri: Uri): String? {
    var name: String? = null
    try {
        context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
            val index = cursor.getColumnIndex(android.provider.OpenableColumns.DISPLAY_NAME)
            if (index != -1 && cursor.moveToFirst()) {
                name = cursor.getString(index)
            }
        }
    } catch (e: Exception) {
        Log.e("PDF_CONVERTER", "Error querying file name", e)
    }
    return name ?: uri.lastPathSegment
}

fun formatFileSize(bytes: Long): String {
    if (bytes <= 0) return "0 B"
    val units = listOf("B", "KB", "MB", "GB")
    val digitGroups = (Math.log10(bytes.toDouble()) / Math.log10(1024.0)).toInt()
    if (digitGroups !in units.indices) return "$bytes B"
    return String.format("%.1f %s", bytes / Math.pow(1024.0, digitGroups.toDouble()), units[digitGroups])
}

fun sharePdf(context: Context, uriString: String) {
    try {
        val uri = Uri.parse(uriString)
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "application/pdf"
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        context.startActivity(Intent.createChooser(intent, "Share PDF"))
    } catch (e: Exception) {
        Log.e("PDF_CONVERTER", "Share failed", e)
        Toast.makeText(context, "Could not share file", Toast.LENGTH_SHORT).show()
    }
}

fun openPdf(context: Context, uriString: String) {
    try {
        val uri = Uri.parse(uriString)
        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(uri, "application/pdf")
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    } catch (e: Exception) {
        Log.e("PDF_CONVERTER", "Open failed", e)
        Toast.makeText(context, "No PDF reader application found", Toast.LENGTH_SHORT).show()
    }
}


// --- DRAFT/PREVIEW DIALOGS ---

@Composable
fun PdfPagesPreviewDialog(
    pdfUri: Uri,
    viewModel: MainViewModel,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    var pages by remember { mutableStateOf<List<com.example.data.PdfPageItem>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(pdfUri) {
        val loaded = withContext(Dispatchers.IO) {
            viewModel.loadPdfPages(context, pdfUri, maxDim = 1000)
        }
        pages = loaded
        isLoading = false
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.92f))
                .statusBarsPadding()
        ) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .background(Color.Black.copy(alpha = 0.4f))
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "PDF Preview",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Filled.Close, contentDescription = "Close", tint = Color.White)
                }
            }

            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = Color(0xFF3B82F6)
                )
            } else if (pages.isEmpty()) {
                Text(
                    text = "Could not load preview pages",
                    color = Color.White,
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 60.dp)
                        .padding(horizontal = 16.dp),
                    contentPadding = PaddingValues(vertical = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    itemsIndexed(pages) { index, page ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(page.bitmap.width.toFloat() / page.bitmap.height.toFloat())
                                .shadow(8.dp, RoundedCornerShape(8.dp))
                                .background(Color.White, RoundedCornerShape(8.dp))
                                .padding(4.dp)
                        ) {
                            Image(
                                bitmap = page.bitmap.asImageBitmap(),
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Fit
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Page ${index + 1} of ${pages.size}",
                            color = Color.White.copy(alpha = 0.6f),
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DownloadFileNameDialog(
    initialName: String,
    onDismiss: () -> Unit,
    onSave: (String) -> Unit
) {
    var enteredName by remember { mutableStateOf(initialName) }

    Dialog(onDismissRequest = onDismiss) {
        GlassCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            cornerRadius = 24.dp
        ) {
            Text(
                text = "💾 Save to Downloads",
                color = Color.White,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = enteredName,
                onValueChange = { enteredName = it },
                label = { Text("File Name", color = Color.White.copy(alpha = 0.6f)) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF3B82F6),
                    unfocusedBorderColor = Color.White.copy(alpha = 0.2f),
                    focusedLabelColor = Color(0xFF3B82F6),
                    unfocusedLabelColor = Color.White.copy(alpha = 0.6f),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                ),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(20.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0.1f))
                ) {
                    Text("Cancel", color = Color.White)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = {
                        if (enteredName.trim().isNotEmpty()) {
                            onSave(enteredName)
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3B82F6))
                ) {
                    Text("Save", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}


// --- INTERACTIVE WRITER/EDITOR CANVAS FOR PDF PAGES ---

data class DrawPath(
    val points: List<Offset>,
    val color: Color,
    val strokeWidth: Float
)

data class TextElement(
    val id: String = java.util.UUID.randomUUID().toString(),
    var text: String,
    var position: Offset,
    var color: Color = Color.Black,
    var fontSize: Float = 24f
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PageEditorDialog(
    page: com.example.data.PdfPageItem,
    onDismiss: () -> Unit,
    onSave: (Bitmap) -> Unit
) {
    val context = LocalContext.current
    var isPenMode by remember { mutableStateOf(true) }
    var selectedColor by remember { mutableStateOf(Color.Red) }
    var selectedStrokeWidth by remember { mutableStateOf(8f) }
    
    // Drawing lines state
    var drawPaths by remember { mutableStateOf(listOf<DrawPath>()) }
    var currentPathPoints by remember { mutableStateOf(listOf<Offset>()) }
    
    // Text elements state
    var textElements by remember { mutableStateOf(listOf<TextElement>()) }
    var showAddTextPrompt by remember { mutableStateOf(false) }
    var textInput by remember { mutableStateOf("") }
    
    // Display size to map coordinates back to actual bitmap
    var displayWidth by remember { mutableStateOf(1f) }
    var displayHeight by remember { mutableStateOf(1f) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF0F0F1A)) // matching Dark Cosmic style
                .statusBarsPadding()
        ) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .background(Color.Black.copy(alpha = 0.4f))
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Filled.Close, contentDescription = "Close", tint = Color.White)
                }
                Text(
                    text = "Page Writer & Editor PRO",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Button(
                    onClick = {
                        // Apply edits to Bitmap
                        val resultBitmap = page.bitmap.copy(Bitmap.Config.ARGB_8888, true)
                        val canvas = android.graphics.Canvas(resultBitmap)
                        
                        // Scale factors
                        val scaleX = resultBitmap.width.toFloat() / displayWidth
                        val scaleY = resultBitmap.height.toFloat() / displayHeight
                        
                        // Draw lines
                        val paint = android.graphics.Paint().apply {
                            isAntiAlias = true
                            style = android.graphics.Paint.Style.STROKE
                            strokeCap = android.graphics.Paint.Cap.ROUND
                            strokeJoin = android.graphics.Paint.Join.ROUND
                        }
                        for (path in drawPaths) {
                            paint.color = path.color.toArgb()
                            paint.strokeWidth = path.strokeWidth * scaleX // relative to scale
                            val nativePath = android.graphics.Path()
                            if (path.points.isNotEmpty()) {
                                nativePath.moveTo(path.points.first().x * scaleX, path.points.first().y * scaleY)
                                for (i in 1 until path.points.size) {
                                    nativePath.lineTo(path.points[i].x * scaleX, path.points[i].y * scaleY)
                                }
                                canvas.drawPath(nativePath, paint)
                            }
                        }
                        
                        // Draw texts
                        val textPaint = android.graphics.Paint().apply {
                            isAntiAlias = true
                            style = android.graphics.Paint.Style.FILL
                            typeface = android.graphics.Typeface.DEFAULT_BOLD
                        }
                        for (textElem in textElements) {
                            textPaint.color = textElem.color.toArgb()
                            textPaint.textSize = textElem.fontSize * scaleX
                            canvas.drawText(
                                textElem.text,
                                textElem.position.x * scaleX,
                                textElem.position.y * scaleY,
                                textPaint
                            )
                        }
                        
                        onSave(resultBitmap)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00C6FF))
                ) {
                    Text("Apply", color = Color.Black, fontWeight = FontWeight.Bold)
                }
            }

            // Canvas & Preview Area
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 60.dp, bottom = 100.dp)
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                // Interactive Canvas Container
                Box(
                    modifier = Modifier
                        .aspectRatio(page.bitmap.width.toFloat() / page.bitmap.height.toFloat())
                        .clip(RoundedCornerShape(8.dp))
                        .border(1.dp, Color.White.copy(alpha = 0.2f), RoundedCornerShape(8.dp))
                        .background(Color.White)
                        .onSizeChanged { size ->
                            displayWidth = size.width.toFloat()
                            displayHeight = size.height.toFloat()
                        }
                        .pointerInput(isPenMode) {
                            if (isPenMode) {
                                detectDragGestures(
                                    onDragStart = { offset ->
                                        currentPathPoints = listOf(offset)
                                    },
                                    onDrag = { change, dragAmount ->
                                        change.consume()
                                        currentPathPoints = currentPathPoints + change.position
                                    },
                                    onDragEnd = {
                                        if (currentPathPoints.isNotEmpty()) {
                                            drawPaths = drawPaths + DrawPath(
                                                points = currentPathPoints,
                                                color = selectedColor,
                                                strokeWidth = selectedStrokeWidth
                                            )
                                            currentPathPoints = emptyList()
                                        }
                                    }
                                )
                            }
                        }
                ) {
                    // Original Bitmap Page
                    Image(
                        bitmap = page.bitmap.asImageBitmap(),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Fit
                    )

                    // Draw Path overlays
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        // Existing paths
                        for (path in drawPaths) {
                            val pathObj = androidx.compose.ui.graphics.Path()
                            if (path.points.isNotEmpty()) {
                                pathObj.moveTo(path.points.first().x, path.points.first().y)
                                for (i in 1 until path.points.size) {
                                    pathObj.lineTo(path.points[i].x, path.points[i].y)
                                }
                                drawPath(
                                    path = pathObj,
                                    color = path.color,
                                    style = androidx.compose.ui.graphics.drawscope.Stroke(
                                        width = path.strokeWidth,
                                        cap = androidx.compose.ui.graphics.StrokeCap.Round,
                                        join = androidx.compose.ui.graphics.StrokeJoin.Round
                                    )
                                )
                            }
                        }

                        // Current drawing path
                        if (currentPathPoints.isNotEmpty()) {
                            val pathObj = androidx.compose.ui.graphics.Path()
                            pathObj.moveTo(currentPathPoints.first().x, currentPathPoints.first().y)
                            for (i in 1 until currentPathPoints.size) {
                                pathObj.lineTo(currentPathPoints[i].x, currentPathPoints[i].y)
                            }
                            drawPath(
                                path = pathObj,
                                color = selectedColor,
                                style = androidx.compose.ui.graphics.drawscope.Stroke(
                                    width = selectedStrokeWidth,
                                    cap = androidx.compose.ui.graphics.StrokeCap.Round,
                                    join = androidx.compose.ui.graphics.StrokeJoin.Round
                                )
                            )
                        }
                    }

                    // Drag-and-drop Text Element Overlays
                    for (textElem in textElements) {
                        var offsetState by remember(textElem.id) { mutableStateOf(textElem.position) }

                        Text(
                            text = textElem.text,
                            color = textElem.color,
                            fontWeight = FontWeight.Bold,
                            fontSize = (textElem.fontSize / displayWidth * 300f).sp, // visually proportional
                            modifier = Modifier
                                .offset {
                                    androidx.compose.ui.unit.IntOffset(
                                        offsetState.x.toInt(),
                                        offsetState.y.toInt()
                                    )
                                }
                                .pointerInput(textElem.id) {
                                    detectDragGestures { change, dragAmount ->
                                        change.consume()
                                        val newOffset = offsetState + dragAmount
                                        offsetState = newOffset
                                        textElem.position = newOffset
                                    }
                                }
                                .background(Color.Black.copy(alpha = 0.2f), RoundedCornerShape(4.dp))
                                .padding(horizontal = 4.dp, vertical = 2.dp)
                        )
                    }
                }
            }

            // Bottom toolbar / Color picker
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .background(Color.Black.copy(alpha = 0.6f))
                    .padding(16.dp)
            ) {
                // Tools and controls row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Pen toggle
                    Button(
                        onClick = { isPenMode = true },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isPenMode) Color(0xFF3B82F6) else Color.White.copy(alpha = 0.08f)
                        ),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Filled.Edit, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Draw", fontSize = 12.sp)
                        }
                    }

                    // Text addition button
                    Button(
                        onClick = { showAddTextPrompt = true },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White.copy(alpha = 0.08f)
                        ),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Filled.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Add Text", fontSize = 12.sp)
                        }
                    }

                    // Brush sizes
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        listOf(4f, 8f, 16f).forEach { sizeVal ->
                            val isSel = selectedStrokeWidth == sizeVal
                            Box(
                                modifier = Modifier
                                    .size(if (isSel) 28.dp else 22.dp)
                                    .clip(CircleShape)
                                    .background(if (isSel) Color(0xFF00C6FF) else Color.White.copy(alpha = 0.2f))
                                    .clickable { selectedStrokeWidth = sizeVal },
                                contentAlignment = Alignment.Center
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(sizeVal.dp / 2f)
                                        .clip(CircleShape)
                                        .background(Color.White)
                                )
                            }
                        }
                    }

                    // Eraser / Clear Button
                    IconButton(
                        onClick = {
                            drawPaths = emptyList()
                            textElements = emptyList()
                        }
                    ) {
                        Icon(Icons.Filled.Refresh, contentDescription = "Clear Page", tint = Color.White)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Color Palette Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val colorsList = listOf(
                        Color.Red, Color.Blue, Color(0xFF10B981), Color.Yellow, Color.White, Color.Black
                    )
                    colorsList.forEach { col ->
                        val isSel = selectedColor == col
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(col)
                                .border(
                                    width = if (isSel) 3.dp else 1.dp,
                                    color = if (isSel) Color(0xFF00C6FF) else Color.White.copy(alpha = 0.3f),
                                    shape = CircleShape
                                )
                                .clickable { selectedColor = col }
                        )
                    }
                }
            }

            // Prompt Dialog for Adding Text
            if (showAddTextPrompt) {
                Dialog(onDismissRequest = { showAddTextPrompt = false }) {
                    GlassCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        cornerRadius = 24.dp
                    ) {
                        Text(
                            text = "Add Custom Text",
                            color = Color.White,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        OutlinedTextField(
                            value = textInput,
                            onValueChange = { textInput = it },
                            placeholder = { Text("Enter text to write on PDF page...", color = Color.White.copy(alpha = 0.4f)) },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFF3B82F6),
                                unfocusedBorderColor = Color.White.copy(alpha = 0.2f),
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White
                            ),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            Button(
                                onClick = { showAddTextPrompt = false },
                                colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0.1f))
                            ) {
                                Text("Cancel", color = Color.White)
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Button(
                                onClick = {
                                    if (textInput.isNotEmpty()) {
                                        textElements = textElements + TextElement(
                                            text = textInput,
                                            position = Offset(displayWidth / 3f, displayHeight / 3f),
                                            color = selectedColor
                                        )
                                        textInput = ""
                                    }
                                    showAddTextPrompt = false
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3B82F6))
                            ) {
                                Text("Add", color = Color.White)
                            }
                        }
                    }
                }
            }
        }
    }
}

// --- FULL SCREEN PREVIEW & INTERACTION DIALOG ---
@Composable
fun FullScreenImageDialog(
    imageUri: Uri?,
    imageBitmap: Bitmap? = null,
    rotation: Float = 0f,
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        var scale by remember { mutableStateOf(1f) }
        var offset by remember { mutableStateOf(Offset.Zero) }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.95f))
                .pointerInput(Unit) {
                    detectTransformGestures { _, pan, zoom, _ ->
                        scale = (scale * zoom).coerceIn(1f, 5f)
                        if (scale > 1f) {
                            offset += pan
                        } else {
                            offset = Offset.Zero
                        }
                    }
                }
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer(
                        scaleX = scale,
                        scaleY = scale,
                        translationX = offset.x,
                        translationY = offset.y,
                        rotationZ = rotation
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (imageBitmap != null) {
                    Image(
                        bitmap = imageBitmap.asImageBitmap(),
                        contentDescription = "Zoomed Page Preview",
                        modifier = Modifier.fillMaxWidth(0.95f),
                        contentScale = ContentScale.Fit
                    )
                } else if (imageUri != null) {
                    AsyncImage(
                        model = imageUri,
                        contentDescription = "Zoomed Image Preview",
                        modifier = Modifier.fillMaxWidth(0.95f),
                        contentScale = ContentScale.Fit
                    )
                }
            }

            // Top action bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter)
                    .statusBarsPadding()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Info text
                Text(
                    text = "Pinch to Zoom / Double Tap to Close",
                    color = Color.White.copy(alpha = 0.6f),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .background(Color.Black.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                )

                // Close Button
                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .background(Color.White.copy(alpha = 0.15f), CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "Close Preview",
                        tint = Color.White
                    )
                }
            }
        }
    }
}

// --- CENTERED PDF GENERATED SUCCESS DIALOG ---
@Composable
fun GeneratedPdfCenterDialog(
    pdfName: String,
    operation: String,
    onPreview: () -> Unit,
    onDownload: () -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.6f)),
            contentAlignment = Alignment.Center
        ) {
            GlassCard(
                modifier = Modifier
                    .fillMaxWidth(0.86f)
                    .padding(16.dp)
                    .shadow(32.dp, RoundedCornerShape(24.dp)),
                cornerRadius = 24.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Celebration header
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .background(Color(0xFF10B981).copy(alpha = 0.15f), CircleShape)
                            .border(1.dp, Color(0xFF10B981).copy(alpha = 0.3f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.CheckCircle,
                            contentDescription = "Success",
                            tint = Color(0xFF10B981),
                            modifier = Modifier.size(34.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "PDF Generated Successfully!",
                        color = Color.White,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 18.sp,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White.copy(alpha = 0.05f), RoundedCornerShape(12.dp))
                            .padding(12.dp)
                    ) {
                        Text(
                            text = "Filename:",
                            color = Color.White.copy(alpha = 0.4f),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = pdfName,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Operation:",
                            color = Color.White.copy(alpha = 0.4f),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = operation,
                            color = Color(0xFF00C6FF),
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Preview Button
                        Button(
                            onClick = onPreview,
                            colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0.08f)),
                            border = BorderStroke(1.dp, Color.White.copy(alpha = 0.15f)),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(40.dp)
                        ) {
                            Text("Preview", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        }

                        // Download/Save Button
                        Button(
                            onClick = onDownload,
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(40.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(
                                    brush = Brush.horizontalGradient(listOf(Color(0xFF2563EB), Color(0xFF8B5CF6)))
                                )
                        ) {
                            Text("Download", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Close Button
                    TextButton(
                        onClick = onDismiss,
                        modifier = Modifier.height(34.dp)
                    ) {
                        Text("Dismiss", color = Color.White.copy(alpha = 0.6f), fontSize = 12.sp)
                    }
                }
            }
        }
    }
}

// --- DEV LOGO AND ABOUT INFO ---
@Composable
fun HelpPrivacyDialog(
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.6f)),
            contentAlignment = Alignment.Center
        ) {
            GlassCard(
                modifier = Modifier
                    .fillMaxWidth(0.88f)
                    .padding(16.dp)
                    .shadow(32.dp, RoundedCornerShape(28.dp)),
                cornerRadius = 28.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Title
                    Text(
                        text = "GlassPDF 2026 PRO",
                        color = Color.White,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 20.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Secure Offline PDF Utility",
                        color = Color(0xFF00C6FF),
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 12.sp
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    // Description / Privacy text
                    Text(
                        text = "Privacy & Rights Policy:\n" +
                               "• 100% Offline Processing: All operations (Conversion, Merging, Splitting, and Writing) are executed directly and securely on your local device CPU/GPU. No external servers or API calls are used.\n" +
                               "• Zero Data Collection: GlassPDF does not collect, store, track, or share any personal files, metadata, or activity logs. Your files are yours alone.",
                        color = Color.White.copy(alpha = 0.85f),
                        fontSize = 12.sp,
                        lineHeight = 18.sp,
                        textAlign = TextAlign.Start,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White.copy(alpha = 0.05f), RoundedCornerShape(12.dp))
                            .padding(12.dp)
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        text = "Developed by:",
                        color = Color.White.copy(alpha = 0.5f),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    // Developer Logo
                    NSDevLogo()

                    Spacer(modifier = Modifier.height(16.dp))

                    // Close Button
                    Button(
                        onClick = onDismiss,
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth(0.6f)
                            .height(40.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(
                                brush = Brush.horizontalGradient(listOf(Color(0xFF2563EB), Color(0xFF8B5CF6)))
                            )
                    ) {
                        Text("Close", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

fun lerp(start: Float, stop: Float, fraction: Float): Float {
    return start + fraction * (stop - start)
}

@Composable
fun FlyingDownloadAnimation(
    visible: Boolean,
    onAnimationEnd: () -> Unit
) {
    if (!visible) return

    val animProgress = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        animProgress.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 1100, easing = FastOutSlowInEasing)
        )
        onAnimationEnd()
    }

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val width = constraints.maxWidth.toFloat()
        val height = constraints.maxHeight.toFloat()

        // Start coordinates: Center of screen
        val startX = width / 2f
        val startY = height / 2f

        // End coordinates: Approx position of history tab (5th tab in bottom-right)
        val endX = width * 0.82f
        val endY = height * 0.94f

        val progress = animProgress.value

        // Interpolated positions with a curved arc (bezier-like movement)
        val t = progress
        val p1X = width * 0.3f // Curving slightly left-up for a nice swing
        val p1Y = height * 0.4f

        val currentX = (1 - t) * (1 - t) * startX + 2 * (1 - t) * t * p1X + t * t * endX
        val currentY = (1 - t) * (1 - t) * startY + 2 * (1 - t) * t * p1Y + t * t * endY

        val scale = lerp(1.5f, 0.3f, t)
        val rotation = lerp(0f, 360f, t)
        val alpha = if (t < 0.8f) 1f else (1f - t) / 0.2f // Fade out at the end

        Box(
            modifier = Modifier
                .offset(
                    x = (currentX / LocalDensity.current.density).dp,
                    y = (currentY / LocalDensity.current.density).dp
                )
                .graphicsLayer(
                    scaleX = scale,
                    scaleY = scale,
                    rotationZ = rotation,
                    alpha = alpha
                )
                .size(48.dp)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(Color(0xFF00C6FF), Color(0xFF0072FF))
                    ),
                    shape = RoundedCornerShape(12.dp)
                )
                .border(2.dp, Color.White, RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.Check,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
fun NSDevLogo() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .background(Color(0xFF131124), RoundedCornerShape(12.dp))
            .border(1.dp, Color.White.copy(alpha = 0.12f), RoundedCornerShape(12.dp))
            .padding(horizontal = 14.dp, vertical = 8.dp)
    ) {
        Text(
            text = "LNS",
            color = Color.White,
            fontWeight = FontWeight.Black,
            fontSize = 16.sp,
            letterSpacing = 0.5.sp
        )
        Text(
            text = ".dev",
            color = Color(0xFF00C6FF),
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )
    }
}

// --- UTILITY METHODS FOR PAGE RANGE PARSING ---
private fun parsePageRange(rangeStr: String, totalPages: Int): Set<Int> {
    val result = mutableSetOf<Int>()
    if (rangeStr.isBlank()) return result
    try {
        val parts = rangeStr.split(",")
        for (part in parts) {
            val trimmed = part.trim()
            if (trimmed.contains("-")) {
                val bounds = trimmed.split("-")
                if (bounds.size == 2) {
                    val start = bounds[0].trim().toIntOrNull() ?: 1
                    val end = bounds[1].trim().toIntOrNull() ?: totalPages
                    val rStart = start.coerceIn(1, totalPages)
                    val rEnd = end.coerceIn(1, totalPages)
                    val actualStart = minOf(rStart, rEnd)
                    val actualEnd = maxOf(rStart, rEnd)
                    for (i in actualStart..actualEnd) {
                        result.add(i)
                    }
                }
            } else {
                val page = trimmed.toIntOrNull()
                if (page != null) {
                    result.add(page.coerceIn(1, totalPages))
                }
            }
        }
    } catch (e: Exception) {
        // Safe fallback
    }
    return result
}

private fun formatPageRange(pagesSet: Set<Int>): String {
    if (pagesSet.isEmpty()) return ""
    val sorted = pagesSet.sorted()
    val result = mutableListOf<String>()
    var i = 0
    while (i < sorted.size) {
        var j = i
        while (j + 1 < sorted.size && sorted[j + 1] == sorted[j] + 1) {
            j++
        }
        if (j > i) {
            result.add("${sorted[i]}-${sorted[j]}")
        } else {
            result.add("${sorted[i]}")
        }
        i = j + 1
    }
    return result.joinToString(", ")
}
