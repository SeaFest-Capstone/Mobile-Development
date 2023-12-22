package com.example.seafest.ui.profile

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.seafest.R
import com.example.seafest.ViewModelFactory
import com.example.seafest.data.ResultState
import com.example.seafest.databinding.FragmentProfileBinding
import com.example.seafest.ui.editprofile.EditProfileActivity
import com.example.seafest.ui.login.LoginActivity

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<ProfileViewModel> {
        ViewModelFactory.getInstance(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.editIcon.setOnClickListener {
            startActivity(Intent(requireActivity(), EditProfileActivity::class.java))
        }

        binding.buttonLogOut.setOnClickListener {
            showCustomPopup("Keluar?", "Apakah anda yakin ingin keluar?")
        }

        binding.buttonPopup.setOnClickListener {
            startActivity(Intent(requireActivity(), LoginActivity::class.java))
        }

    }

    override fun onResume() {
        super.onResume()
        viewModel.getSession().observe(this) { user ->
            if (user.token == "") {
                binding.layoutProfile.visibility = View.GONE
                binding.layoutNotLogin.visibility = View.VISIBLE
            } else {
                binding.layoutProfile.visibility = View.VISIBLE
                binding.layoutNotLogin.visibility = View.GONE
                user.uid?.let { getData(it) }
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

                    val profileResponse = result.data.data
                    val username = profileResponse?.username ?: ""
                    val email = profileResponse?.email ?: ""
                    val phone = profileResponse?.noTelp ?: ""
                    val address = profileResponse?.alamat ?: ""
                    val profilePhotoUrl = profileResponse?.photoProfile ?: ""

                    Log.d("ini glide", "link foto: $profilePhotoUrl")
                    setProfile(username, email, phone, address, profilePhotoUrl)
                    showLayout(true)
                }

                is ResultState.Error -> {
                    showLoading(false)
                    val iconError = R.drawable.check_x
                    val errorMessage = result.message
                    showCustomPopup("Error", errorMessage, iconError)
                    showLayout(false)
                }
            }
        }
    }

    private fun setProfile(
        username: String?,
        email: String?,
        phone: String?,
        address: String?,
        profilePhotoUrl: String?
    ) {
        binding.tvUsername.text = username

        binding.tvEmail.text = email

        if (phone == "empty") {
            binding.tvPhone.text = "-"
        } else {
            binding.tvPhone.text = phone
        }
        if (address == "empty") {
            binding.tvAddress.text = "-"
        } else {
            binding.tvAddress.text = address
        }

            Glide.with(this)
                .load(profilePhotoUrl)
                .placeholder(R.drawable.user)
                .error(R.drawable.user)
                .into(binding.profilePhoto)

    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressIndicator?.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
    private fun showLayout(isLoading: Boolean) {
        binding.layoutProfile?.visibility = if (isLoading) View.VISIBLE else View.GONE
    }


    private fun showCustomPopup(title: String, message: String, image: Int) {
        if (!isAdded || requireActivity().isFinishing) {
            return
        }
        val dialog = Dialog(requireActivity())
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
            requireActivity().finish()
        }

        val buttonYes: Button = dialog.findViewById(R.id.button_popup_yes)
        buttonYes.visibility = View.GONE

        val window = dialog.window
        val layoutParams = window?.attributes
        val displayMetrics = DisplayMetrics()
        requireActivity().windowManager.defaultDisplay.getMetrics(displayMetrics)
        val screenWidth = displayMetrics.widthPixels
        layoutParams?.width = (screenWidth * 0.9).toInt()
        window?.attributes = layoutParams

        dialog.show()
    }

    private fun showCustomPopup(title: String, message: String) {
        val dialog = Dialog(requireActivity())
        dialog.setContentView(R.layout.popup_costum)

        val imagePopup: ImageView = dialog.findViewById(R.id.image_popup)
        imagePopup.setImageResource(R.drawable.logo_full_apk)

        val popupTitle: TextView = dialog.findViewById(R.id.title_popup)
        popupTitle.text = title

        val popupMessage: TextView = dialog.findViewById(R.id.deskripsi_popup)
        popupMessage.text = message

        val popupButton: Button = dialog.findViewById(R.id.button_popup)
        popupButton.text = "Tidak"
        popupButton.setBackgroundResource(R.drawable.button_red)
        popupButton.setOnClickListener {
            dialog.dismiss()
        }

        val buttonYes: Button = dialog.findViewById(R.id.button_popup_yes)
        buttonYes.text = "Iya"
        buttonYes.setOnClickListener {
            viewModel.logout()
            dialog.dismiss()
        }

        val window = dialog.window
        val layoutParams = window?.attributes
        val displayMetrics = DisplayMetrics()
        requireActivity().windowManager.defaultDisplay.getMetrics(displayMetrics)
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