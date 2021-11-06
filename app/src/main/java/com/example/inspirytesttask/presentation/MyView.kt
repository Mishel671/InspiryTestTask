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
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.io.File
import java.util.ArrayList
import android.graphics.BitmapFactory
import android.os.Build
import androidx.core.graphics.applyCanvas
import androidx.core.graphics.scale
import java.io.FileInputStream
import java.io.FileNotFoundException


class MyView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : View(context, attrs, defStyleAttr) {

    private lateinit var bitmap: Bitmap
    private var dispWidth: Int = 0
    private var dispHeight: Int = 0

    private var start = 1
    private var move = 1

    private var startForRecord: Int = 0
    private var moveForRecord: Int = 0


    private val coroutineScope = CoroutineScope(Dispatchers.Default)

    private val paint = Paint().apply {
        color = Color.BLACK
        textSize = 80f
        isAntiAlias = true
    }
    private val text = "Hello World"
    private val textWidth = paint.measureText(text)


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        dispWidth = w
        dispHeight = h
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            bitmap = Bitmap.createBitmap(dispWidth, dispHeight, Bitmap.Config.ARGB_8888)
        } else {
            bitmap = Bitmap.createBitmap(dispWidth, dispHeight, Bitmap.Config.RGB_565)
        }
        move = ((dispWidth / 20) - (paint.measureText(text).toInt() / 20)) / 2
        Log.d("MyLog", "$dispWidth & $move")

    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        Log.d("MyLog", "onLayout")
        MainActivity.bitmapRecordStart = { frameCount ->
            coroutineScope.launch {

                startForRecord = start
                moveForRecord = move
                for (i in 1..frameCount) {
                    bitmap.applyCanvas {
                        drawCanvas(this, true)
                    }
                    bitmapReady?.invoke(bitmap.scale(1080, 1920))
                }
                bitmap.recycle()
                stopEncoder?.invoke()
            }
        }
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawCanvas(canvas, false)
        invalidate()
    }

    private fun drawCanvas(canvas: Canvas, drawToBitmap: Boolean) {
        canvas.drawColor(Color.parseColor("#DADADA"))
        if(drawToBitmap) {
            canvas.drawText(text, startForRecord.toFloat(), (dispHeight / 2).toFloat(), paint)
            if (startForRecord + textWidth >= bitmap!!.width) {
                moveForRecord *= -1
            } else if (startForRecord <= 0) {
                moveForRecord *= -1
            }
            startForRecord += moveForRecord

        } else {
            canvas.drawText(text, start.toFloat(), (dispHeight / 2).toFloat(), paint)
            if (start + textWidth >= dispWidth) {
                move *= -1
            } else if (start <= 0) {
                move *= -1
            }
            start += move

        }
    }




    companion object {
        var bitmapReady: ((Bitmap) -> Unit)? = null
        var stopEncoder: (() -> Unit)? = null
    }
}