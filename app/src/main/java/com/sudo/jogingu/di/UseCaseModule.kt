package com.sudo.jogingu.di

import com.sudo.domain.repositories.MainRepository
import com.sudo.domain.use_case.notification.AddNewNotification
import com.sudo.domain.use_case.profile.GetUserUseCase
import com.sudo.domain.use_case.profile.SetUserUseCase
import com.sudo.domain.use_case.profile.UpdateUserUseCase
import com.sudo.domain.use_case.run.AddNewRunUseCase
import com.sudo.domain.use_case.run.DeleteRunsUseCase
import com.sudo.domain.use_case.run.GetAllRunsUseCase
import com.sudo.domain.use_case.target.DeleteTargetUseCase
import com.sudo.domain.use_case.target.GetTargetUseCase
import com.sudo.domain.use_case.target.SetTargetUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Singleton
    @Provides
    fun provideAddNewNotificationUseCase(

    ) = AddNewNotification()

    @Singleton
    @Provides
    fun provideGetUserUseCase(
        repo: MainRepository
    ) = GetUserUseCase(repo)

    @Singleton
    @Provides
    fun provideSetUserUseCase(
        repo: MainRepository
    ) = SetUserUseCase(repo)

    @Singleton
    @Provides
    fun provideUpdateUserUseCase(
        repo: MainRepository
    ) = UpdateUserUseCase(repo)

    @Singleton
    @Provides
    fun provideAddNewRunUseCase(
        repo: MainRepository
    ) = AddNewRunUseCase(repo)

    @Singleton
    @Provides
    fun provideDeleteRunUseCase(
        repo: MainRepository
    ) = DeleteRunsUseCase(repo)

    @Singleton
    @Provides
    fun provideGetAllRunsUseCase(
        repo: MainRepository
    ) = GetAllRunsUseCase(repo)

    @Singleton
    @Provides
    fun provideDeleteTargetUseCase(
        repo: MainRepository
    ) = DeleteTargetUseCase(repo)

    @Singleton
    @Provides
    fun provideGetTargetUseCase(
        repo: MainRepository
    ) = GetTargetUseCase(repo)

    @Singleton
    @Provides
    fun provideSetTargetUseCase(
        repo: MainRepository
    ) = SetTargetUseCase(repo)

}