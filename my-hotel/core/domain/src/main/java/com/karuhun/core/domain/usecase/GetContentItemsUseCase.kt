package com.karuhun.core.domain.usecase

import com.karuhun.core.domain.repository.ContentItemsRepository
import com.karuhun.core.domain.repository.ContentRepository
import com.karuhun.core.model.ContentItem
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetContentItemsUseCase @Inject constructor(
    private val repository: ContentItemsRepository,
) {
    suspend operator fun invoke(contentId: Int): Flow<List<ContentItem>> =
        repository.getContentItems(id = contentId)
}