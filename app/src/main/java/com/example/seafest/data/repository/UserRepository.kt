package com.example.seafest.data.repository



import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.seafest.data.ResultState
import com.example.seafest.data.api.ApiService
import com.example.seafest.data.api.response.EditProfileResponse
import com.example.seafest.data.api.response.LoginResponse
import com.example.seafest.data.api.response.ProfileResponse
import com.example.seafest.data.api.response.RegisterResponse
import com.example.seafest.data.api.response.User
import com.example.seafest.data.pref.UserPreference
import kotlinx.coroutines.flow.Flow
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import retrofit2.HttpException
import java.io.File

class UserRepository private constructor(
    private val apiService: ApiService,
    private val userPreference: UserPreference
) {

    suspend fun saveSession(user: User) {
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<User> {
        return userPreference.getSession()
    }


    suspend fun logout() {
        userPreference.logout()
    }


    fun login(email: String, password: String): LiveData<ResultState<LoginResponse>> = liveData {
        emit(ResultState.Loading)
        try {
            val result = apiService.login(email, password)
            emit(ResultState.Success(result))
        } catch (e: Exception) {
            when (e) {
                is HttpException -> {
                    // Ada response dari server, coba ekstrak pesan kesalahan dari body
                    val errorBody = e.response()?.errorBody()?.string()
                    val errorMessage = extractErrorMessage(errorBody)
                    emit(ResultState.Error(errorMessage ?: "An unknown error occurred"))
                }
                is IOException -> {
                    emit(ResultState.Error("Network error. Please check your connection and try again."))
                }
                else -> {
                    emit(ResultState.Error("An unknown error occurred"))
                }
            }
        }
    }

    private fun extractErrorMessage(errorBody: String?): String? {
        return try {
            val json = JSONObject(errorBody)
            json.getString("message")
        } catch (e: JSONException) {
            null
        }
    }

    fun register(username:String, email: String, password: String, confirmPassword:String): LiveData<ResultState<RegisterResponse>> = liveData {
        emit(ResultState.Loading)
        try {
            val result = apiService.register(username,email,password,confirmPassword)
            emit(ResultState.Success(result))
        } catch (e: Exception) {
            when (e) {
                is HttpException -> {

                    val errorBody = e.response()?.errorBody()?.string()
                    val errorMessage = extractErrorMessage(errorBody)
                    emit(ResultState.Error(errorMessage ?: "An unknown error occurred"))
                }
                is IOException -> {
                    emit(ResultState.Error("Network error. Please check your connection and try again."))
                }
                else -> {
                    emit(ResultState.Error("An unknown error occurred"))
                }
            }
        }
    }
    fun getProfile(idUser: String?): LiveData<ResultState<ProfileResponse>> = liveData {
        emit(ResultState.Loading)
        try {
            val result = apiService.getProfile(idUser)
            emit(ResultState.Success(result))
        } catch (e: Exception) {
            when (e) {
                is HttpException -> {
                    // Ada response dari server, coba ekstrak pesan kesalahan dari body
                    val errorBody = e.response()?.errorBody()?.string()
                    val errorMessage = extractErrorMessage(errorBody)
                    emit(ResultState.Error(errorMessage ?: "An unknown error occurred"))
                }
                is IOException -> {
                    emit(ResultState.Error("Network error. Please check your connection and try again."))
                }
                else -> {
                    emit(ResultState.Error("An unknown error occurred"))
                }
            }
        }
    }

    fun updateProfile(
        idUser: String,
        photoProfile: File,
        nomorTelepon: String,
        alamat: String
    ): LiveData<ResultState<EditProfileResponse>> = liveData {
        emit(ResultState.Loading)
        val requestBody = nomorTelepon.toRequestBody("text/plain".toMediaType())
        val requestBody2 = alamat.toRequestBody("text/plain".toMediaType())
        val requestImageFile = photoProfile.asRequestBody("image/jpeg".toMediaType())
        val multipartBody = MultipartBody.Part.createFormData(
            "photoProfile",
            photoProfile.name,
            requestImageFile
        )
        try {
            val result = apiService.updateProfile(idUser, multipartBody, requestBody, requestBody2)
            emit(ResultState.Success(result))
        } catch (e: Exception) {
            when (e) {
                is HttpException -> {
                    // Ada response dari server, coba ekstrak pesan kesalahan dari body
                    val errorBody = e.response()?.errorBody()?.string()
                    val errorMessage = extractErrorMessage(errorBody)
                    emit(ResultState.Error(errorMessage ?: "An unknown error occurred"))
                }
                is IOException -> {
                    emit(ResultState.Error("Network error. Please check your connection and try again."))
                }
                else -> {
                    emit(ResultState.Error("An unknown error occurred"))
                }
            }
        }
    }

    fun updateProfile(
        idUser: String,
        nomorTelepon: String,
        alamat: String
    ): LiveData<ResultState<EditProfileResponse>> = liveData {
        emit(ResultState.Loading)
        val requestBody = nomorTelepon.toRequestBody("text/plain".toMediaType())
        val requestBody2 = alamat.toRequestBody("text/plain".toMediaType())


        try {
            val result = apiService.updateProfile(idUser, requestBody, requestBody2)
            emit(ResultState.Success(result))
        } catch (e: Exception) {
            when (e) {
                is HttpException -> {
                    // Ada response dari server, coba ekstrak pesan kesalahan dari body
                    val errorBody = e.response()?.errorBody()?.string()
                    val errorMessage = extractErrorMessage(errorBody)
                    emit(ResultState.Error(errorMessage ?: "An unknown error occurred"))
                }
                is IOException -> {
                    emit(ResultState.Error("Network error. Please check your connection and try again."))
                }
                else -> {
                    emit(ResultState.Error("An unknown error occurred"))
                }
            }
        }
    }

    companion object {
        fun getInstance(
            apiService: ApiService,
            userPreference: UserPreference
        ): UserRepository = UserRepository(apiService, userPreference)
    }
}