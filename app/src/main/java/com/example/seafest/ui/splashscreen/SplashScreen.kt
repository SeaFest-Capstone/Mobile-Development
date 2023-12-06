package com.example.seafest.ui.splashscreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.example.seafest.R
import com.example.seafest.databinding.ActivityMainBinding
import com.example.seafest.databinding.ActivitySplashScreenBinding
import com.example.seafest.ui.main.MainActivity

class SplashScreen : AppCompatActivity() {
    private var _binding : ActivitySplashScreenBinding? = null
    private val binding get() = _binding

    private val SPLASH_TIME_OUT: Long = 2500 // Durasi splash screen dalam milidetik

    @Override
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        val animasi : Animation = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        binding?.ivLogoSplashScreen?.startAnimation(animasi)

        // Handler untuk menangani delay dan navigasi ke aktivitas tujuan
        Handler().postDelayed({
            // Intent untuk memulai aktivitas tujuan setelah splash screen selesai
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)

            // Tutup aktivitas splash screen agar tidak dapat dikembalikan
            finish()
        }, SPLASH_TIME_OUT)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}

