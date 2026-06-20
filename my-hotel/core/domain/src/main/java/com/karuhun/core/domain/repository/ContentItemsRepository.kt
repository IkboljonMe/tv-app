package com.karuhun.core.domain.repository

import com.karuhun.core.common.util.Syncable
import com.karuhun.core.model.ContentItem
import kotlinx.coroutines.flow.Flow

interface ContentItemsRepository : Syncable {
    suspend fun getContentItems(id: Int): Flow<List<ContentItem>>
}