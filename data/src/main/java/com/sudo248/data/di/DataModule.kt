package com.sudo248.data.di

import android.content.Context
import androidx.room.Room
import com.sudo248.data.local.database.JoginguDatabase
import com.sudo248.data.local.database.dao.JoginguDao
import com.sudo248.data.repositories.MainRepositoryImpl
import com.sudo248.data.shared_preference.SharedPref
import com.sudo248.domain.repositories.MainRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
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
    fun provideIoDispatcher(): CoroutineDispatcher = Dispatchers.IO

}