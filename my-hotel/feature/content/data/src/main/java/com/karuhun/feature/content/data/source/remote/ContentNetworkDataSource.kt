package com.karuhun.feature.content.data.source.remote

import com.karuhun.core.model.Content
import com.karuhun.core.model.ContentItem
import com.karuhun.core.model.NetworkChangeList
import com.karuhun.feature.content.data.source.ContentApiService
import com.karuhun.feature.content.data.source.remote.response.toDomainList
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ContentNetworkDataSource @Inject constructor(
    private val apiService: ContentApiService,
    private val ioDispatcher: CoroutineDispatcher,
) {
    suspend fun getContents(ids: List<Int>): List<Content> {
        return withContext(ioDispatcher) {
            val allFoods = mutableListOf<Content>()
            var currentPage = 1

            do {
                val params = mapOf(
                    "order" to "asc",
                    "paginate" to "10",
                    "page" to "$currentPage",
                    "id" to ids.joinToString(",")
                )
                val response = apiService.getContents(params)
                allFoods.addAll(response.data?.data?.toDomainList() ?: emptyList())
                currentPage++
            } while (response.data?.nextPageUrl != null)
            allFoods
        }
    }

    suspend fun getContentChangeList(after: Int) : List<NetworkChangeList> {
        return withContext(ioDispatcher) {
            val allChangeLists = mutableListOf<NetworkChangeList>()
            var currentPage = 1
            do {
                val params = mapOf(
                    "order" to "asc",
                    "orderBy" to "contents.version",
                    "paginate" to "10",
                    "page" to "$currentPage",
                    "after" to after.toString()
                )
                val response = apiService.getContentChangeList(params)
                allChangeLists.addAll(response.data?.data ?: emptyList())
                currentPage++
            } while (response.data?.nextPageUrl != null)
            allChangeLists
        }
    }

    suspend fun getContentItems(
        ids: List<Int>
    ): List<ContentItem> {
        return  withContext(ioDispatcher) {
            val allFoods = mutableListOf<ContentItem>()
            var currentPage = 1

            do {
                val params = mapOf(
                    "order" to "asc",
                    "paginate" to "10",
                    "page" to "$currentPage",
                    "id" to ids.joinToString(",")
                )
                val response = apiService.getContentItems(params)
                allFoods.addAll(response.data?.data?.toDomainList() ?: emptyList())
                currentPage++
            } while (response.data?.nextPageUrl != null)
            allFoods
        }
    }

    suspend fun getContentItemChangelist(after: Int) : List<NetworkChangeList> {
        return withContext(ioDispatcher) {
            val allChangeLists = mutableListOf<NetworkChangeList>()
            var currentPage = 1
            do {
                val params = mapOf(
                    "order" to "asc",
                    "orderBy" to "content_items.version",
                    "paginate" to "10",
                    "page" to "$currentPage",
                    "after" to after.toString()
                )
                val response = apiService.getContentItemChangeList(params)
                allChangeLists.addAll(response.data?.data ?: emptyList())
                currentPage++
            } while (response.data?.nextPageUrl != null)
            allChangeLists
        }
    }
}
