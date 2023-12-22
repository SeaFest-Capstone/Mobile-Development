package com.example.seafest.ui.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.seafest.data.api.response.User
import com.example.seafest.data.repository.FishRepository
import com.example.seafest.data.repository.UserRepository

class HistoryViewModel(private val repository: FishRepository, private val userRepository: UserRepository) : ViewModel() {
    fun getFishHistory(idUser: String?) = repository.fishHistory(idUser)

    fun getSession(): LiveData<User> {
        return userRepository.getSession().asLiveData()
    }
}