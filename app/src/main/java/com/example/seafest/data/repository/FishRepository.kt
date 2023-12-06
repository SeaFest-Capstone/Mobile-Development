package com.example.seafest.data.repository


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.seafest.data.FishRemoteMediator
import com.example.seafest.data.ResultState
import com.example.seafest.data.api.ApiService
import com.example.seafest.data.api.response.DetailFishResponse
import com.example.seafest.data.api.response.FishResponse
import com.example.seafest.data.api.response.ListFishItem
import com.example.seafest.data.local.FishDatabase
import com.example.seafest.data.local.FishEntity
import com.example.seafest.data.paging.FishPagingSource
//import com.example.seafest.data.api.response.FishResponse
import com.google.gson.Gson

class FishRepository private constructor(
    private val apiService: ApiService,
    private val FishDatabase: FishDatabase
) {

    fun getAllFish(): LiveData<PagingData<FishEntity>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            remoteMediator = FishRemoteMediator(FishDatabase, apiService),
            pagingSourceFactory = {
//                QuotePagingSource(apiService)
                FishDatabase.fishDao().getAllFish()
            }
        ).liveData
    }

    fun getFish(id:Int?): LiveData<PagingData<FishEntity>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            remoteMediator = FishRemoteMediator(FishDatabase, apiService),
            pagingSourceFactory = {
//                QuotePagingSource(apiService)
                FishDatabase.fishDao().getFish(id)
            }
        ).liveData
    }

    fun getFishPaging3(id:Int): LiveData<PagingData<ListFishItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                FishPagingSource(apiService,id)
            }
        ).liveData
    }

    fun getHomeFish(id:Int?): LiveData<ResultState<FishResponse>> = liveData {
        emit(ResultState.Loading)
        try {
            val result = apiService.getListFish(page = 1, size = 15, idHabitat = id)
            emitSource(MutableLiveData(ResultState.Success(result)))
        } catch (e: Exception) {
            emit(ResultState.Error(e.message.toString()))
        }
    }

    fun searchFish(nameFish:String?): LiveData<ResultState<FishResponse>> = liveData {
        emit(ResultState.Loading)
        try {
            val result = apiService.searchFish(nameFish)
            emitSource(MutableLiveData(ResultState.Success(result)))
        } catch (e: Exception) {
            emit(ResultState.Error(e.message.toString()))
        }
    }

    fun findFish(name:String): LiveData<PagingData<FishEntity>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            remoteMediator = FishRemoteMediator(FishDatabase, apiService),
            pagingSourceFactory = {
//                QuotePagingSource(apiService)
                FishDatabase.fishDao().findFish(name)
            }
        ).liveData
    }

    suspend fun getFishDetail(storyId: Int): DetailFishResponse? {
        try {
            return apiService.getDetailFish(storyId)
        } catch (e: Exception) {
            // Handle the exception according to your application's requirements.
            // Misalnya, Anda dapat mencetak log, melempar ulang eksepsi atau mengembalikan nilai default.
            e.printStackTrace()
            return null
        }
    }

    companion object {
        fun getInstance(
            apiService: ApiService,
            database: FishDatabase
        ): FishRepository = FishRepository(apiService, database)
    }
}


