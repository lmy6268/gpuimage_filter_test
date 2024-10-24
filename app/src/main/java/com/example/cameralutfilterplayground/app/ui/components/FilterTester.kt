package com.example.cameralutfilterplayground.app.ui.components

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.view.ViewGroup
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import jp.co.cyberagent.android.gpuimage.GPUImageView
import jp.co.cyberagent.android.gpuimage.filter.GPUImageLookupFilter


@Composable
fun FilterTestScreen(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val contentResolver = LocalContext.current.contentResolver
    var filterImage by remember { mutableStateOf<Bitmap?>(null) }

    val gpuImageView = remember {
        GPUImageView(context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            setBackgroundColor(Color.WHITE)
        }
    }

    val backgroundLauncher =
        rememberLauncherForActivityResult(PickVisualMedia()) { uri ->
            uri?.run { gpuImageView.setImage(this) }
        }
    val filterLauncher =
        rememberLauncherForActivityResult(PickVisualMedia()) { uri ->
            uri?.run {
                val inputStream = contentResolver.openInputStream(this)
                inputStream?.use { stream ->
                    val bitmap = BitmapFactory.decodeStream(stream)
                    filterImage = bitmap
                }
            }
        }
    LaunchedEffect(filterImage) {
        filterImage?.run {
            gpuImageView.filter = GPUImageLookupFilter(1.0f).apply {
                bitmap = this@run
            }
        }
    }
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AndroidView(
            factory = { gpuImageView },
            modifier = Modifier
                .weight(9f)
                .fillMaxWidth()
        )
        Row(
            Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                Modifier
                    .fillMaxSize()
                    .weight(1f)
                    .clickable {
                        backgroundLauncher.launch(PickVisualMediaRequest(PickVisualMedia.ImageOnly))
                    }, contentAlignment = Alignment.Center
            ) {
                Text("배경 이미지 가져오기")
            }
            Box(
                Modifier
                    .fillMaxSize()
                    .clickable {
                        filterLauncher.launch(PickVisualMediaRequest(PickVisualMedia.ImageOnly))
                    }
                    .weight(1f), contentAlignment = Alignment.Center
            ) {
                Text("필터 이미지 가져오기")
            }
        }
    }
}

//fun loadImage(launcher: ManagedActivityResultLauncher<Intent, ActivityResult>, context: Context) {
//    val isAvailable = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU ||
//            Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && SdkExtensions.getExtensionVersion(
//        Build.VERSION_CODES.R
//    ) >= 2
//    val intent = if (isAvailable) Intent(MediaStore.ACTION_PICK_IMAGES).apply {
//        type = getVisualMimeType
//    }
//
//}