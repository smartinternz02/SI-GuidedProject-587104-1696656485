package com.aarush.core.model.usecase

import com.aarush.core.model.model.Source
import com.aarush.core.model.repository.NewsRepository
import javax.inject.Inject

class GetSourceByNameUseCase @Inject constructor(private val repo: NewsRepository) {
    suspend fun getSourceByName(name: String, page: Int): List<Source> {
        return repo.getSourceByName(name, page)
    }
}