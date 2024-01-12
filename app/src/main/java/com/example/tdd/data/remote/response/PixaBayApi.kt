package com.example.tdd.data.remote.response

import com.example.tdd.BuildConfig
import com.example.tdd.other.Constants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface PixaBayApi {

    @GET("api/")
    suspend fun searchForImage(
        @Query("q") searchQuery: String,
        @Query("key") apiKey: String = Constants.API_KEY
    ): Response<ImageResponse>
}