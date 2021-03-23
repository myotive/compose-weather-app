/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
