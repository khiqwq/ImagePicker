package com.example.imagepicker

import android.Manifest
import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import coil3.compose.AsyncImage
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme(colorScheme = lightColorScheme()) {
                ImagePickerScreen(
                    onPin = { startLockTask() },
                    onHideBars = {
                        WindowCompat.getInsetsController(window, window.decorView).apply {
                            hide(WindowInsetsCompat.Type.systemBars())
                            systemBarsBehavior =
                                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
                        }
                    },
                    onShowBars = {
                        WindowCompat.getInsetsController(window, window.decorView)
                            .show(WindowInsetsCompat.Type.systemBars())
                    }
                )
            }
        }
    }
}

@Composable
fun ImagePickerScreen(
    onPin: () -> Unit,
    onHideBars: () -> Unit,
    onShowBars: () -> Unit
) {
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var suggestedUri by remember { mutableStateOf<Uri?>(null) }
    var showSuggestion by remember { mutableStateOf(false) }
    // Track which URIs the user has already seen/ignored
    val ignoredUris = remember { mutableSetOf<Uri>() }
    var hasPermission by remember { mutableStateOf(false) }
    var showPinButton by remember { mutableStateOf(true) }
    val context = LocalContext.current

    val picker = rememberLauncherForActivityResult(PickVisualMedia()) { uri ->
        if (uri != null) {
            imageUri = uri
            // Mark old suggestion as ignored
            suggestedUri?.let { ignoredUris.add(it) }
            showSuggestion = false
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        hasPermission = granted
    }

    LaunchedEffect(Unit) {
        permissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
    }

    // Re-check for recent photos whenever we're on the pick screen
    LaunchedEffect(hasPermission, imageUri) {
        if (!hasPermission || imageUri != null) return@LaunchedEffect
        val recent = findRecentPhoto(context)
        if (recent != null && recent !in ignoredUris) {
            suggestedUri = recent
            showSuggestion = true
        } else {
            showSuggestion = false
        }
    }

    // Auto-dismiss suggestion after 5 seconds, mark as ignored
    LaunchedEffect(showSuggestion) {
        if (showSuggestion) {
            delay(5000)
            showSuggestion = false
            suggestedUri?.let { ignoredUris.add(it) }
        }
    }

    LaunchedEffect(imageUri) {
        if (imageUri != null) {
            onHideBars()
            showPinButton = true
        } else {
            onShowBars()
        }
    }

    // Auto-hide pin button after 4 seconds
    LaunchedEffect(showPinButton, imageUri) {
        if (showPinButton && imageUri != null) {
            delay(4000)
            showPinButton = false
        }
    }

    Box(
        modifier = Modifier.fillMaxSize().background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        if (imageUri != null) {
            // ── Image display ──
            Box(Modifier.fillMaxSize().then(Modifier.padding(0.dp))) {
                AsyncImage(
                    model = imageUri,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Fit
                )
            }

            // Pin button — auto-hides after 4s
            AnimatedVisibility(
                visible = showPinButton,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 48.dp, end = 16.dp),
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Surface(
                    onClick = {
                        onPin()
                        showPinButton = false
                    },
                    shape = RoundedCornerShape(24.dp),
                    color = Color.White.copy(alpha = 0.88f),
                    shadowElevation = 6.dp
                ) {
                    Text(
                        "\uD83D\uDCCC 固定展示",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color(0xFF444444),
                        modifier = Modifier.padding(horizontal = 18.dp, vertical = 10.dp)
                    )
                }
            }
        } else {
            // ── Pick screen ──
            Box(
                Modifier.fillMaxSize().drawBehind {
                    drawRect(Color(0xFFEDEDF2))
                    drawCircle(
                        Brush.radialGradient(
                            listOf(Color(0x38A0AACC), Color.Transparent),
                            center = Offset(size.width * 0.2f, size.height * 0.25f),
                            radius = size.minDimension * 0.9f
                        ),
                        radius = size.minDimension * 0.9f,
                        center = Offset(size.width * 0.2f, size.height * 0.25f)
                    )
                    drawCircle(
                        Brush.radialGradient(
                            listOf(Color(0x30B0B8D0), Color.Transparent),
                            center = Offset(size.width * 0.8f, size.height * 0.7f),
                            radius = size.minDimension * 0.75f
                        ),
                        radius = size.minDimension * 0.75f,
                        center = Offset(size.width * 0.8f, size.height * 0.7f)
                    )
                }
            ) {

                // Glass pick button — centered
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Surface(
                        onClick = {
                            picker.launch(PickVisualMediaRequest(PickVisualMedia.ImageOnly))
                        },
                        shape = RoundedCornerShape(24.dp),
                        color = Color.White.copy(alpha = 0.42f),
                        border = BorderStroke(
                            1.dp,
                            Brush.verticalGradient(
                                listOf(
                                    Color.White.copy(alpha = 0.85f),
                                    Color.White.copy(alpha = 0.12f)
                                )
                            )
                        ),
                        shadowElevation = 0.dp
                    ) {
                        Text(
                            "选择图片",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Normal,
                            color = Color(0xFF555555),
                            modifier = Modifier.padding(horizontal = 36.dp, vertical = 16.dp)
                        )
                    }
                }

                // Suggestion card — bottom end
                AnimatedVisibility(
                    visible = showSuggestion && suggestedUri != null,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(20.dp),
                    enter = slideInVertically { it } + fadeIn(),
                    exit = slideOutVertically { it } + fadeOut()
                ) {
                    suggestedUri?.let { uri ->
                        Surface(
                            onClick = {
                                imageUri = uri
                                showSuggestion = false
                                ignoredUris.add(uri)
                            },
                            shape = RoundedCornerShape(20.dp),
                            color = Color.White.copy(alpha = 0.48f),
                            border = BorderStroke(
                                1.dp,
                                Brush.verticalGradient(
                                    listOf(
                                        Color.White.copy(alpha = 0.8f),
                                        Color.White.copy(alpha = 0.1f)
                                    )
                                )
                            ),
                            shadowElevation = 0.dp
                        ) {
                            Column(
                                modifier = Modifier
                                    .widthIn(max = 160.dp)
                                    .padding(12.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                AsyncImage(
                                    model = uri,
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(96.dp)
                                        .clip(RoundedCornerShape(12.dp)),
                                    contentScale = ContentScale.Crop
                                )
                                Spacer(Modifier.height(8.dp))
                                Text(
                                    "你可能是要展示这张图片",
                                    fontSize = 11.sp,
                                    color = Color(0xFF999999),
                                    textAlign = TextAlign.Center,
                                    lineHeight = 15.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun findRecentPhoto(context: Context): Uri? {
    val fiveMinAgo = System.currentTimeMillis() - 5 * 60 * 1000
    context.contentResolver.query(
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
        arrayOf(MediaStore.Images.Media._ID),
        "${MediaStore.Images.Media.DATE_TAKEN} > ?",
        arrayOf(fiveMinAgo.toString()),
        "${MediaStore.Images.Media.DATE_TAKEN} DESC"
    )?.use { cursor ->
        if (cursor.moveToFirst()) {
            val id = cursor.getLong(
                cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            )
            return ContentUris.withAppendedId(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id
            )
        }
    }
    return null
}
