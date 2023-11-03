package com.project.core.data.remote.source

import com.project.core.data.remote.response.ArticlesResponse
import com.project.core.data.remote.response.SourcesResponse
import com.project.core.data.remote.service.ApiService
import com.project.core.pref.EncryptedPreferences
import com.project.core.util.pagination.PagingConstant
import com.project.core.util.vo.Either
import com.project.core.util.exception.Failure
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