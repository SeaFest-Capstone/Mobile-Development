package com.example.seafest.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.seafest.data.api.response.DetailFishResponse
import com.example.seafest.data.api.response.Fish
import com.example.seafest.data.repository.FishRepository
import kotlinx.coroutines.launch

class DetailViewModel(private val fishRepository: FishRepository) : ViewModel() {

    fun getFishDetail(fishId: Int?) = fishRepository.fishDetail(fishId)
}