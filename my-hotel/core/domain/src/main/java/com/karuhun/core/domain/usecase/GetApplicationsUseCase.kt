package com.karuhun.core.domain.usecase

import com.karuhun.core.domain.repository.ApplicationRepository
import com.karuhun.core.model.Application
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetApplicationsUseCase @Inject constructor(
    private val repository: ApplicationRepository
) {
    operator fun invoke(): Flow<List<Application>> = repository.getAllApplications()
}