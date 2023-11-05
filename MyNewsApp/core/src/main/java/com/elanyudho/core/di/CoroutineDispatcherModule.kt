package com.aarush.core.di

import com.aarush.core.dispatcher.DispatcherProvider
import com.aarush.core.dispatcher.DispatcherProviderImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.scopes.ActivityScoped

@Module
@InstallIn(ActivityComponent::class)
abstract class CoroutinesDispatcherModule {

    @Binds
    @ActivityScoped
    abstract fun bindDispatcherProvider(dispatcherProvider: DispatcherProviderImpl): DispatcherProvider

}