package com.aarush.newsapp.ui.search

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.aarush.core.abstraction.BaseViewModel
import com.aarush.core.dispatcher.DispatcherProvider
import com.aarush.core.model.model.Article
import com.aarush.core.model.model.Source
import com.aarush.core.model.usecase.GetArticlesByQueryUseCase
import com.aarush.core.model.usecase.GetSourceByNameUseCase
import com.aarush.core.util.exception.Failure
import com.aarush.core.util.extension.onError
import com.aarush.core.util.extension.onSuccess
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SearchViewModel @Inject constructor(
    private val dispatcherProvider: DispatcherProvider,
    private val getSourceByNameUseCase: GetSourceByNameUseCase,
    private val getArticlesByQueryUseCase: GetArticlesByQueryUseCase,
) : BaseViewModel<SearchViewModel.SearchUiState>() {

    sealed class SearchUiState {
        object InitialLoading : SearchUiState()
        object PagingLoading : SearchUiState()
        data class SourcesLoaded(val data: List<Source>) : SearchUiState()
        data class ArticleLoaded(val data: List<Article>) : SearchUiState()
        data class FailedLoaded(val failure: Failure) : SearchUiState()
    }

    fun getSourceByName(name: String, page: Long) {
        _uiState.value = if (page == 1L) {
            SearchUiState.InitialLoading
        } else {
            SearchUiState.PagingLoading
        }
        viewModelScope.launch(dispatcherProvider.io) {
            val data = getSourceByNameUseCase.getSourceByName(name, page.toInt())
            val dataString = data.joinToString(separator = ", ")
            Log.d("datalistv", dataString)
            withContext(dispatcherProvider.main) {
                _uiState.value = SearchUiState.SourcesLoaded(data)
            }
        }
    }

    fun getArticlesByQuery(source: String, page: Long) {
        _uiState.value = if (page == 1L) {
            SearchUiState.InitialLoading
        } else {
            SearchUiState.PagingLoading
        }
        viewModelScope.launch(dispatcherProvider.io) {
            getArticlesByQueryUseCase.run(GetArticlesByQueryUseCase.Params(source, page.toString()))
                .onSuccess {
                    withContext(dispatcherProvider.main) {
                        _uiState.value = SearchUiState.ArticleLoaded(it)
                    }
                }
                .onError {
                    withContext(dispatcherProvider.main) {
                        _uiState.value = SearchUiState.FailedLoaded(it)
                    }
                }
        }
    }

}