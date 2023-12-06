package com.example.seafest.data.api

import com.example.seafest.data.api.response.DetailFishResponse
import com.example.seafest.data.api.response.FishResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("fish")
    suspend fun getListFish(
        @Query("page") page: Int? = 1,
        @Query("size") size: Int? = 5,
        @Query("idHabitat") idHabitat: Int?
    ): FishResponse

    @GET("fish")
    suspend fun getListFish(
        @Query("page") page: Int? = 1,
        @Query("size") size: Int? = 5
    ): FishResponse

    @GET("fish/detail/{id}")
    suspend fun getDetailFish(@Path("id") id: Int): DetailFishResponse

    @GET("search/fish")
    suspend fun searchFish(
        @Query("nameFish") nameFish: String?,
    ): FishResponse


}