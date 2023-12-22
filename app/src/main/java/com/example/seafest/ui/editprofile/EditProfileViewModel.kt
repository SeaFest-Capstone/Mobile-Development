package com.example.seafest.ui.editprofile

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.seafest.data.api.response.User
import com.example.seafest.data.repository.UserRepository
import java.io.File

class EditProfileViewModel(private val repository: UserRepository) : ViewModel() {
    fun getProfile(idUser: String?) = repository.getProfile(idUser)
    fun updateProfile(idUser: String, imgFile: File, noTelp: String, alamat: String) =
        repository.updateProfile(idUser, imgFile, noTelp, alamat)

    fun updateProfile(idUser: String, noTelp: String, alamat: String) =
        repository.updateProfile(idUser, noTelp, alamat)

    fun getSession(): LiveData<User> {
        return repository.getSession().asLiveData()
    }
}