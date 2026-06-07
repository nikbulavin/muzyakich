package ru.resodostudio.muzyakich.core.data.repository.util

import android.content.Context
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.ktx.AppUpdateResult
import com.google.android.play.core.ktx.requestUpdateFlow
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class InAppUpdateManagerImpl @Inject constructor(
    @ApplicationContext private val context: Context,
) : InAppUpdateManager {

    private val appUpdateManager: AppUpdateManager = AppUpdateManagerFactory.create(context)

    override val inAppUpdateResult: Flow<AppUpdateResult> = appUpdateManager.requestUpdateFlow()
        .catch { emit(AppUpdateResult.NotAvailable) }
}