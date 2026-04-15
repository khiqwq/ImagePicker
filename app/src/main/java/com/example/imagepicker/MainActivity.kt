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
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.IntOffset
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

@Composable
private fun GlassCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    val shape = RoundedCornerShape(16.dp)
    Box(modifier = modifier) {
        Box(
            modifier = Modifier
                .matchParentSize()
                .offset { IntOffset(0, 6) }
                .blur(12.dp)
                .background(Color.Black.copy(alpha = 0.1f), shape)
        )
        Column(
            modifier = Modifier
                .clip(shape)
                .background(Color.White.copy(alpha = 0.72f))
                .border(0.5.dp, Color.White.copy(alpha = 0.4f), shape)
                .then(
                    if (onClick != null) Modifier.clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = onClick
                    ) else Modifier
                )
                .padding(horizontal = 20.dp, vertical = 14.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            content = content
        )
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
    var suggestionDismissed by remember { mutableStateOf(false) }
    var showPinButton by remember { mutableStateOf(true) }
    val context = LocalContext.current

    val picker = rememberLauncherForActivityResult(PickVisualMedia()) { uri ->
        if (uri != null) {
            imageUri = uri
            suggestionDismissed = true
            showSuggestion = false
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted && !suggestionDismissed) {
            val recent = findRecentPhoto(context)
            suggestedUri = recent
            if (recent != null) showSuggestion = true
        }
    }

    LaunchedEffect(Unit) {
        permissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
    }

    LaunchedEffect(showSuggestion) {
        if (showSuggestion) {
            delay(5000)
            showSuggestion = false
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

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        if (imageUri != null) {
            AsyncImage(
                model = imageUri,
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Fit
            )

            AnimatedVisibility(
                visible = showPinButton,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 48.dp, end = 16.dp),
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                GlassCard(onClick = {
                    onPin()
                    showPinButton = false
                }) {
                    Text(
                        "\uD83D\uDCCC 固定展示",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        } else {
            GlassCard(onClick = {
                picker.launch(PickVisualMediaRequest(PickVisualMedia.ImageOnly))
            }) {
                Text(
                    "选择图片",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF333333)
                )
            }

            AnimatedVisibility(
                visible = showSuggestion && suggestedUri != null,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp),
                enter = slideInVertically { it } + fadeIn(),
                exit = slideOutVertically { it } + fadeOut()
            ) {
                suggestedUri?.let { uri ->
                    GlassCard(
                        modifier = Modifier.widthIn(max = 180.dp),
                        onClick = {
                            imageUri = uri
                            showSuggestion = false
                        }
                    ) {
                        AsyncImage(
                            model = uri,
                            contentDescription = null,
                            modifier = Modifier
                                .size(100.dp)
                                .clip(RoundedCornerShape(12.dp)),
                            contentScale = ContentScale.Crop
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(
                            "你可能是要展示这张图片",
                            fontSize = 11.sp,
                            color = Color.Gray,
                            textAlign = TextAlign.Center
                        )
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
