package com.karuhun.core.common.util

import android.util.Log
import com.karuhun.core.model.ChangeListVersions
import com.karuhun.core.model.NetworkChangeList
import kotlin.coroutines.cancellation.CancellationException

interface Synchronizer {

    suspend fun Syncable.sync() = this@sync.syncWith(this@Synchronizer)
    suspend fun getChangeListVersions(): ChangeListVersions

    suspend fun updateChangeListVersions(update: ChangeListVersions.() -> ChangeListVersions)
}

interface Syncable {
    suspend fun syncWith(synchronizer: Synchronizer): Boolean
}

private suspend fun <T> suspendRunCatching(block: suspend () -> T): Result<T> = try {
    Result.success(block())
} catch (cancellationException: CancellationException) {
    throw cancellationException
} catch (exception: Exception) {
    Log.i(
        "suspendRunCatching",
        "Failed to evaluate a suspendRunCatchingBlock. Returning failure Result",
        exception,
    )
    Result.failure(exception)
}
suspend fun <T> Synchronizer.syncData(
    versionReader: suspend () -> Int,
    fetchData: suspend (Int) -> List<T>,
    saveData: suspend (List<T>) -> Unit,
    updateVersion: suspend (Int) -> Unit,
): Boolean = suspendRunCatching {
    val localVersion = versionReader()
    val data = fetchData(localVersion)

    if (data.isNotEmpty()) {
        saveData(data)
        // Increment version after successful sync
        updateVersion(localVersion + 1)
    }
}.isSuccess

suspend fun <T> Synchronizer.syncSimpleData(
    fetchData: suspend () -> T?,
    saveData: suspend (T) -> Unit,
): Boolean = suspendRunCatching {
    val data = fetchData()
    if (data != null) {
        saveData(data)
    }
}.isSuccess

suspend fun Synchronizer.changeListSync(
    versionReader: (ChangeListVersions) -> Int,
    changeListFetcher: suspend (Int) -> List<NetworkChangeList>,
    versionUpdater: ChangeListVersions.(Int) -> ChangeListVersions,
    modelDeleter: suspend (List<Int>) -> Unit,
    modelUpdater: suspend (List<Int>) -> Unit,
): Boolean = suspendRunCatching {
    val currentVersion = versionReader(getChangeListVersions())
    val changeList = changeListFetcher(currentVersion)
    if (changeList.isEmpty()) return@suspendRunCatching true
    val (deleted, updated) = changeList.partition{ it.deletedAt != null }

    modelDeleter(deleted.map { it.id })
    modelUpdater(updated.map { it.id })

    val latestVersion = changeList.last().version

    updateChangeListVersions {
        versionUpdater(latestVersion)
    }
}.isSuccess
