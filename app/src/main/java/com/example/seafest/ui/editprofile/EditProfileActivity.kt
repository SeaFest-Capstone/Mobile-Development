package com.example.seafest.ui.editprofile

import android.app.Dialog
import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.seafest.R
import com.example.seafest.ViewModelFactory
import com.example.seafest.data.ResultState
import com.example.seafest.databinding.ActivityEditProfileBinding
import com.example.seafest.utils.reduceFileImage
import com.example.seafest.utils.uriToFile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EditProfileActivity : AppCompatActivity(), OnImageSelectedListener {
    private var _binding: ActivityEditProfileBinding? = null
    private val binding get() = _binding!!

    private var currentImageUri: Uri? = null
    private var isUpNavigationEnabled = true
    private val viewModel by viewModels<EditProfileViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = "Edit Profile"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.arrow_left_alt_black_24px)

        binding.profilePhoto.setOnClickListener {
            showBottomSheet()
        }

        binding.btnCamera.setOnClickListener {
            showBottomSheet()
        }

        viewModel.getSession().observe(this) {
            it.uid?.let { it1 -> getData(it1) }
        }
        setupActivity()
        showImage()


    }

    fun setupActivity() {
        binding.buttonSave.setOnClickListener {
            startUpload()
            Log.d("Debug", "Button clicked")
        }
    }

    private fun startUpload() {
        Log.d("Coba", "Ini startUpload")
        viewModel.getSession().observe(this) {
            upload(it.uid!!)
        }

    }

    fun showImage() {
        currentImageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.profilePhoto.setImageURI(it)
        }
    }

    private fun showBottomSheet() {
        val modalBottomSheet = ModalBottomSheet()
        modalBottomSheet.setOnImageSelectedListener(this)
        modalBottomSheet.show(supportFragmentManager, ModalBottomSheet.TAG)
    }

    override fun onSupportNavigateUp(): Boolean {
        if (isUpNavigationEnabled) {
            onBackPressed()
        }
        return true
    }

    override fun onImageSelected(imageUri: Uri) {
        currentImageUri = imageUri
        showImage()
    }

    private fun upload(id: String) {
        lifecycleScope.launch(Dispatchers.Main) {
            currentImageUri?.let { uri ->
                showLoading(true)
                binding.tvPhone.isEnabled =false
                binding.tvAddress.isEnabled =false
                binding.buttonSave.isClickable = false
                val imgFile = withContext(Dispatchers.IO) {
                    uriToFile(
                        uri,
                        this@EditProfileActivity
                    ).reduceFileImage()
                }
                val noTelp = binding.tvPhone.text.toString()
                val alamat = binding.tvAddress.text.toString()
                viewModel.updateProfile(id, imgFile, noTelp, alamat)
                    .observe(this@EditProfileActivity) { result ->
                        Log.d("upload", "Button viewmodel")
                        Log.d("result", "ini result $result")
                        if (result != null) {
                            when (result) {
                                is ResultState.Loading -> {
                                    showLoading(true)
                                    isUpNavigationEnabled = false
                                }

                                is ResultState.Success -> {
                                    binding.tvPhone.isEnabled =true
                                    binding.tvAddress.isEnabled =true
                                    binding.buttonSave.isClickable = true
                                    isUpNavigationEnabled = true
                                    showLoading(false)
                                    val iconCheck = R.drawable.check_v
                                    result.data.message?.let {
                                        showCustomPopup(
                                            "Profile",
                                            it,
                                            iconCheck
                                        )
                                    }

                                }

                                is ResultState.Error -> {
                                    isUpNavigationEnabled = true
                                    binding.tvPhone.isEnabled = true
                                    binding.tvAddress.isEnabled = true
                                    binding.buttonSave.isClickable = true
                                    showLoading(false)
                                    val errorResponse = result.message

                                }
                            }
                        }
                    }
            } ?: run {
                val noTelp = binding.tvPhone.text.toString()
                val alamat = binding.tvAddress.text.toString()
                viewModel.updateProfile(id, noTelp, alamat)
                    .observe(this@EditProfileActivity) { result ->
                        if (result != null) {
                            when (result) {
                                is ResultState.Loading -> {
                                    showLoading(true)
                                    binding.tvPhone.isEnabled =false
                                    binding.tvAddress.isEnabled =false
                                    binding.buttonSave.isClickable = false
                                    isUpNavigationEnabled = false

                                }

                                is ResultState.Success -> {
                                    showLoading(false)
                                    val iconCheck = R.drawable.check_v
                                    result.data.message?.let {
                                        showCustomPopup(
                                            "Profile",
                                            it,
                                            iconCheck
                                        )
                                    }
                                    binding.tvPhone.isEnabled =true
                                    binding.tvAddress.isEnabled =true
                                    binding.buttonSave.isClickable = true
                                    isUpNavigationEnabled = true
                                }

                                is ResultState.Error -> {
                                    showLoading(false)
                                    val iconError = R.drawable.check_x
                                    val errorResponse = result.message
                                    showCustomPopup("Error", errorResponse.toString(), iconError)
                                    binding.tvPhone.isEnabled =true
                                    binding.tvAddress.isEnabled =true
                                    binding.buttonSave.isClickable = true
                                    isUpNavigationEnabled = true
                                }
                            }
                        }
                    }
            }
        }
    }

    private fun getData(id: String) {
        viewModel.getProfile(id).observe(this) { result ->
            when (result) {
                is ResultState.Loading -> {
                    showLoading(true)
                    showLayout(false)
                }

                is ResultState.Success -> {
                    showLoading(false)
                    showLayout(true)
                    val profileResponse = result.data.data
                    Log.d("coba", "$profileResponse")
                    val username = profileResponse?.username ?: ""
                    val email = profileResponse?.email ?: ""
                    val phone = profileResponse?.noTelp ?: ""
                    val address = profileResponse?.alamat ?: ""
                    val profilePhotoUrl = profileResponse?.photoProfile ?: ""

                    setEditProfile(username, email, phone, address, profilePhotoUrl)
                }

                is ResultState.Error -> {
                    showLoading(false)
                    val iconError = R.drawable.check_x
                    val errorMessage = result.message
                    showCustomPopup("Error", errorMessage, iconError)
                }
            }
        }
    }

    private fun setEditProfile(
        username: String?,
        email: String?,
        phone: String?,
        address: String?,
        profilePhotoUrl: String?
    ) {
        binding.tvUsername.setText(username)
        binding.tvEmail.setText(email)

        val formattedPhone = if (phone.isNullOrBlank() || phone == "empty") {
            "-"
        } else {
            phone
        }
        binding.tvPhone.setText(formattedPhone)

        val formattedAddress = if (address.isNullOrBlank() || address == "empty") {
            "-"
        } else {
            address
        }
        binding.tvAddress.setText(formattedAddress)

        if (profilePhotoUrl != "empty") {
            Glide.with(this)
                .load(profilePhotoUrl)
                .placeholder(R.drawable.user)
                .error(R.drawable.user)
                .into(binding.profilePhoto)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressIndicator?.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showLayout(isLoading: Boolean) {
        binding.layoutEditProfile?.visibility = if (isLoading) View.VISIBLE else View.GONE
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

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}

