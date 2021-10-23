package com.sudo.data.di

import android.content.Context
import androidx.room.Room
import com.sudo.data.local.database.JoginguDatabase
import com.sudo.data.shared_preference.SharedPref
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Singleton
    @Provides
    fun provideDataBase(
        @ApplicationContext context: Context
    ) = JoginguDatabase.getInstance(context)

    @Singleton
    @Provides
    fun provideJoginguDao(
        database: JoginguDatabase
    ) = database.joginguDao

    @Singleton
    @Provides
    fun provideSharedPref(
        @ApplicationContext context: Context
    ) = SharedPref(context)

}