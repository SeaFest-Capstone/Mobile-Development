package com.example.seafest.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.seafest.data.ResultState
import com.example.seafest.data.api.response.FishResponse
import com.example.seafest.data.local.FishEntity
import com.example.seafest.data.repository.FishRepository
import kotlinx.coroutines.launch
import java.io.File

class HomeViewModel(private val repository: FishRepository) : ViewModel() {

    fun getHomeFish(id: Int?)= repository.getHomeFish(id)


    fun searchFish(name: String): LiveData<PagingData<FishEntity>> {
        return repository.findFish(name)
    }
}