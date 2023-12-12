package com.example.seafest.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.seafest.data.api.response.LoginResult
import com.example.seafest.data.repository.FishRepository
import com.example.seafest.data.repository.UserRepository

class MainViewModel(private val repository: UserRepository) : ViewModel()  {
    fun getSession(): LiveData<LoginResult> {
        return repository.getSession().asLiveData()
    }
}