package com.e.data.di

import com.e.data.repo.EnterAppRepoImpl
import com.e.data.repo.DomainRepoImpl
import com.e.data.repo.UserRepoImpl
import com.e.data.repo.VideosRepoImpl
import com.e.domain.repo.EnterAppRepo
import com.e.domain.repo.DomainRepo
import com.e.domain.repo.UserRepo
import com.e.domain.repo.VideosRepo
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
    abstract fun bindGetUserRepo(getUserRepoImpl: UserRepoImpl): UserRepo

    @Binds
    abstract fun bindGetVideosRepo(getVideosRepoImpl: VideosRepoImpl): VideosRepo

    @Binds
    abstract fun bindGetDomainRepo(getDomainRepoImpl: DomainRepoImpl): DomainRepo
}