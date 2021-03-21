package com.example.androiddevchallenge.network

import com.example.androiddevchallenge.BuildConfig
import com.example.androiddevchallenge.network.models.UnsplashSearchPhotoResult
import retrofit2.http.GET
import retrofit2.http.Query


interface UnsplashAPI {
    @GET("search/photos")
    suspend fun search(
        @Query("query") query: String,
        @Query("client_id") clientId: String = BuildConfig.UNSPLASH_API_KEY
    ): UnsplashSearchPhotoResult
}