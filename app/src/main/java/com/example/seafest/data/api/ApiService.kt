package com.example.seafest.data.api

import com.example.seafest.data.api.response.CheckoutResponse
import com.example.seafest.data.api.response.AddScanResponse
import com.example.seafest.data.api.response.CartResponse
import com.example.seafest.data.api.response.DetailFishResponse
import com.example.seafest.data.api.response.EditProfileResponse
import com.example.seafest.data.api.response.FishResponse
import com.example.seafest.data.api.response.HistoryResponse
import com.example.seafest.data.api.response.LoginResponse
import com.example.seafest.data.api.response.ProfileResponse
import com.example.seafest.data.api.response.RegisterResponse
import com.example.seafest.data.api.response.ViewCartResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

interface ApiService {

    @GET("fish/habitat/{idHabitat}")
    suspend fun getFishByHabitat(@Path("idHabitat") idHabitat: String?): FishResponse

    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginResponse


    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("username")username: String,
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("confirmPassword") confirmPassword: String
    ): RegisterResponse

    @GET("profile/{idUser}")
    suspend fun getProfile(@Path("idUser") idUser: String?) : ProfileResponse


    @GET("fish/detail/{id}")
    suspend fun getDetailFish(@Path("id") id: String?): DetailFishResponse

    @GET("scan-history/{idUser}")
    suspend fun getHistory(
        @Path("idUser") idUser: String?): HistoryResponse

    @GET("view-cart/{idUser}")
    suspend fun getCart(
        @Path("idUser") idUser: String?): ViewCartResponse

    @FormUrlEncoded
    @POST("add-to-cart/{idUser}")
    suspend fun addCart(
        @Path("idUser") idUser: String?,
        @Field("fishIdCart") fishIdCart: String?,
    ): CartResponse

    @Multipart
    @PUT("updateProfile/{idUser}")
    suspend fun updateProfile(
        @Path("idUser") idUser: String?,
        @Part photoProfile: MultipartBody.Part,
        @Part("noTelp") noTelp: RequestBody,
        @Part("alamat") alamat: RequestBody,
    ): EditProfileResponse

    @Multipart
    @PUT("updateProfile/{idUser}")
    suspend fun updateProfile(
        @Path("idUser") idUser: String?,
        @Part("noTelp") noTelp: RequestBody,
        @Part("alamat") alamat: RequestBody,
    ): EditProfileResponse

    @FormUrlEncoded
    @POST("checkout/{idUser}")
    suspend fun checkout(
        @Path("idUser") idUser: String?,
        @Field("fishIdCart") fishIdCart: String?,
    ): CheckoutResponse

    @Multipart
    @POST("addScanResult")
    suspend fun addScanResult(
        @Part fishPhoto: MultipartBody.Part,
        @Part("userId") userId: RequestBody,
        @Part("fishStatus") fishStatus: RequestBody,
        @Part("fishName") fishName: RequestBody,
    ): AddScanResponse
}