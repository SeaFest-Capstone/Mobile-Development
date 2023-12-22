package com.example.seafest.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.seafest.data.api.response.User
import com.example.seafest.data.repository.UserRepository
import kotlinx.coroutines.launch

class ProfileViewModel(private val userRepository: UserRepository): ViewModel() {
    fun logout() {
        viewModelScope.launch {
            userRepository.logout()
        }
    }

    fun getProfile(idUser: String?) = userRepository.getProfile(idUser)

    fun getSession(): LiveData<User> {
        return userRepository.getSession().asLiveData()
    }
}