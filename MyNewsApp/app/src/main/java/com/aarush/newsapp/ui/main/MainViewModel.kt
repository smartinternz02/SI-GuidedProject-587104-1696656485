package com.aarush.newsapp.ui.main

import androidx.lifecycle.viewModelScope
import com.aarush.core.abstraction.BaseViewModel
import com.aarush.core.dispatcher.DispatcherProvider
import com.aarush.core.model.model.Source
import com.aarush.core.model.usecase.GetSourcesByCategoryUseCase
import com.aarush.core.util.exception.Failure
import com.aarush.core.util.extension.onError
import com.aarush.core.util.extension.onSuccess
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val dispatcherProvider: DispatcherProvider,
    private val getSourcesByCategory: GetSourcesByCategoryUseCase,
) : BaseViewModel<MainViewModel.MainUiState>() {

    sealed class MainUiState {
        object InitialLoading: MainUiState()
        object PagingLoading: MainUiState()
        data class SourcesLoaded(val data: List<Source>) : MainUiState()
        data class FailedLoaded(val failure: Failure) : MainUiState()
    }

    fun getSourceByCategory(category: String, page: Long){
        _uiState.value = if (page == 1L) {
            MainUiState.InitialLoading
        }else{
            MainUiState.PagingLoading
        }
        viewModelScope.launch(dispatcherProvider.io) {
            getSourcesByCategory.run(GetSourcesByCategoryUseCase.Params(category, page.toInt()))
                .onSuccess {
                    withContext(dispatcherProvider.main) {
                        _uiState.value = MainUiState.SourcesLoaded(it)
                    }
                }
                .onError {
                    withContext(dispatcherProvider.main) {
                        _uiState.value = MainUiState.FailedLoaded(it)
                    }
                }
        }
    }

}