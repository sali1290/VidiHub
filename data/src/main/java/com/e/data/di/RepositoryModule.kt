package com.e.data.di

import com.e.data.repo.EnterAppRepoImpl
import com.e.data.repo.GetUserRepoImpl
import com.e.domain.repo.EnterAppRepo
import com.e.domain.repo.GetUserRepo
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindEnterAppRepo(enterAppRepoImpl: EnterAppRepoImpl): EnterAppRepo

    @Binds
    abstract fun bindGetUserRepo(getUserRepoImpl: GetUserRepoImpl): GetUserRepo
}