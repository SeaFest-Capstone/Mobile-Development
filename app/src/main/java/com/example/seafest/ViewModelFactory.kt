package com.example.seafest

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.seafest.data.repository.FishRepository
import com.example.seafest.data.repository.UserRepository
import com.example.seafest.di.Injection
import com.example.seafest.ui.cart.CartViewModel
import com.example.seafest.ui.detail.DetailViewModel
import com.example.seafest.ui.editprofile.EditProfileViewModel
import com.example.seafest.ui.history.HistoryViewModel
import com.example.seafest.ui.home.HomeViewModel
import com.example.seafest.ui.list.ListViewmodel
import com.example.seafest.ui.login.LoginViewModel
import com.example.seafest.ui.main.MainViewModel
import com.example.seafest.ui.profile.ProfileViewModel
import com.example.seafest.ui.register.RegisterViewModel
import com.example.seafest.ui.scanner.ScannerViewModel

class ViewModelFactory(private val fishRepository: FishRepository,private val userRepository: UserRepository) :
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
                DetailViewModel(fishRepository, userRepository) as T
            }

            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(userRepository) as T
            }

            modelClass.isAssignableFrom(CartViewModel::class.java) -> {
                CartViewModel(fishRepository, userRepository) as T
            }

            modelClass.isAssignableFrom(ProfileViewModel::class.java) -> {
                ProfileViewModel(userRepository) as T
            }

            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(userRepository) as T
            }

            modelClass.isAssignableFrom(ScannerViewModel::class.java) -> {
                ScannerViewModel(fishRepository, userRepository) as T
            }

            modelClass.isAssignableFrom(EditProfileViewModel::class.java) -> {
                EditProfileViewModel(userRepository) as T
            }

            modelClass.isAssignableFrom(HistoryViewModel::class.java) -> {
                HistoryViewModel(fishRepository, userRepository) as T
            }

            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
                RegisterViewModel(userRepository) as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class " + modelClass.name)
        }
    }

    companion object{
        @JvmStatic
        fun getInstance(context: Context): ViewModelFactory {
            return ViewModelFactory(Injection.provideFishRepository(context),(Injection.provideRepository(context)))
        }
    }
}