package com.sudo248.jogingu.di

import com.sudo248.domain.repositories.*
import com.sudo248.domain.use_case.app.IsFirstOpenAppUseCase
import com.sudo248.domain.use_case.auth.CheckUserLoginUseCase
import com.sudo248.domain.use_case.auth.LogoutUseCase
import com.sudo248.domain.use_case.auth.SignInUserCase
import com.sudo248.domain.use_case.auth.SignUpUserCase
import com.sudo248.domain.use_case.profile.*
import com.sudo248.domain.use_case.run.AddNewRunUseCase
import com.sudo248.domain.use_case.run.DeleteRunsUseCase
import com.sudo248.domain.use_case.run.GetAllRunsUseCase
import com.sudo248.domain.use_case.run.GetAllUserRunUseCase
import com.sudo248.domain.use_case.statistic.GetRunsThisDayUseCase
import com.sudo248.domain.use_case.statistic.GetRunsThisMonthUseCase
import com.sudo248.domain.use_case.statistic.GetRunsThisWeekUseCase
import com.sudo248.domain.use_case.target.DeleteTargetUseCase
import com.sudo248.domain.use_case.target.GetTargetUseCase
import com.sudo248.domain.use_case.target.SetTargetUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(ViewModelComponent::class)
object UseCaseModule {

    @ViewModelScoped
    @Provides
    fun provideIsFirstOpenAppUseCase(
        repo: MainRepository
    ) = IsFirstOpenAppUseCase(repo)

    @ViewModelScoped
    @Provides
    fun provideGetUserUseCase(
        repo: UserRepository
    ) = GetUserUseCase(repo)

    @ViewModelScoped
    @Provides
    fun provideSetUserUseCase(
        repo: UserRepository
    ) = SetUserUseCase(repo)

    @ViewModelScoped
    @Provides
    fun provideUpdateUserUseCase(
        repo: UserRepository
    ) = UpdateUserUseCase(repo)

    @ViewModelScoped
    @Provides
    fun provideAddNewRunUseCase(
        repo: RunRepository
    ) = AddNewRunUseCase(repo)

    @ViewModelScoped
    @Provides
    fun provideDeleteRunUseCase(
        repo: RunRepository
    ) = DeleteRunsUseCase(repo)

    @ViewModelScoped
    @Provides
    fun provideGetAllRunsUseCase(
        repo: RunRepository
    ) = GetAllRunsUseCase(repo)

    @ViewModelScoped
    @Provides
    fun provideGetAllUserRunsUseCase(
        repo: RunRepository
    ) = GetAllUserRunUseCase(repo)

    @ViewModelScoped
    @Provides
    fun provideDeleteTargetUseCase(
        repo: TargetRepository
    ) = DeleteTargetUseCase(repo)

    @ViewModelScoped
    @Provides
    fun provideGetTargetUseCase(
        repo: TargetRepository
    ) = GetTargetUseCase(repo)

    @ViewModelScoped
    @Provides
    fun provideSetTargetUseCase(
        repo: TargetRepository
    ) = SetTargetUseCase(repo)

    @ViewModelScoped
    @Provides
    fun provideGetNameUserUseCase(
        repo: UserRepository
    ) = GetNameUserUseCase(repo)

    @ViewModelScoped
    @Provides
    fun provideGetBMRUserUseCase(
        repo: UserRepository
    ) = GetBMRUserUseCase(repo)

    @ViewModelScoped
    @Provides
    fun provideGetRunsThisDayUseCase(
        repo: RunRepository
    ) = GetRunsThisDayUseCase(repo)

    @ViewModelScoped
    @Provides
    fun provideGetRunsThisWeekUseCase(
        repo: RunRepository
    ) = GetRunsThisWeekUseCase(repo)

    @ViewModelScoped
    @Provides
    fun provideGetRunsThisMonthUseCase(
        repo: RunRepository
    ) = GetRunsThisMonthUseCase(repo)

    @ViewModelScoped
    @Provides
    fun provideSignInUseCase(
        repo: AuthRepository
    ) = SignInUserCase(repo)

    @ViewModelScoped
    @Provides
    fun provideSignUpUseCase(
        repo: AuthRepository
    ) = SignUpUserCase(repo)

    @ViewModelScoped
    @Provides
    fun provideLogoutUseCase(
        repo: AuthRepository
    ) = LogoutUseCase(repo)

    @ViewModelScoped
    @Provides
    fun provideCheckUserLogInUseCase(
        repo: AuthRepository
    ) = CheckUserLoginUseCase(repo)
}