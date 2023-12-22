package com.example.seafest.ui.register

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
import com.example.seafest.R
import com.example.seafest.ViewModelFactory
import com.example.seafest.data.ResultState
import com.example.seafest.databinding.ActivityRegisterBinding
import com.example.seafest.ui.login.LoginActivity

class RegisterActivity : AppCompatActivity() {
    private var _binding : ActivityRegisterBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<RegisterViewModel> {
        ViewModelFactory.getInstance(this)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        _binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.ivButtonX.setOnClickListener {
            finish()
        }
        binding.tvSignIn.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
        register()
    }

    private fun buttonClickable(isTrue: Boolean) {
        binding.ivButtonX.isClickable = isTrue
        binding.tvSignIn.isEnabled = isTrue
        binding.btnSignUp.isEnabled = isTrue
        binding.etEmail.isEnabled = isTrue
        binding.etPassword.isEnabled = isTrue
        binding.etUsername.isEnabled = isTrue
        binding.etConfirmPassword.isEnabled = isTrue
    }
    private fun register() {
        binding.btnSignUp.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()
            val username = binding.etUsername.text.toString()
            val confirmPassword = binding.etConfirmPassword.text.toString()

            if (!isUsernameValid(username)) {
                binding.etUsername.error = getString(R.string.invalid_username_format)
                return@setOnClickListener
            }

            if (!isEmailValid(email)) {
                binding.etEmail.error = getString(R.string.invalid_email_format)
                return@setOnClickListener
            }

            if (password.length < 8) {
                binding.etPassword.error = getString(R.string.length_of_password)
                return@setOnClickListener
            }

            if (confirmPassword.length < 8) {
                binding.etConfirmPassword.error = getString(R.string.length_of_password)
                return@setOnClickListener
            } else {
                if (confirmPassword != password) {
                    binding.etConfirmPassword.error = getString(R.string.not_same)
                    return@setOnClickListener
                }
            }


            viewModel.register(username,email, password, confirmPassword).observe(this){ result ->
                when (result) {
                    is ResultState.Loading -> {
                        buttonClickable(false)
                        showLoading(true)
                    }

                    is ResultState.Success -> {
                        showLoading(false)
                        buttonClickable(true)
                        val registerResponse = result.data
                        showCustomPopup( "${registerResponse.message}", R.drawable.check_v, true)
                    }

                    is ResultState.Error -> {
                        showLoading(false)
                        buttonClickable(true)
                        val errorMessage = result.message
                        showCustomPopup("$errorMessage", R.drawable.check_x, false)
                    }
                }
            }
        }
    }

    private fun isEmailValid(email: String): Boolean {
        val emailPattern = "^[a-zA-Z0-9_]+@gmail\\.com\$"
        return email.matches(emailPattern.toRegex())
    }
    private fun isUsernameValid(username: String): Boolean {
        val regex = "^[a-zA-Z0-9_]+$"
        return username.matches(regex.toRegex())
    }

    private fun showCustomPopup(message: String, image: Int, status: Boolean) {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.popup_costum)

        val imagePopup: ImageView = dialog.findViewById(R.id.image_popup)

        imagePopup.setImageResource(image)

        val popupTitle: TextView = dialog.findViewById(R.id.title_popup)
        popupTitle.text = "Register"

        val popupMessage: TextView = dialog.findViewById(R.id.deskripsi_popup)
        popupMessage.text = message

        val popupButton: Button = dialog.findViewById(R.id.button_popup)
        popupButton.text = "Oke"
        popupButton.setOnClickListener {
            dialog.dismiss()
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
            if (status) {
                intentToLogin()
            }
        }
        dialog.show()
    }

    private fun intentToLogin() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
    private fun showLoading(isLoading: Boolean) {
        binding.progressIndicator?.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}