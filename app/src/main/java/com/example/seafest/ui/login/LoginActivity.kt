package com.example.seafest.ui.login

import android.content.Intent
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import com.example.seafest.R
import com.example.seafest.ViewModelFactory
import com.example.seafest.data.ResultState
import com.example.seafest.databinding.ActivityLoginBinding
import com.example.seafest.ui.home.HomeViewModel
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
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)

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
            val email = binding.etPassword.text.toString()
            val password = binding.tvPassword.text.toString()
            viewModel.login(email, password).observe(this, { result ->
                when (result) {
                    is ResultState.Loading -> {
                        // Tindakan ketika proses loading
                    }

                    is ResultState.Success -> {
                        // Tindakan ketika login berhasil
                        val loginResponse = result.data
                        loginResponse.loginResult?.let { it1 -> viewModel.saveSession(it1) }
                        Log.d("LoginTest", "Login Berhasil ${loginResponse.loginResult}")
                    }

                    is ResultState.Error -> {
                        // Tindakan ketika terjadi error
                        val errorMessage = result.message // Pesan error jika diperlukan
                    }
                }
            })
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}