package com.example.seafest.ui.profile

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.DisplayMetrics
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.viewModels
import com.example.seafest.R
import com.example.seafest.ViewModelFactory
import com.example.seafest.databinding.FragmentHomeBinding
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
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.editIcon.setOnClickListener {
            startActivity(Intent(requireActivity(), EditProfileActivity::class.java))
        }

        binding.buttonLogOut.setOnClickListener {
            viewModel.logout()
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
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProfileFragment().apply {

            }
    }

    private fun showCustomPopup(title:String,message: String) {
        val dialog = Dialog(requireActivity())
        dialog.setContentView(R.layout.popup_costum)

        val imagePopup: ImageView = dialog.findViewById(R.id.image_popup)
        imagePopup.setImageResource(R.drawable.logo_full_apk)

        val popupTitle: TextView = dialog.findViewById(R.id.title_popup)
        popupTitle.text = title

        val popupMessage: TextView = dialog.findViewById(R.id.deskripsi_popup)
        popupMessage.text = message

        val popupButton: Button = dialog.findViewById(R.id.button_popup)
        popupButton.text = "Sign Up"
        popupButton.setOnClickListener {
            val intent = Intent(requireActivity(), LoginActivity::class.java)
            startActivity(intent)
            dialog.dismiss()
        }
        val window = dialog.window
        val layoutParams = window?.attributes
        val displayMetrics = DisplayMetrics()
        requireActivity().windowManager.defaultDisplay.getMetrics(displayMetrics)
        val screenWidth = displayMetrics.widthPixels
        layoutParams?.width = (screenWidth * 0.9).toInt()
        window?.attributes = layoutParams

        dialog.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}