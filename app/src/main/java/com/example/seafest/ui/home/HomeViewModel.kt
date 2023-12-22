package com.example.seafest.ui.home


import androidx.lifecycle.ViewModel
import com.example.seafest.data.repository.FishRepository


class HomeViewModel(private val repository: FishRepository) : ViewModel() {

    fun getFishByHabitat(idHabitat: String?) = repository.getFishByHabitat(idHabitat)


}
