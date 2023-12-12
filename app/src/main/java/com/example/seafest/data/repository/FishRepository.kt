package com.example.seafest.data.repository


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.example.seafest.data.ResultState
import com.example.seafest.data.api.ApiService
import com.example.seafest.data.api.response.DetailFishResponse
import com.example.seafest.data.api.response.FishResponse


class FishRepository private constructor(
    private val apiService: ApiService
) {
    fun getFishByHabitat(idHabitat: Int?): LiveData<ResultState<FishResponse>> = liveData {
        emit(ResultState.Loading)
        try {
            val result = apiService.getFishByHabitat(idHabitat)
            emitSource(MutableLiveData(ResultState.Success(result)))
        } catch (e: Exception) {
            emit(ResultState.Error(e.message.toString()))
        }
    }

    fun searchFish(nameFish: String?): LiveData<ResultState<FishResponse>> = liveData {
        emit(ResultState.Loading)
        try {
            val result = apiService.searchFish(nameFish)
            emitSource(MutableLiveData(ResultState.Success(result)))
        } catch (e: Exception) {
            emit(ResultState.Error(e.message.toString()))
        }
    }

    fun fishDetail(fishId: Int?): LiveData<ResultState<DetailFishResponse>> = liveData {
        emit(ResultState.Loading)
        try {
            val result = apiService.getDetailFish(fishId)
            emitSource(MutableLiveData(ResultState.Success(result)))
        } catch (e: Exception) {
            emit(ResultState.Error(e.message.toString()))
        }
    }

    companion object {
        fun getInstance(
            apiService: ApiService,
        ): FishRepository = FishRepository(apiService)
    }
}


