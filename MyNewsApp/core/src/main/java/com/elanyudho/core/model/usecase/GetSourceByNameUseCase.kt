package com.project.core.model.usecase

import com.project.core.model.model.Source
import com.project.core.model.repository.NewsRepository
import javax.inject.Inject

class GetSourceByNameUseCase @Inject constructor(private val repo: NewsRepository) {
    suspend fun getSourceByName(name: String, page: Int): List<Source> {
        return repo.getSourceByName(name, page)
    }
}