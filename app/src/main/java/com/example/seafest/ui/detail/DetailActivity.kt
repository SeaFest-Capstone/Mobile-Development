package com.example.seafest.ui.detail

import android.app.Dialog
import android.content.Intent
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import com.bumptech.glide.Glide
import com.example.seafest.R
import com.example.seafest.ViewModelFactory
import com.example.seafest.data.ResultState
import com.example.seafest.databinding.ActivityDetailBinding
import com.example.seafest.ui.login.LoginActivity
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

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        binding.buttonBack.setOnClickListener { onBackPressed() }

        val id = intent.getStringExtra(KEY)
        val fishIdCart = intent.getStringExtra(FID)

        if (id != null) {
            getData(id)
        }
        binding.cartButton.setOnClickListener {
            addToCart(fishIdCart!!)
        }
    }

    private fun addToCart(fishIdCart: String) {

        viewModel.getSession().observe(this){
            if (it.uid == "") {
                showLoginPopup(
                    "SeaFest",
                    "Anda Belum Login \nSilahkan Login terlebih dahulu",
                    R.drawable.logo_full_apk
                )
            } else {
                it.uid?.let { it1 -> addCart(it1, fishIdCart) }
            }
        }

    }

    private fun addCart(id: String, fishIdCart:String) {
        if (fishIdCart != null) {
            viewModel.addCart(id, fishIdCart).observe(this){result ->
                when (result) {
                    is ResultState.Loading -> {
                        binding.cartButton.isClickable = false
                        showLoading(true)
                    }

                    is ResultState.Success -> {
                        showLoading(false)
                        val response = result.data.message
                        showCustomPopup("Success", "$response", R.drawable.check_v)
                        binding.cartButton.isClickable = true
                    }

                    is ResultState.Error -> {
                        val errorResponse = result.message
                        showCustomPopup("SeaFest", "$errorResponse", R.drawable.logo_full_apk)
                        binding.cartButton.isClickable = true
                    }
                }

            }

        }else{
            showCustomPopup("Error", "Fish id Cart Null", R.drawable.check_x)
        }
    }

    private fun getData(id: String) {
        viewModel.getFishDetail(id).observe(this) { result ->
            when (result) {
                is ResultState.Loading -> {
                    // Tindakan yang dilakukan saat sedang memuat
                    showLoading(true)
                }

                is ResultState.Success -> {
                    showLoading(false)

                    val fishResponse = result.data.fish
                    val nameFish = fishResponse?.nameFish ?: ""
                    val habitat = fishResponse?.habitat ?: ""
                    val photoUrl = fishResponse?.photoUrl ?: ""
                    val description = fishResponse?.description ?: ""
                    val price = fishResponse?.price?.toInt() ?: 0
                    val benefit = fishResponse?.benefit ?: ""
                    val catatan = fishResponse?.pesan ?: ""
                    if (catatan != "") {
                        binding.tvCatatan.visibility = View.VISIBLE
                        binding.catatanIkan.visibility = View.VISIBLE
                        binding.catatanIkan.text = catatan
                    }
                    setDetailStory(nameFish, habitat, photoUrl, description, price, benefit)
                }

                is ResultState.Error -> {
                    showLoading(false)
                    val iconError = R.drawable.check_x
                    val errorResponse = result.message
                    showCustomPopup("Error", errorResponse, iconError)
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressIndicator?.visibility = if (isLoading) View.VISIBLE else View.GONE
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
        binding.hargaNamaIkan.text = "Harga $nama"
        harga?.let {
            val formattedHarga = Helper.formatRupiah(it)
            binding.hargaIkan?.text = formattedHarga
        }
        Glide.with(this).load(photo).into(binding.fotoIkan)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
    private fun showCustomPopup(title: String, message: String, image: Int) {

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
            dialog.dismiss()
        }

        dialog.setOnDismissListener { finish() }

        val buttonYes: Button = dialog.findViewById(R.id.button_popup_yes)
        buttonYes.visibility = View.GONE

        val window = dialog.window
        val layoutParams = window?.attributes
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val screenWidth = displayMetrics.widthPixels
        layoutParams?.width = (screenWidth * 1).toInt()
        window?.attributes = layoutParams

        dialog.show()
    }

    private fun showLoginPopup(title: String, message: String, image: Int) {

        val dialog = Dialog(this)
        dialog.setContentView(R.layout.popup_costum)

        val imagePopup: ImageView = dialog.findViewById(R.id.image_popup)

        imagePopup.setImageResource(image)

        val popupTitle: TextView = dialog.findViewById(R.id.title_popup)
        popupTitle.text = title

        val popupMessage: TextView = dialog.findViewById(R.id.deskripsi_popup)
        popupMessage.text = message

        val popupButton: Button = dialog.findViewById(R.id.button_popup)
        popupButton.text = "Login"
        popupButton.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }


        val buttonYes: Button = dialog.findViewById(R.id.button_popup_yes)
        buttonYes.visibility = View.GONE

        val window = dialog.window
        val layoutParams = window?.attributes
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val screenWidth = displayMetrics.widthPixels
        layoutParams?.width = (screenWidth * 1).toInt()
        window?.attributes = layoutParams

        dialog.show()
    }

    companion object {
        const val KEY = "key"
        const val FID = "fid"
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}