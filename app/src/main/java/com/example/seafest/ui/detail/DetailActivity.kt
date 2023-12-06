package com.example.seafest.ui.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.seafest.R
import com.example.seafest.ViewModelFactory
import com.example.seafest.databinding.ActivityDetailBinding
import com.example.seafest.databinding.FragmentHomeBinding
import com.example.seafest.ui.home.HomeViewModel
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

        binding.buttonBack.setOnClickListener { onBackPressed() }

        val id = intent.getIntExtra(KEY,0)
        viewModel.getFishDetail(id)

        viewModel.detailFishResponse.observe(this, Observer { detailFishResponse ->
            try {
                // Cek apakah detailFishResponse tidak null
                detailFishResponse?.let { detailFish ->
                    // Pastikan nilai-nilai yang diterima tidak null sebelum mengakses propertinya
                    val nameFish = detailFish.nameFish ?: ""
                    val habitat = detailFish.habitat ?: ""
                    val photoUrl = detailFish.photoUrl ?: ""
                    val description = detailFish.description ?: ""
                    val price = detailFish.price ?: 0
                    val benefit = detailFish.benefit ?: ""

                    setDetailStory(nameFish, habitat, photoUrl, description, price, benefit)
                }
            } catch (e: Exception) {
                // Handle error sesuai kebutuhan, misalnya menampilkan pesan kesalahan
                e.printStackTrace()
                // Tambahkan penanganan kesalahan di sini sesuai kebutuhan
            }
        })
        }

    private fun setDetailStory(
        nama: String?,
        habitat:String?,
        photo: String?,
        description: String?,
        harga: Int?,
        manfaat:String?,
    ) {
        binding?.tvNameFishDetail?.text = nama
        binding.tvHabitat.text = "/$habitat"
        binding?.deskripsiIkan?.text = description
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