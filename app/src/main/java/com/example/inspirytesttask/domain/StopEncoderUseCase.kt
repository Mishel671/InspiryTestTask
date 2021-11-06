package com.example.inspirytesttask.domain

import android.graphics.Bitmap
import java.io.File
import java.util.ArrayList

class StopEncoderUseCase(private val videoEncoder: VideoEncoder) {

    fun stopEncoder() {
        videoEncoder.stopEncoder()
    }
}
