package com.example.inspirytesttask.presentation

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.inspirytesttask.data.BitmapToVideoEncoder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.util.ArrayList

class MyView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : View(context, attrs, defStyleAttr) {

    private lateinit var bitmap: Bitmap
    private lateinit var canvasBitmap: Canvas
    private var dispWidth: Int = 0
    private var dispHeight: Int = 0
    private var start = 1
    private var move = 1
    private var countRecordedFrame = -1
    private var needRecordFrame = 0
    private var startRecord = false
    private val bitmapList: ArrayList<Bitmap> = ArrayList()

    var bitmapListReady: ((ArrayList<Bitmap>) -> Unit)? = null


    private val paint = Paint().apply {
        color = Color.BLACK
        textSize = 80f
        isAntiAlias = true
    }
    private val text = "Hello World"
    private var endText = start + paint.measureText(text)


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        dispWidth = w
        dispHeight = h
        bitmap = Bitmap.createBitmap(dispWidth, dispHeight, Bitmap.Config.ARGB_8888)
        canvasBitmap = Canvas(bitmap)
        move = ((dispWidth / 20) - (paint.measureText(text).toInt() / 20)) / 2
        Log.d("MyLog", "$dispWidth & $move")
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawCanvas(canvas)
        if (countRecordedFrame < needRecordFrame && startRecord) {
            drawCanvas(canvasBitmap)
            bitmapList.add(bitmap.copy(bitmap.config, false))
            countRecordedFrame++
        } else if (countRecordedFrame >= needRecordFrame && startRecord) {
            startRecord = false
            bitmapListReady?.invoke(bitmapList)

        }
        invalidate()
        if (endText.toInt() >= dispWidth) {
            move *= -1
        } else if (start <= 0) {
            move *= -1
        }
        start += move
        endText = start + paint.measureText(text)
    }


    fun setRecordOptions(countFrame: Int, start: Boolean) {
        startRecord = start
        needRecordFrame = countFrame
        countRecordedFrame = 0
    }

    private fun drawCanvas(canvas: Canvas) {
        canvas.drawColor(Color.parseColor("#DADADA"))
        canvas.drawText(text, start.toFloat(), (dispHeight / 2).toFloat(), paint)
    }

    private fun startEncoder() {

        val bitmapToVideoEncoder = BitmapToVideoEncoder(
            object : BitmapToVideoEncoder.IBitmapToVideoEncoderCallback {
                override fun onEncodingComplete(outputFile: File?) {
                    Handler(Looper.getMainLooper()).post {
                        Toast.makeText(
                            context,
                            "Encoding complete!",
                            Toast.LENGTH_LONG
                        ).show()
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