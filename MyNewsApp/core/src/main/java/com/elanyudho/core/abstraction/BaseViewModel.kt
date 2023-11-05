package com.aarush.core.abstraction

import androidx.annotation.VisibleForTesting
import androidx.annotation.VisibleForTesting.PROTECTED
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

abstract class BaseViewModel<T> : ViewModel() {
    @VisibleForTesting(otherwise = PROTECTED)
    val _uiState: MutableLiveData<T> = MutableLiveData()

    val uiState: LiveData<T>
        get() = _uiState

}