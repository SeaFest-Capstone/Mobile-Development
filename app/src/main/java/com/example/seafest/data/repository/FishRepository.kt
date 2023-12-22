package com.example.seafest.data.repository


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.example.seafest.data.api.response.CheckoutResponse
import com.example.seafest.data.ResultState
import com.example.seafest.data.api.ApiService
import com.example.seafest.data.api.response.AddScanResponse
import com.example.seafest.data.api.response.CartResponse
import com.example.seafest.data.api.response.DetailFishResponse
import com.example.seafest.data.api.response.FishResponse
import com.example.seafest.data.api.response.HistoryResponse
import com.example.seafest.data.api.response.ViewCartResponse
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.HttpException
import java.io.File
import java.io.IOException


class FishRepository private constructor(
    private val apiService: ApiService
) {
    fun getFishByHabitat(idHabitat: String?): LiveData<ResultState<FishResponse>> = liveData {
        emit(ResultState.Loading)
        try {
            val result = apiService.getFishByHabitat(idHabitat)
            emitSource(MutableLiveData(ResultState.Success(result)))
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

    fun fishDetail(fishId: String?): LiveData<ResultState<DetailFishResponse>> = liveData {
        emit(ResultState.Loading)
        try {
            val result = apiService.getDetailFish(fishId)
            emitSource(MutableLiveData(ResultState.Success(result)))
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

    fun getCart(userId: String?): LiveData<ResultState<ViewCartResponse>> = liveData {
        emit(ResultState.Loading)
        try {
            val result = apiService.getCart(userId)
            emitSource(MutableLiveData(ResultState.Success(result)))
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

    fun addCart(idUser: String?,fishIdCart:String?):LiveData<ResultState<CartResponse>> = liveData {
        emit(ResultState.Loading)
        try {
            val result = apiService.addCart(idUser, fishIdCart)
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

    fun fishHistory(idUser: String?): LiveData<ResultState<HistoryResponse>> = liveData {
        emit(ResultState.Loading)
        try {
            val result = apiService.getHistory(idUser)
            emitSource(MutableLiveData(ResultState.Success(result)))
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

    fun checkoutResponse(idUser: String?,fishIdCart:String?):LiveData<ResultState<CheckoutResponse>> = liveData {
        emit(ResultState.Loading)
        try {
            val result = apiService.checkout(idUser, fishIdCart)
            emit(ResultState.Success(result))
        } catch (e: HttpException) {
            emit(ResultState.Error(e.message.toString()))
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

    fun addScan(
        fishPhoto: File,
        userId: String,
        fishStatus: String,
        fishName: String
    ): LiveData<ResultState<AddScanResponse>> = liveData {
        emit(ResultState.Loading)
        val requestBody0 = userId.toRequestBody("text/plain".toMediaType())
        val requestBody1 = fishStatus.toRequestBody("text/plain".toMediaType())
        val requestBody2 = fishName.toRequestBody("text/plain".toMediaType())
        val requestImageFile = fishPhoto.asRequestBody("image/jpeg".toMediaType())
        val multipartBody = MultipartBody.Part.createFormData(
            "fishPhoto",
            fishPhoto.name,
            requestImageFile
        )
        try {
            val result = apiService.addScanResult(multipartBody,requestBody0,requestBody1,requestBody2)
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

    private fun extractErrorMessage(errorBody: String?): String? {
        return try {
            val json = JSONObject(errorBody)
            json.getString("message")
        } catch (e: JSONException) {
            null
        }
    }
    companion object {
        fun getInstance(
            apiService: ApiService,
        ): FishRepository = FishRepository(apiService)
    }
}


