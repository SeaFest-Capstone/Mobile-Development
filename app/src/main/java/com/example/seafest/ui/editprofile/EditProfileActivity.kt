package com.example.seafest.ui.editprofile

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.seafest.R
import com.example.seafest.databinding.ActivityDetailBinding
import com.example.seafest.databinding.ActivityEditProfileBinding

class EditProfileActivity : AppCompatActivity() {
    private var _binding: ActivityEditProfileBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)

        setSupportActionBar(binding.toolbar)


        supportActionBar?.title = "Edit Profile"


        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        supportActionBar?.setHomeAsUpIndicator(R.drawable.arrow_left_alt_black_24px)
    }

    // Tangani klik tombol kembali
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
