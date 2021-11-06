package com.example.inspirytesttask.domain

import android.graphics.Bitmap
import java.io.File
import java.util.ArrayList

class StartEncoderUseCase(private val videoEncoder: VideoEncoder) {

    fun startEncoder(file: File) {
        videoEncoder.startEncoder(file)
    }
}