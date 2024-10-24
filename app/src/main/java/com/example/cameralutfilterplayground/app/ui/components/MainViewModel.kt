package com.example.cameralutfilterplayground.app.ui.components

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cameralutfilterplayground.domain.CameraRepository
import com.example.cameralutfilterplayground.model.ImageFrame
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: CameraRepository
) : ViewModel() {
    val frame: StateFlow<ImageFrame?> field = MutableStateFlow(null)
    fun initializeCamera(lifecycleOwner: LifecycleOwner) {
        viewModelScope.launch {
            repository.initializeCamera(lifecycleOwner)
                .collectLatest {
                    frame.value = it
                }
        }
    }
}