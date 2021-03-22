package com.example.androiddevchallenge.di

import android.app.Activity
import android.content.Context
import com.example.androiddevchallenge.location.LocationPermissionHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.scopes.ActivityScoped
import java.lang.ref.WeakReference

@Module
@InstallIn(ActivityComponent::class)
class PermissionModule {
    @ActivityScoped
    @Provides
    fun providePermissionHelper(@ActivityContext context: Context): LocationPermissionHelper =
        LocationPermissionHelper(WeakReference(context as Activity))
}