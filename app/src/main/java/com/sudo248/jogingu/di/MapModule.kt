package com.sudo248.jogingu.di

import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityScoped
import dagger.hilt.android.scopes.ServiceScoped
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent
import java.util.Locale


@Module
@InstallIn(ViewModelComponent::class)
object MapModule {

    @ViewModelScoped
    @Provides
    fun provideGeoCoder(
        @ApplicationContext context: Context
    ): Geocoder = Geocoder(context, Locale("vi", "VN"))

}