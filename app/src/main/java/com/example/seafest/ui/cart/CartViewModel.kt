package com.example.seafest.ui.cart

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.seafest.data.api.response.User
import com.example.seafest.data.repository.FishRepository
import com.example.seafest.data.repository.UserRepository

class CartViewModel(private val repository: FishRepository,private val userRepository: UserRepository) : ViewModel() {

    fun getCart(idUser: String) = repository.getCart(idUser)
    fun checkout(userId:String,fishIdCart:String) = repository.checkoutResponse(userId, fishIdCart)
    fun getSession(): LiveData<User> {
        return userRepository.getSession().asLiveData()
    }
}