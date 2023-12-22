package com.example.seafest.ui.register

import androidx.lifecycle.ViewModel
import com.example.seafest.data.repository.UserRepository

class RegisterViewModel(private val repository: UserRepository) : ViewModel() {
    fun register(username:String, email: String, password: String, confirmPassword: String) = repository.register(username, email, password, confirmPassword)
}