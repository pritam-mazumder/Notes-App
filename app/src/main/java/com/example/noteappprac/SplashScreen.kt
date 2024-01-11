package com.example.noteappprac

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.noteappprac.databinding.ActivitySplashScreenBinding

class SplashScreen : AppCompatActivity() {
    private val binding by lazy { ActivitySplashScreenBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.getStarted.setOnClickListener {
            startActivity(Intent(this, LogIn::class.java))
            finish()
        }
    }
}