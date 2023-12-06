package com.example.seafest.di

import android.content.Context
import com.example.seafest.data.api.ApiConfig
import com.example.seafest.data.local.FishDatabase
import com.example.seafest.data.repository.FishRepository

object Injection {

    fun provideFishRepository(context: Context): FishRepository {
        val apiService = ApiConfig.getApiService()
        val database = FishDatabase.getInstance(context)
        return FishRepository.getInstance(apiService, database)
    }
}