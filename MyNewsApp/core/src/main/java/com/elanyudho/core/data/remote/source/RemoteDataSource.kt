package com.aarush.core.data.remote.source

import com.aarush.core.data.remote.response.ArticlesResponse
import com.aarush.core.data.remote.response.SourcesResponse
import com.aarush.core.data.remote.service.ApiService
import com.aarush.core.pref.EncryptedPreferences
import com.aarush.core.util.pagination.PagingConstant
import com.aarush.core.util.vo.Either
import com.aarush.core.util.exception.Failure
import javax.inject.Inject

class RemoteDataSource
@Inject constructor(private val api: ApiService, private val encryptedPreferences: EncryptedPreferences) : RemoteSafeRequest() {

    suspend fun getSourcesByCategory(category: String): Either<Failure, SourcesResponse> =
        request {
            api.getSourcesByCategory(category, encryptedPreferences.encryptedToken)
        }

    suspend fun getArticles(source: String, page: String): Either<Failure, ArticlesResponse> =
        request {
            api.getArticles(source, encryptedPreferences.encryptedToken, page, PagingConstant.BATCH_SIZE.toString())
        }

    suspend fun getArticlesByQuery(source: String, page: String): Either<Failure, ArticlesResponse> =
        request {
            api.getArticlesByQuery(source, encryptedPreferences.encryptedToken, page, PagingConstant.BATCH_SIZE.toString())
        }

}