package com.project.core.di

import com.project.core.data.remote.mapper.ArticlesMapper
import com.project.core.data.remote.mapper.SourcesMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.scopes.ActivityScoped

@Module
@InstallIn(ActivityComponent::class)
object MapperModule {
    @Provides
    @ActivityScoped
    fun provideSourcesMapper() = SourcesMapper()

    @Provides
    @ActivityScoped
    fun provideArticlesMapper() = ArticlesMapper()
}