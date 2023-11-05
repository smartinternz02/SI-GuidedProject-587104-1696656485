package com.aarush.core.di


import com.aarush.core.data.local.LocalDataSource
import com.aarush.core.data.local.room.NewsDao
import com.aarush.core.data.remote.service.ApiService
import com.aarush.core.data.remote.source.RemoteDataSource
import com.aarush.core.pref.EncryptedPreferences
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