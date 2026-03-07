package ru.resodostudio.muzyakich.core.data.repository.util

import com.google.android.play.core.ktx.AppUpdateResult
import kotlinx.coroutines.flow.Flow

interface InAppUpdateManager {

    val inAppUpdateResult: Flow<AppUpdateResult>
}