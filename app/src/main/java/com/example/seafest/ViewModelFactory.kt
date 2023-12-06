package com.example.seafest

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.seafest.data.repository.FishRepository
import com.example.seafest.di.Injection
import com.example.seafest.ui.detail.DetailViewModel
import com.example.seafest.ui.home.HomeViewModel
import com.example.seafest.ui.list.ListViewmodel
import com.example.seafest.ui.search.SearchViewModel

class ViewModelFactory(private val fishRepository: FishRepository) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(fishRepository) as T
            }

            modelClass.isAssignableFrom(ListViewmodel::class.java) -> {
                ListViewmodel(fishRepository) as T
            }

            modelClass.isAssignableFrom(DetailViewModel::class.java) -> {
                DetailViewModel(fishRepository) as T
            }

            modelClass.isAssignableFrom(SearchViewModel::class.java) -> {
                SearchViewModel(fishRepository) as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class " + modelClass.name)
        }
    }

    companion object{
        @JvmStatic
        fun getInstance(context: Context): ViewModelFactory {
            return ViewModelFactory(Injection.provideFishRepository(context))
        }
    }
}