package com.example.inspirytesttask.presentation

import android.Manifest
import android.content.Context
import android.graphics.Bitmap
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModel
import com.example.inspirytesttask.data.VideoEncoderImpl
import com.example.inspirytesttask.domain.SetBitmapUseCase
import com.example.inspirytesttask.domain.StartEncoderUseCase
import com.example.inspirytesttask.domain.StopEncoderUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.io.File

class MainViewModel(
    context: Context,
) : ViewModel() {

    private val encoderImpl = VideoEncoderImpl(context)

    private val startEncoderUseCase = StartEncoderUseCase(encoderImpl)
    private val setBitmapUseCase = SetBitmapUseCase(encoderImpl)
    private val stopEncoderUseCase = StopEncoderUseCase(encoderImpl)

    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    fun startEncoder(file: File) {
        coroutineScope.launch {
            startEncoderUseCase.startEncoder(file)
            cancel()
        }
    }

    fun setBitmap(bitmap: Bitmap) {
        setBitmapUseCase.setBitmap(bitmap)
    }

    fun stopEncoder() {
        stopEncoderUseCase.stopEncoder()
    }

}