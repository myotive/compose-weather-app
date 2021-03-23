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
package com.example.androiddevchallenge.di

import com.example.androiddevchallenge.BuildConfig
import com.example.androiddevchallenge.network.OpenWeatherAPI
import com.example.androiddevchallenge.network.UnsplashAPI
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class UnsplashRetrofit

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class OpenWeatherRetrofit

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    fun provideUnsplashHttpClient(
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    @Provides
    fun provideLoggingInterceptor(): HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
        if (BuildConfig.DEBUG)
            this.level = HttpLoggingInterceptor.Level.BODY
        else
            this.level = HttpLoggingInterceptor.Level.NONE
    }

    @Provides
    fun provideGsonConverterFactory(): GsonConverterFactory = GsonConverterFactory.create()

    @Provides
    @OpenWeatherRetrofit
    fun provideOpenWeatherRetrofit(
        client: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ): Retrofit = Retrofit
        .Builder()
        .client(client)
        .baseUrl(BuildConfig.OPEN_WEATHER_MAPS_BASE_URL)
        .addConverterFactory(gsonConverterFactory)
        .build()

    @Provides
    @UnsplashRetrofit
    fun provideUnsplashRetrofit(
        client: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ): Retrofit = Retrofit
        .Builder()
        .client(client)
        .baseUrl(BuildConfig.UNSPASH_BASE_URL)
        .addConverterFactory(gsonConverterFactory)
        .build()

    @Singleton
    @Provides
    fun provideOpenWeatherAPI(@OpenWeatherRetrofit retrofit: Retrofit): OpenWeatherAPI =
        retrofit.create(OpenWeatherAPI::class.java)

    @Singleton
    @Provides
    fun provideUnsplashAPI(@UnsplashRetrofit retrofit: Retrofit): UnsplashAPI =
        retrofit.create(UnsplashAPI::class.java)
}
