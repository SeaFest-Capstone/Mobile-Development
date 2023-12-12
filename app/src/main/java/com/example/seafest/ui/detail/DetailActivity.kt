package com.example.seafest.ui.detail

import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import com.bumptech.glide.Glide
import com.example.seafest.R
import com.example.seafest.ViewModelFactory
import com.example.seafest.data.ResultState
import com.example.seafest.databinding.ActivityDetailBinding
import com.example.seafest.utils.Helper

class DetailActivity : AppCompatActivity() {

    private var _binding: ActivityDetailBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<DetailViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)

        binding.buttonBack.setOnClickListener { onBackPressed() }

        val id = intent.getIntExtra(KEY, 0)

        getData(id)

    }

    private fun getData(id: Int) {
        viewModel.getFishDetail(id).observe(this) { result ->
            when (result) {
                is ResultState.Loading -> {
                    // Tindakan yang dilakukan saat sedang memuat
                    showLoading(true)
                }
                is ResultState.Success -> {
                    val fishResponse = result.data.fish
                    val nameFish = fishResponse?.nameFish ?: ""
                    val habitat = fishResponse?.habitat ?: ""
                    val photoUrl = fishResponse?.photoUrl ?: ""
                    val description = fishResponse?.description ?: ""
                    val price = fishResponse?.price ?: 0
                    val benefit = fishResponse?.benefit ?: ""

                    setDetailStory(nameFish, habitat, photoUrl, description, price, benefit)
                }

                is ResultState.Error -> {
                    showLoading(false)
                    val iconError = R.drawable.check_x
                    val errorMessage = result.message
//                    showCustomPopup("Error", "Gagal terhubung \nCek koneksi internet anda", iconError)
//                    showAlert("Gagal terhubung \nCek koneksi internet anda ")
                }
            }
        }
    }
    private fun showLoading(isLoading: Boolean) {
        binding?.progressIndicator?.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
    private fun setDetailStory(
        nama: String?,
        habitat: String?,
        photo: String?,
        description: String?,
        harga: Int?,
        manfaat: String?,
    ) {
        binding.tvNameFishDetail.text = nama
        binding.tvHabitat.text = "/$habitat"
        binding.deskripsiIkan.text = description
        binding.manfaatIkan.text = manfaat
        binding.hargaNamaIkan.text = "Harga Ikan $nama"
        harga?.let {
            val formattedHarga = Helper.formatRupiah(it)
            binding?.hargaIkan?.text = formattedHarga
        }
        Glide.with(this).load(photo).into(binding?.fotoIkan!!)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    companion object {
        const val KEY = "key"
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}