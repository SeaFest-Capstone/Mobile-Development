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

    private val _detailFishResponse = MutableLiveData<Fish?>()
    val detailFishResponse: LiveData<Fish?> get() = _detailFishResponse

    fun getFishDetail(storyId: Int) {
        viewModelScope.launch {
            try {
                val response = fishRepository.getFishDetail(storyId)
                _detailFishResponse.value = response?.fish
            } catch (e: Exception) {
                // Handle the exception, for example, show an error message to the user.
                e.printStackTrace()
            }
        }
    }
}