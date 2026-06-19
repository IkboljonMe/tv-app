package com.karuhun.core.domain.repository

import com.karuhun.core.common.util.Syncable
import com.karuhun.core.model.Content
import kotlinx.coroutines.flow.Flow

interface ContentRepository : Syncable {
    suspend fun getContents(): Flow<List<Content>>
}