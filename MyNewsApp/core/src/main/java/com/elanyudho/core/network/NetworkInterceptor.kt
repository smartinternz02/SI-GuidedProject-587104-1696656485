package com.project.core.network

import com.project.core.pref.EncryptedPreferences
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class NetworkInterceptor() : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val newRequest =
            chain.request().newBuilder().build()

        return chain.proceed(newRequest)
    }
}