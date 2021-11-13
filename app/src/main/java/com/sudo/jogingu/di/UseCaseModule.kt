package com.sudo.jogingu.di

import com.sudo.domain.repositories.MainRepository
import com.sudo.domain.use_case.app.IsFirstOpenApp
import com.sudo.domain.use_case.notification.AddNewNotification
import com.sudo.domain.use_case.profile.*
import com.sudo.domain.use_case.run.AddNewRunUseCase
import com.sudo.domain.use_case.run.DeleteRunsUseCase
import com.sudo.domain.use_case.run.GetAllRunsUseCase
import com.sudo.domain.use_case.statistic.GetRunsThisDayUseCase
import com.sudo.domain.use_case.statistic.GetRunsThisMonthUseCase
import com.sudo.domain.use_case.statistic.GetRunsThisWeekUseCase
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
    fun provideIsFirstOpenAppUseCase(
        repo: MainRepository
    ) = IsFirstOpenApp(repo)

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

    @Singleton
    @Provides
    fun provideGetNameUserUseCase(
        repo: MainRepository
    ) = GetNameUserUseCase(repo)

    @Singleton
    @Provides
    fun provideGetBMRUserUseCase(
        repo: MainRepository
    ) = GetBMRUserUseCase(repo)

    @Singleton
    @Provides
    fun provideGetRunsThisDayUseCase(
        repo: MainRepository
    ) = GetRunsThisDayUseCase(repo)

    @Singleton
    @Provides
    fun provideGetRunsThisWeekUseCase(
        repo: MainRepository
    ) = GetRunsThisWeekUseCase(repo)

    @Singleton
    @Provides
    fun provideGetRunsThisMonthUseCase(
        repo: MainRepository
    ) = GetRunsThisMonthUseCase(repo)

}