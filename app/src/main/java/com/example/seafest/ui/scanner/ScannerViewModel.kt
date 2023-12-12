package com.example.seafest.ui.scanner

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.seafest.data.api.response.LoginResult
import com.example.seafest.data.repository.FishRepository
import com.example.seafest.data.repository.UserRepository

class ScannerViewModel(private val repository: FishRepository, private val userRepository: UserRepository) : ViewModel() {
    fun getSession(): LiveData<LoginResult> {
        return userRepository.getSession().asLiveData()
    }
}