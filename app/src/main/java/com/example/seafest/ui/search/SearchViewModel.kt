package com.example.seafest.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.seafest.data.ResultState
import com.example.seafest.data.api.response.FishResponse
import com.example.seafest.data.repository.FishRepository
import kotlinx.coroutines.launch

class SearchViewModel(private val fishRepository: FishRepository) : ViewModel() {

//    private val _fishLiveData = MutableLiveData<ResultState<FishResponse>>()
//    val fishLiveData: LiveData<ResultState<FishResponse>> get() = _fishLiveData

//    fun searchFish(nameFish: String?) {
//        viewModelScope.launch {
//            _fishLiveData.value = ResultState.Loading
//            try {
//                val result = fishRepository.searchFish(nameFish)
//                _fishLiveData.value = ResultState.Success(result)
//            } catch (e: Exception) {
//                _fishLiveData.value = ResultState.Error(e.message.toString())
//            }
//        }
//    }
private val _fishLiveData = MutableLiveData<ResultState<FishResponse>?>()
    val fishLiveData: LiveData<ResultState<FishResponse>?> get() = _fishLiveData

    fun searchFish(nameFish: String?) {
        viewModelScope.launch {
            _fishLiveData.value = ResultState.Loading
            try {
                val result = fishRepository.searchFish(nameFish).value
                // Ambil nilai dari LiveData yang dihasilkan oleh fishRepository.searchFish
                _fishLiveData.value = result
            } catch (e: Exception) {
                _fishLiveData.value = ResultState.Error(e.message.toString())
            }
        }
    }
    fun searchFishc(name: String?)= fishRepository.searchFish(name)
}
