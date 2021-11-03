package com.example.inspirytesttask.presentation

import android.content.Context
import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import com.example.inspirytesttask.data.VideoEncoderImpl
import com.example.inspirytesttask.domain.StartEncoderUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(
    context: Context
): ViewModel(){
    private val encoderImpl = VideoEncoderImpl(context)

    private val startEncoderUseCase = StartEncoderUseCase(encoderImpl)

    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    fun setupEncoder(bitmapList: ArrayList<Bitmap>){
        coroutineScope.launch {
            startEncoderUseCase.startEncoder(bitmapList)
        }
    }
}