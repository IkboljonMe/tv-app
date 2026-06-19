package com.karuhun.feature.itemlist.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.karuhun.core.common.orZero
import com.karuhun.core.domain.usecase.GetContentItemsUseCase
import com.karuhun.core.domain.usecase.GetContentsUseCase
import com.karuhun.core.ui.navigation.delegate.mvi.MVI
import com.karuhun.core.ui.navigation.delegate.mvi.mvi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class ContentViewModel @Inject constructor(
    private val getContentItemsUseCase: GetContentItemsUseCase,
) : ViewModel(),
    MVI<ContentContract.UiState, ContentContract.UiAction, ContentContract.UiEffect> by mvi(
        initialState = ContentContract.UiState(),
    ) {

    override fun onAction(action: ContentContract.UiAction) {
        when (action) {
            is ContentContract.UiAction.LoadContents -> {
                loadContents(action.contentId)
            }
        }
    }

    private fun loadContents(contentId: Int) = viewModelScope.launch {
        updateUiState { copy(isLoading = true) }
        getContentItemsUseCase(contentId).collect {
            updateUiState {
                copy(
                    isLoading = false,
                    contents = it,
                )
            }
        }
    }
}