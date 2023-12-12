package com.example.seafest.ui.splashscreen

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.DisplayMetrics
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
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
            if (isInternetAvailable()) {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)

                finish()
            }else{
                val iconError = R.drawable.check_x
                showCustomPopup("Error", "Gagal terhubung \nCek koneksi internet anda", iconError)
            }
        }, SPLASH_TIME_OUT)

    }

    private fun isInternetAvailable(): Boolean {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            val networkCapabilities = connectivityManager.activeNetwork ?: return false
            val activeNetwork =
                connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false

            return when {
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                // Untuk API level 23 ke bawah, Anda mungkin perlu menambahkan cek untuk TRANSPORT_ETHERNET dan TRANSPORT_BLUETOOTH
                else -> false
            }
        } else {
            val activeNetworkInfo: NetworkInfo? = connectivityManager.activeNetworkInfo
            return activeNetworkInfo != null && activeNetworkInfo.isConnected
        }
    }
    private fun showCustomPopup(title:String,message: String, image: Int) {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.popup_costum)

        val imagePopup: ImageView = dialog.findViewById(R.id.image_popup)

        imagePopup.setImageResource(image)

        val popupTitle: TextView = dialog.findViewById(R.id.title_popup)
        popupTitle.text = title

        val popupMessage: TextView = dialog.findViewById(R.id.deskripsi_popup)
        popupMessage.text = message

        val popupButton: Button = dialog.findViewById(R.id.button_popup)
        popupButton.text = "Oke"
        popupButton.setOnClickListener {
            finish()
        }

        val window = dialog.window
        val layoutParams = window?.attributes
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val screenWidth = displayMetrics.widthPixels
        layoutParams?.width = (screenWidth * 0.9).toInt()
        window?.attributes = layoutParams

        dialog.setOnDismissListener{
            finish()
        }
        dialog.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}

