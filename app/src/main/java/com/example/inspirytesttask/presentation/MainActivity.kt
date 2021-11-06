package com.example.inspirytesttask.presentation

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.inspirytesttask.databinding.ActivityMainBinding
import androidx.core.app.ActivityCompat
import java.io.File


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding


    private val viewModelFactory by lazy {
        MainViewModelFactory(this)
    }
    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]
    }

    private var encoderStatus = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        askPermission()
        binding.renderTemplate.setOnClickListener {
            if (!encoderStatus) {
                encoderStatus = true
                val file = File(getExternalFilesDir(null), FILE_NAME)
                bitmapRecordStart?.invoke(FRAME_COUNT)
                viewModel.startEncoder(file)
                MyView.bitmapReady = { bitmap ->
                    viewModel.setBitmap(bitmap)
                }
                MyView.stopEncoder = {
                    viewModel.stopEncoder()
                }
                toastMessage("Encode start")
            } else {
                toastMessage("Encode already start")
            }

        }
    }

    private fun askPermission() {
        ActivityCompat.requestPermissions(this,
            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
            PERMISSION_REQUEST_CODE)

    }


    private fun toastMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }


    companion object {
        var bitmapRecordStart: ((Int) -> Unit)? = null

        private const val FILE_NAME = "InspiryVideo.mp4"
        private const val FRAME_COUNT = 120
        private const val PERMISSION_REQUEST_CODE = 7
    }
}