package com.example.inspirytesttask.data

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import com.example.inspirytesttask.domain.VideoEncoder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import androidx.core.content.FileProvider
import androidx.core.content.ContextCompat.startActivity

import android.content.Intent
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getExternalFilesDirs
import com.example.inspirytesttask.BuildConfig
import androidx.core.app.ActivityCompat.startActivityForResult
import com.example.inspirytesttask.presentation.MainActivity
import androidx.core.content.ContextCompat.startActivity
import kotlinx.coroutines.cancel

class VideoEncoderImpl(
    private val context: Context,
) : VideoEncoder {
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    private var bitmapToVideoEncoder: BitmapToVideoEncoder? = null

    override fun startEncoder(file: File) {
        bitmapToVideoEncoder = BitmapToVideoEncoder(
            object : BitmapToVideoEncoder.IBitmapToVideoEncoderCallback {
                override fun onEncodingComplete(outputFile: File?) {
                    coroutineScope.launch {

                        val videoURI = FileProvider.getUriForFile(context,
                            context.applicationContext.packageName + ".provider",
                            file)
                        val intent = Intent()
                        intent.action = Intent.ACTION_VIEW
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                        intent.setDataAndType(videoURI, "video/mp4")
                        context.startActivity(intent)
                        cancel()
                    }
                }
            })
        bitmapToVideoEncoder?.startEncoding(file)
    }

    override fun setBitmap(bitmap: Bitmap) {
        bitmapToVideoEncoder?.queueFrame(bitmap)
    }

    override fun stopEncoder() {
        bitmapToVideoEncoder?.stopEncoding()
    }
}