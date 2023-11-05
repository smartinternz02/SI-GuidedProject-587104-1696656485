package com.aarush.core.model.repository

import com.aarush.core.model.model.Article
import com.aarush.core.model.model.Source
import com.aarush.core.util.exception.Failure
import com.aarush.core.util.vo.Either
import kotlinx.coroutines.flow.Flow

interface NewsRepository {
    suspend fun getSourcesByCategory(category: String, page: Int): Either<Failure, List<Source>>

    suspend fun getSourceByName(name: String, page: Int): List<Source>

    suspend fun getArticles(source: String, page: String): Either<Failure, List<Article>>

    suspend fun getArticlesByQuery(source: String, page: String): Either<Failure, List<Article>>

}