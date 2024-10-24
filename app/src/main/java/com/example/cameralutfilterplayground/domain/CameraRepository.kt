package com.example.cameralutfilterplayground.domain

import android.graphics.Bitmap
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.example.cameralutfilterplayground.model.ImageFrame
import kotlinx.coroutines.flow.Flow

interface CameraRepository {
    suspend fun initializeCamera(lifecycleOwner: LifecycleOwner): Flow<ImageFrame>
}