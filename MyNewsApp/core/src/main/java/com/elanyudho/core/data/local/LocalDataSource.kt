package com.project.core.data.local

import com.project.core.data.local.room.NewsDao
import com.project.core.model.model.Source
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalDataSource @Inject constructor(private val newsDao: NewsDao) {

    fun insertSources(sources: List<Source>) = newsDao.insertSources(sources)

    fun getSourcesByCategory(category: String, offset: Int): List<Source> = newsDao.getSourceByCategory(category, offset)

    fun getSourcesByName(name: String, offset: Int): List<Source> = newsDao.getSourceByName(name, offset)

}