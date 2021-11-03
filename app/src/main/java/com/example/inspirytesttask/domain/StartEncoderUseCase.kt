package com.example.inspirytesttask.domain

import android.graphics.Bitmap
import java.io.File
import java.util.ArrayList

class StartEncoderUseCase(private val videoEncoder: VideoEncoder)  {

    fun startEncoder(bitmapList: ArrayList<Bitmap>){
        videoEncoder.startEncoder(bitmapList)
    }
}