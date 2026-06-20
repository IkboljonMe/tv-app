package com.karuhun.core.domain.usecase

import com.karuhun.core.domain.repository.ContentRepository
import com.karuhun.core.model.Content
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetContentsUseCase @Inject constructor(
    private val repository: ContentRepository
) {
    suspend operator fun invoke(): Flow<List<Content>> = repository.getContents()
}