package com.aarush.core.pref

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.aarush.core.BuildConfig
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class EncryptedPreferences
@Inject constructor(
    @ApplicationContext context: Context
) {
    private val masterKey = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)

    private val sp: SharedPreferences by lazy {
        EncryptedSharedPreferences.create(
            SECURE_PREF_NAME,
            masterKey,
            context,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    private val spe : SharedPreferences.Editor by lazy {
        sp.edit()
    }

    fun clear() {
        spe.clear().apply()
    }

    var encryptedToken: String
        get() = sp.getString(SECURE_SP_TOKEN, BuildConfig.TOKEN) ?: ""
        set(value) = spe.putString(SECURE_SP_TOKEN, value).apply()

    var baseUrl: String
        get() = sp.getString(BASE_URL, BuildConfig.BASE_URL) ?: ""
        set(value) = spe.putString(BASE_URL, value).apply()

    var filter: Int
        get() = sp.getInt(FILTER, 0)
        set(value) = spe.putInt(FILTER, value).apply()

    companion object {
        private const val SECURE_PREF_NAME = "com.aarush.newapp"
        private const val SECURE_SP_TOKEN = "pref_token"
        private const val BASE_URL = "base_url"
        private const val FILTER = "filter"

    }

}
