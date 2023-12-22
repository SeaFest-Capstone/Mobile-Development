package com.example.seafest.di

import android.content.Context
import com.example.seafest.data.api.ApiConfig
import com.example.seafest.data.pref.UserPreference
import com.example.seafest.data.pref.dataStore
import com.example.seafest.data.repository.FishRepository
import com.example.seafest.data.repository.UserRepository

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val apiService = ApiConfig.getApiService()
        return UserRepository.getInstance(apiService, pref)
    }

    fun provideFishRepository(context: Context): FishRepository {
        val apiService = ApiConfig.getApiService()
        return FishRepository.getInstance(apiService)
    }
}