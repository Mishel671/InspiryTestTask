package com.example.inspirytesttask.domain

import android.graphics.Bitmap
import java.io.File
import java.util.ArrayList

interface VideoEncoder {

    fun startEncoder(file:File)

    fun setBitmap(bitmap:Bitmap)

    fun stopEncoder()
}