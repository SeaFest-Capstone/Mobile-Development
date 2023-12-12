package com.example.seafest.data.api

import com.example.seafest.data.api.response.DetailFishResponse
import com.example.seafest.data.api.response.FishResponse
import com.example.seafest.data.api.response.LoginResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("fish/habitat/{idHabitat}")
    suspend fun getFishByHabitat(@Path("idHabitat") idHabitat: Int?): FishResponse


    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginResponse

    @GET("fish/detail/{id}")
    suspend fun getDetailFish(@Path("id") id: Int?): DetailFishResponse

    @GET("search/fish")
    suspend fun searchFish(
        @Query("nameFish") nameFish: String?,
    ): FishResponse


}