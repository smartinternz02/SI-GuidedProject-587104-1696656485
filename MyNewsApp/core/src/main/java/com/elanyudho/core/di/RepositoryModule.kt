package com.aarush.core.di

import com.aarush.core.data.repository.NewsRepositoryImpl
import com.aarush.core.model.repository.NewsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.scopes.ActivityScoped

@Module
@InstallIn(ActivityComponent::class)
abstract class RepositoryModule {
    @Binds
    @ActivityScoped
    abstract fun bindNewsRepository(repositoryImpl: NewsRepositoryImpl): NewsRepository

}