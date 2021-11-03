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
import androidx.core.content.ContextCompat
import com.example.inspirytesttask.BuildConfig


class VideoEncoderImpl(
    private val context: Context,
) : VideoEncoder {

    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    override fun startEncoder(bitmapList: ArrayList<Bitmap>) {
        val bitmapToVideoEncoder = BitmapToVideoEncoder(
            object : BitmapToVideoEncoder.IBitmapToVideoEncoderCallback {
                override fun onEncodingComplete(outputFile: File?) {
                    coroutineScope.launch {
                        Toast.makeText(
                            context,
                            "Encoding complete!",
                            Toast.LENGTH_LONG
                        ).show()
                        val videoFile = File(Environment
                            .getExternalStoragePublicDirectory(
                                Environment.DIRECTORY_DCIM
                            ).toString() + "/muxedAudioVideo.mp4")
                        val fileUri: Uri = FileProvider.getUriForFile(context,
                            BuildConfig.APPLICATION_ID + ".provider", ///storage/emulated/0/DCIM/
                            videoFile)
                        val intent = Intent(Intent.ACTION_VIEW)
                        intent.setDataAndType(fileUri, "video/mp4")
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION) //DO NOT FORGET THIS EVER
                        context.startActivity(intent)
                    }
                }
            })
        val fileName = "/muxedAudioVideo.mp4"
        val file = File(
            Environment
                .getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DCIM
                ).toString() + fileName
        )
        bitmapToVideoEncoder.startEncoding(file)
        var bitmapScaled: Bitmap
        for (bitmap in bitmapList) {
            bitmapScaled = Bitmap.createScaledBitmap(
                bitmap,
                1080,
                1920,
                false)
            bitmapToVideoEncoder.queueFrame(bitmapScaled)
        }
        bitmapToVideoEncoder.stopEncoding()
    }
}