package com.example.seafest.ui.list


import androidx.lifecycle.ViewModel
import com.example.seafest.data.repository.FishRepository

class ListViewmodel(private val repository: FishRepository) : ViewModel()  {
    fun getFishByHabitat(idHabitat: String?) = repository.getFishByHabitat(idHabitat)

}