package com.example.seafest.ui.scanner

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.seafest.data.api.response.User
import com.example.seafest.data.repository.FishRepository
import com.example.seafest.data.repository.UserRepository
import java.io.File

class ScannerViewModel(private val repository: FishRepository, private val userRepository: UserRepository) : ViewModel() {
    fun getSession(): LiveData<User> {
        return userRepository.getSession().asLiveData()
    }
    fun addScan(imgFile: File,userId: String, fishStatus: String, fishName: String ) =
        repository.addScan( imgFile, userId, fishStatus, fishName)
}