package com.example.androiddevchallenge.di

import android.content.Context
import com.example.androiddevchallenge.location.LocationHelper
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.scopes.ActivityScoped

@Module
@InstallIn(ActivityComponent::class)
class LocationModule {
    @ActivityScoped
    @Provides
    fun provideLocationHelper(fusedLocationProviderClient: FusedLocationProviderClient) =
        LocationHelper(fusedLocationProviderClient)

    @ActivityScoped
    @Provides
    fun provideFusedLocationProviderClient(@ActivityContext context: Context): FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)
}