package com.karuhun.feature.itemlist.ui

import com.karuhun.core.model.ContentItem

object ContentContract {
    data class UiState(
        val isLoading: Boolean? = false,
        val contents: List<ContentItem> = emptyList(),
        var contentId: Int? = null
    )

    sealed interface UiAction {
        data class LoadContents(val contentId: Int) : UiAction
    }
    sealed interface UiEffect {
        data class ShowError(val message: String)
    }
}