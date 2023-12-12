package com.example.seafest.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.seafest.data.ResultState
import com.example.seafest.data.api.ApiService
import com.example.seafest.data.api.response.LoginResponse
import com.example.seafest.data.api.response.LoginResult
import com.example.seafest.data.pref.UserPreference
import kotlinx.coroutines.flow.Flow

class UserRepository private constructor(
    private val apiService: ApiService,
    private val userPreference: UserPreference
) {

    suspend fun saveSession(user: LoginResult) {
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<LoginResult> {
        return userPreference.getSession()
    }


    suspend fun logout() {
        userPreference.logout()
    }

    fun login(email: String, password: String): LiveData<ResultState<LoginResponse>> = liveData {
        emit(ResultState.Loading)
        try {
            val result = apiService.login(email, password)
            emit(ResultState.Success(result))
        } catch (e: Exception) {
            emit(ResultState.Error(e.message.toString()))
        }
    }

    companion object {
        fun getInstance(
            apiService: ApiService,
            userPreference: UserPreference
        ): UserRepository = UserRepository(apiService, userPreference)
    }
}