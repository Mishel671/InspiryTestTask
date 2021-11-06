package com.example.inspirytesttask.domain

import android.graphics.Bitmap
import java.io.File
import java.util.ArrayList

class SetBitmapUseCase(private val videoEncoder: VideoEncoder) {

    fun setBitmap(bitmap: Bitmap) {
        videoEncoder.setBitmap(bitmap)
    }
}
