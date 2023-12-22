package com.example.seafest.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.seafest.data.api.response.DetailFishResponse
import com.example.seafest.data.api.response.Fish
import com.example.seafest.data.api.response.User
import com.example.seafest.data.repository.FishRepository
import com.example.seafest.data.repository.UserRepository
import kotlinx.coroutines.launch

class DetailViewModel(private val fishRepository: FishRepository,private val userRepository: UserRepository) : ViewModel() {

    fun addCart(idUser: String,fishIdCart: String) = fishRepository.addCart(idUser, fishIdCart)
    fun getSession(): LiveData<User> {
        return userRepository.getSession().asLiveData()
    }
    fun getFishDetail(fishId: String?) = fishRepository.fishDetail(fishId)
}