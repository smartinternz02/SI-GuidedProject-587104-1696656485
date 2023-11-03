package com.project.core.di


import com.project.core.data.local.LocalDataSource
import com.project.core.data.local.room.NewsDao
import com.project.core.data.remote.service.ApiService
import com.project.core.data.remote.source.RemoteDataSource
import com.project.core.pref.EncryptedPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.scopes.ActivityScoped

@Module
@InstallIn(ActivityComponent::class)
object DataSourceModule {

    @Provides
    @ActivityScoped
    fun provideRemoteDataSource(apiService: ApiService, encryptedPreferences: EncryptedPreferences) = RemoteDataSource(apiService, encryptedPreferences)

    @Provides
    @ActivityScoped
    fun provideLocalDataSource(favoriteDao: NewsDao) = LocalDataSource(favoriteDao)
}