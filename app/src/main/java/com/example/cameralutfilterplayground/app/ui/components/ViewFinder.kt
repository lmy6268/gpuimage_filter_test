package com.example.cameralutfilterplayground.app.ui.components

import android.graphics.Color
import android.view.ViewGroup
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.cameralutfilterplayground.model.ImageFrame
import jp.co.cyberagent.android.gpuimage.GPUImageView
import jp.co.cyberagent.android.gpuimage.GPUImageView.RENDERMODE_WHEN_DIRTY
import timber.log.Timber

@Composable
fun CameraViewFinder(
    modifier: Modifier = Modifier,
) {
    val viewModel: MainViewModel = hiltViewModel()
    val owner = LocalLifecycleOwner.current
    LaunchedEffect(Unit) {
        viewModel.initializeCamera(owner)
    }

    val frame by viewModel.frame.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val gpuImageView = remember {
        GPUImageView(context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            setBackgroundColor(Color.WHITE)
        }
    }
    LaunchedEffect(frame) {
        frame?.let {
            gpuImageView.setImage(it.data)
        }
    }
    Box(modifier) {
        AndroidView(modifier = Modifier.fillMaxSize(), factory = { gpuImageView })
    }

}