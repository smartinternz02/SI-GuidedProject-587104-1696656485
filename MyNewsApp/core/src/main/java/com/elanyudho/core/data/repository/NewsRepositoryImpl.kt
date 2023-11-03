package com.project.core.data.repository

import android.util.Log
import com.project.core.data.local.LocalDataSource
import com.project.core.data.remote.mapper.ArticlesMapper
import com.project.core.data.remote.mapper.SourcesMapper
import com.project.core.data.remote.source.RemoteDataSource
import com.project.core.model.model.Article
import com.project.core.model.model.Source
import com.project.core.model.repository.NewsRepository
import com.project.core.util.exception.Failure
import com.project.core.util.extension.STATUS_OK
import com.project.core.util.pagination.PagingConstant
import com.project.core.util.vo.Either
import com.project.core.util.vo.RequestResults
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
    private val sourcesMapper: SourcesMapper,
    private val articlesMapper: ArticlesMapper
) : NewsRepository {

    override suspend fun getSourcesByCategory(category: String, page: Int): Either<Failure, List<Source>> {
        return when (val response = remoteDataSource.getSourcesByCategory(category)) {
            is Either.Success -> {
                if (response.body.status == STATUS_OK) {
                    val data = sourcesMapper.mapToDomain(response.body)
                    val offset = (page - 1) * PagingConstant.BATCH_SIZE
                    localDataSource.insertSources(data)
                    Either.Success(localDataSource.getSourcesByCategory(category,offset))
                } else {
                    Either.Error(Failure(RequestResults.THERE_IS_ERROR, Throwable(message = response.body.message), response.body.code ?: ""))
                }
            }

            is Either.Error -> {
                Either.Error(response.failure)
            }
        }
    }

    override suspend fun getSourceByName(name: String, page: Int): List<Source> {
        val offset = (page - 1) * PagingConstant.BATCH_SIZE
        return localDataSource.getSourcesByName(name, offset)
    }

    override suspend fun getArticles(source: String, page: String): Either<Failure, List<Article>> {
        return when (val response = remoteDataSource.getArticles(source, page)) {
            is Either.Success -> {
                if (response.body.status == STATUS_OK) {
                    Either.Success(articlesMapper.mapToDomain(response.body))
                } else {
                    Either.Error(Failure(RequestResults.THERE_IS_ERROR, Throwable(message = response.body.message), response.body.code ?: ""))
                }
            }

            is Either.Error -> {
                Either.Error(response.failure)
            }
        }
    }

    override suspend fun getArticlesByQuery(
        source: String,
        page: String
    ): Either<Failure, List<Article>> {
        return when (val response = remoteDataSource.getArticlesByQuery(source, page)) {
            is Either.Success -> {
                if (response.body.status == STATUS_OK) {
                    Either.Success(articlesMapper.mapToDomain(response.body))
                } else {
                    Either.Error(Failure(RequestResults.THERE_IS_ERROR, Throwable(message = response.body.message), response.body.code ?: ""))
                }
            }

            is Either.Error -> {
                Either.Error(response.failure)
            }
        }
    }
}