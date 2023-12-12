package com.example.seafest.ui.history

import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.seafest.R
import com.example.seafest.databinding.ActivityHistoryListBinding

class HistoryListActivity : AppCompatActivity() {

    private var _binding : ActivityHistoryListBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityHistoryListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
        setSupportActionBar(binding.toolbar)


        supportActionBar?.title = "Riwayat Scan"


        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        supportActionBar?.setHomeAsUpIndicator(R.drawable.arrow_left_alt_black_24px)
    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}