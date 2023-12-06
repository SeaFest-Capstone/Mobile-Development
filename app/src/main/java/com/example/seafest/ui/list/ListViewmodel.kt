package com.example.seafest.ui.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.seafest.data.api.response.ListFishItem
import com.example.seafest.data.local.FishEntity
import com.example.seafest.data.repository.FishRepository

class ListViewmodel(private val repository: FishRepository) : ViewModel()  {

    fun getFishPaging3(id: Int): LiveData<PagingData<ListFishItem>> {
        return repository.getFishPaging3(id)
    }
}