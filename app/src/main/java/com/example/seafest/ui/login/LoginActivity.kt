package com.example.seafest.ui.login

import android.app.Dialog
import android.content.Intent
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import com.example.seafest.R
import com.example.seafest.ViewModelFactory
import com.example.seafest.data.ResultState
import com.example.seafest.databinding.ActivityLoginBinding
import com.example.seafest.ui.register.RegisterActivity

class LoginActivity : AppCompatActivity() {
    private var _binding: ActivityLoginBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        binding.ivButtonX.setOnClickListener {
            finish()
        }

        binding.tvSignUp.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
            finish()
        }
        login()
    }

    private fun login() {
        binding.btnSignIn.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()

            if (!isEmailValid(email)) {
                binding.etEmail.error = getString(R.string.invalid_email_format)
                return@setOnClickListener
            }


            if (password.length < 8) {
                binding.etPassword.error = getString(R.string.length_of_password)
                return@setOnClickListener
            }

            viewModel.login(email, password).observe(this) { result ->
                when (result) {
                    is ResultState.Loading -> {
                        binding.btnSignIn.isClickable = false
                        binding.etEmail.isEnabled = false
                        binding.etPassword.isEnabled = false
                        showLoading(true)
                    }

                    is ResultState.Success -> {
                        binding.btnSignIn.isClickable = true
                        binding.etEmail.isEnabled = true
                        binding.etPassword.isEnabled = true
                        showLoading(false)
                        val loginResponse = result.data
                        loginResponse.user?.let { it1 ->
                            viewModel.saveSession(it1)
                            Log.d("Save Session", "Ini yang di sace $it1")
                        }
                        showCustomPopup("${loginResponse.message}", R.drawable.check_v, true)
                        Log.d("LoginTest", "Login Berhasil ${loginResponse.user}")
                    }

                    is ResultState.Error -> {
                        binding.btnSignIn.isClickable = true
                        binding.etEmail.isEnabled = true
                        binding.etPassword.isEnabled = true
                        showLoading(false)
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

    private fun showCustomPopup(message: String, image: Int, status: Boolean) {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.popup_costum)

        val imagePopup: ImageView = dialog.findViewById(R.id.image_popup)

        imagePopup.setImageResource(image)

        val popupTitle: TextView = dialog.findViewById(R.id.title_popup)
        popupTitle.text = "Login"

        val popupMessage: TextView = dialog.findViewById(R.id.deskripsi_popup)
        popupMessage.text = message

        val popupButton: Button = dialog.findViewById(R.id.button_popup)
        popupButton.text = "Oke"
        popupButton.setOnClickListener {
            if (status) {
                finish()
            }
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

        dialog.setOnDismissListener {
            if (status) {
                finish()
            }
        }
        dialog.show()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressIndicator?.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}