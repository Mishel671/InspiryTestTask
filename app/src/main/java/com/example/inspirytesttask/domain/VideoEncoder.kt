package com.example.inspirytesttask.domain

import android.graphics.Bitmap
import java.io.File
import java.util.ArrayList

interface VideoEncoder {

    fun startEncoder(bitmapList: ArrayList<Bitmap>)
}