package com.karuhun.feature.content.data.repository

import com.karuhun.core.common.util.Synchronizer
import com.karuhun.core.common.util.changeListSync
import com.karuhun.core.database.dao.ContentDao
import com.karuhun.core.database.dao.ContentItemDao
import com.karuhun.core.database.model.toEntity
import com.karuhun.core.database.model.toModel
import com.karuhun.core.domain.repository.ContentRepository
import com.karuhun.core.model.ChangeListVersions
import com.karuhun.core.model.Content
import com.karuhun.feature.content.data.source.ContentApiService
import com.karuhun.feature.content.data.source.remote.ContentNetworkDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ContentRepositoryImpl @Inject constructor(
    private val apiService: ContentApiService,
    private val contentDao: ContentDao,
    private val contentItemDao: ContentItemDao,
    private val networkDataSource: ContentNetworkDataSource
) : ContentRepository {
    override suspend fun getContents(): Flow<List<Content>> {
        return contentDao.getAll().map { it.toModel() }
    }


    override suspend fun syncWith(synchronizer: Synchronizer): Boolean {
        return synchronizer.changeListSync(
            versionReader = ChangeListVersions::contentsVersion,
            changeListFetcher = { currentVersion ->
                networkDataSource.getContentChangeList(currentVersion)
            },
            versionUpdater = { latestVersion ->
                copy(
                    contentsVersion = latestVersion
                )
            },
            modelDeleter = contentDao::deleteContentByIds,
            modelUpdater = { changedIds ->
                val networkContents = networkDataSource.getContents(changedIds)
                contentDao.upsert(networkContents.toEntity())
            }
        )
    }
}