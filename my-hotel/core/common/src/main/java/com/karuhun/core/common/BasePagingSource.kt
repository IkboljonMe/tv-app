package com.karuhun.core.common

import androidx.paging.PagingSource
import androidx.paging.PagingState
import retrofit2.HttpException
import java.io.IOException

abstract class BasePagingSource<LocalData : Any, RemoteData : Any> :
    PagingSource<Int, LocalData>() {

    companion object {
        const val DEFAULT_PAGE_SIZE = 10
        const val DEFAULT_INITIAL_PAGE_NUMBER = 1
    }

    abstract suspend fun fetchData(page: Int, pageSize: Int): List<RemoteData>

    abstract fun mapToLocalData(remoteData: List<RemoteData>): List<LocalData>

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LocalData> {
        val pageNumber = params.key ?: DEFAULT_INITIAL_PAGE_NUMBER
        val pageSize = params.loadSize.coerceAtMost(DEFAULT_PAGE_SIZE)

        return try {
            val remoteDataList = fetchData(page = pageNumber, pageSize = pageSize)
            val localDataList = mapToLocalData(remoteDataList)

            val prevKey = if (pageNumber == DEFAULT_INITIAL_PAGE_NUMBER) null else pageNumber - 1
            val nextKey =
                if (remoteDataList.isEmpty() || remoteDataList.size < pageSize) null else pageNumber + 1

            LoadResult.Page(
                data = localDataList,
                prevKey = prevKey,
                nextKey = nextKey,
            )
        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: HttpException) {
            LoadResult.Error(e)
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, LocalData>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}
