package com.karuhun.core.domain.repository

import com.karuhun.core.common.util.Syncable
import com.karuhun.core.model.Application
import kotlinx.coroutines.flow.Flow

interface ApplicationRepository : Syncable {
    fun getAllApplications(): Flow<List<Application>>
}