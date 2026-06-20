package com.karuhun.feature.content.data.source.remote

import com.karuhun.core.model.Application
import com.karuhun.core.model.NetworkChangeList
import com.karuhun.feature.content.data.source.ApplicationApiService
import com.karuhun.feature.content.data.source.remote.response.toDomainList
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ApplicationNetworkDataSource @Inject constructor(
    private val apiService: ApplicationApiService,
    private val ioDispatcher: CoroutineDispatcher
) {
    suspend fun getApplicationChangeList(after: Int) : List<NetworkChangeList> {
        return withContext(ioDispatcher) {
            val allApplications = mutableListOf<NetworkChangeList>()
            var currentPage = 1
            do {
                val params = mapOf(
                    "order" to "asc",
                    "orderBy" to "applications.version",
                    "paginate" to "10",
                    "page" to "$currentPage",
                    "after" to after.toString()
                )
                val response = apiService.getApplicationChangelist(params)
                allApplications.addAll(response.data?.data ?: emptyList())
                currentPage++
            } while (response.data?.nextPageUrl != null)
            allApplications
        }
    }

    suspend fun getApplications(
        ids: List<Int>
    ): List<Application> {
        return  withContext(ioDispatcher) {
            val allApplication = mutableListOf<Application>()
            var currentPage = 1

            do {
                val params = mapOf(
                    "order" to "asc",
                    "paginate" to "10",
                    "page" to "$currentPage",
                    "ids" to ids.joinToString(",")
                )
                val response = apiService.getApplications(params)
                allApplication.addAll(response.data?.data?.toDomainList() ?: emptyList())
                currentPage++
            } while (response.data?.nextPageUrl != null)
            allApplication
        }
    }
}