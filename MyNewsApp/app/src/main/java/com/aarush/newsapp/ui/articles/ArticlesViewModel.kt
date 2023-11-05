package com.aarush.newsapp.ui.articles

import androidx.lifecycle.viewModelScope
import com.aarush.core.abstraction.BaseViewModel
import com.aarush.core.dispatcher.DispatcherProvider
import com.aarush.core.model.model.Article
import com.aarush.core.model.usecase.GetArticlesUseCase
import com.aarush.core.util.exception.Failure
import com.aarush.core.util.extension.onError
import com.aarush.core.util.extension.onSuccess
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ArticlesViewModel @Inject constructor(
    private val dispatcherProvider: DispatcherProvider,
    private val getArticlesUseCase: GetArticlesUseCase,
) : BaseViewModel<ArticlesViewModel.ArticlesUiState>() {

    sealed class ArticlesUiState {
        object InitialLoading: ArticlesUiState()
        object PagingLoading: ArticlesUiState()
        data class ArticlesLoaded(val data: List<Article>) : ArticlesUiState()
        data class FailedLoaded(val failure: Failure) : ArticlesUiState()
    }

    fun getArticles(source: String, page: Long){
        _uiState.value = if (page == 1L) {
            ArticlesUiState.InitialLoading
        }else{
            ArticlesUiState.PagingLoading
        }
        viewModelScope.launch(dispatcherProvider.io) {
            getArticlesUseCase.run(GetArticlesUseCase.Params(source, page.toString()))
                .onSuccess {
                    withContext(dispatcherProvider.main) {
                        _uiState.value = ArticlesUiState.ArticlesLoaded(it)
                    }
                }
                .onError {
                    withContext(dispatcherProvider.main) {
                        _uiState.value = ArticlesUiState.FailedLoaded(it)
                    }
                }
        }
    }

}