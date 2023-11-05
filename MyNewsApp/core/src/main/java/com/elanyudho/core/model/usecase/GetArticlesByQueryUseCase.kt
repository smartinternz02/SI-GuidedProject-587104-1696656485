package com.aarush.core.model.usecase

import com.aarush.core.abstraction.UseCase
import com.aarush.core.model.model.Article
import com.aarush.core.model.repository.NewsRepository
import com.aarush.core.util.exception.Failure
import com.aarush.core.util.vo.Either
import javax.inject.Inject

class GetArticlesByQueryUseCase @Inject constructor(private val repo: NewsRepository) : UseCase<List<Article>, GetArticlesByQueryUseCase.Params>() {

    data class Params(
        val source: String,
        val page: String
    )

    override suspend fun run(params: Params): Either<Failure, List<Article>> {
        return repo.getArticlesByQuery(params.source, params.page)
    }
}