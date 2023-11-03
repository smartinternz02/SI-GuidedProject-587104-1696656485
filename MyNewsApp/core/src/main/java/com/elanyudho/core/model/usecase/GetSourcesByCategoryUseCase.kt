package com.project.core.model.usecase

import com.project.core.abstraction.UseCase
import com.project.core.model.model.Source
import com.project.core.model.repository.NewsRepository
import com.project.core.util.exception.Failure
import com.project.core.util.vo.Either
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSourcesByCategoryUseCase @Inject constructor(private val repo: NewsRepository) : UseCase<List<Source>, GetSourcesByCategoryUseCase.Params>() {

    data class Params(
        val category: String,
        val page: Int
    )

    override suspend fun run(params: Params): Either<Failure, List<Source>> {
        return repo.getSourcesByCategory(params.category, params.page)
    }
}