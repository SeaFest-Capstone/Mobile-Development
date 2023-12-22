package com.example.seafest.ui.history

import android.app.Dialog
import android.content.Intent
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.seafest.R
import com.example.seafest.ViewModelFactory
import com.example.seafest.adapter.HistoryListAdapter
import com.example.seafest.adapter.SeaFishAdapter
import com.example.seafest.data.ResultState
import com.example.seafest.data.api.response.ListScanItem
import com.example.seafest.databinding.ActivityHistoryListBinding
import com.example.seafest.ui.detailhistory.DetailHistoryFragment
import com.example.seafest.ui.home.HomeViewModel
import com.example.seafest.ui.login.LoginActivity

class HistoryListActivity : AppCompatActivity() {

    private var _binding: ActivityHistoryListBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<HistoryViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityHistoryListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        setSupportActionBar(binding.toolbar)

        supportActionBar?.title = "Riwayat Scan"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.arrow_left_alt_black_24px)

        binding.buttonPopup.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    LoginActivity::class.java
                )
            )
        }

        viewModel.getSession().observe(this) { user ->
            if (user.token != "") {
                user.uid?.let { getData(it) }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getSession().observe(this) { user ->
            if (user.token == "") {
                binding.layoutNotLogin.visibility = View.VISIBLE
                binding.recyclerView.visibility = View.GONE
            }else{
                binding.layoutNotLogin.visibility = View.GONE
                binding.recyclerView.visibility = View.VISIBLE
            }
        }
    }
    private fun getData(id: String) {
        viewModel.getFishHistory(id).observe(this) { result ->
            when (result) {
                is ResultState.Loading -> {
                    showLoading(true)
                }

                is ResultState.Success -> {
                    showLoading(false)
                    val fishResponse = result.data.listScan
                    if (fishResponse == null || fishResponse.isEmpty()) {
                        binding.recyclerView.visibility = View.GONE
                        binding.tvNotFound.visibility = View.VISIBLE
                    } else {
                        binding.tvNotFound.visibility = View.GONE

                        val adapterFish = HistoryListAdapter(fishResponse) {
                            showDetailDialog(it)
                        }
                        binding.recyclerView?.apply {
                            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                            adapter = adapterFish
                            setHasFixedSize(true)
                            visibility = View.VISIBLE
                        }
                        adapterFish.submitList(fishResponse)
                        Log.d("historyResponse", "ini data history : $fishResponse")
                    }
                }

                is ResultState.Error -> {
                    showLoading(false)
                    val iconError = R.drawable.check_x
                    val errorMessage = result.message
                    showCustomPopup(errorMessage, iconError)
                }
            }
        }
    }

    private fun showCustomPopup(message: String, image: Int) {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.popup_costum)

        val imagePopup: ImageView = dialog.findViewById(R.id.image_popup)

        imagePopup.setImageResource(image)

        val popupTitle: TextView = dialog.findViewById(R.id.title_popup)
        popupTitle.text = "Scan"

        val popupMessage: TextView = dialog.findViewById(R.id.deskripsi_popup)
        popupMessage.text = message

        val popupButton: Button = dialog.findViewById(R.id.button_popup)
        popupButton.text = "Oke"
        popupButton.setOnClickListener {
            dialog.dismiss()
            finish()
        }

        val buttonYes: Button = dialog.findViewById(R.id.button_popup_yes)
        buttonYes.visibility = View.GONE

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

    private fun showDetailDialog(fish: ListScanItem) {
        val detailDialog = DetailHistoryFragment.newInstance(fish)

        // Menetapkan lebar dan tinggi dialog menjadi MATCH_PARENT
        val width = ViewGroup.LayoutParams.MATCH_PARENT
        val height = ViewGroup.LayoutParams.MATCH_PARENT

        // Menetapkan parameter untuk dialog
        detailDialog.dialog?.window?.setLayout(width, height)

        // Menampilkan dialog
        detailDialog.show(supportFragmentManager, "FishDetailDialog")
    }


    private fun showLoading(isLoading: Boolean) {
        binding.progressIndicator?.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}