package com.example.androiddevchallenge.di

import com.example.androiddevchallenge.repository.IWeatherRepository
import com.example.androiddevchallenge.repository.WeatherRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindsWeatherRepository(
        weatherRepository: WeatherRepository
    ) : IWeatherRepository
}