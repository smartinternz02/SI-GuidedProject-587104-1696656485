package com.project.core.model.usecase

import com.project.core.abstraction.UseCase
import com.project.core.model.model.Article
import com.project.core.model.repository.NewsRepository
import com.project.core.util.exception.Failure
import com.project.core.util.vo.Either
import javax.inject.Inject

class GetArticlesUseCase @Inject constructor(private val repo: NewsRepository) : UseCase<List<Article>, GetArticlesUseCase.Params>() {

    data class Params(
        val source: String,
        val page: String
    )

    override suspend fun run(params: Params): Either<Failure, List<Article>> {
        return repo.getArticles(params.source, params.page)
    }
}