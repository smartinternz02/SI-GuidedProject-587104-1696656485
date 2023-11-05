package com.aarush.core.model.usecase

import com.aarush.core.abstraction.UseCase
import com.aarush.core.model.model.Source
import com.aarush.core.model.repository.NewsRepository
import com.aarush.core.util.exception.Failure
import com.aarush.core.util.vo.Either
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