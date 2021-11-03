package com.example.inspirytesttask.presentation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.inspirytesttask.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val viewModelFactory by lazy{
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


        binding.renderTemplate.setOnClickListener {
            if (!encoderStatus) {
                binding.myView.setRecordOptions(FRAME_COUNT, true)
                binding.myView.bitmapListReady = {
                    viewModel.setupEncoder(it)
                    toastMessage("Encode start")
                }
            } else {
                toastMessage("Encode already start")
            }
        }

    }

    private fun toastMessage(message: String){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    companion object{
        private const val FRAME_COUNT = 120
    }
}