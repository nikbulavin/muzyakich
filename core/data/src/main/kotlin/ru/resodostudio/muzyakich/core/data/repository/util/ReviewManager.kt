package ru.resodostudio.muzyakich.core.data.repository.util

import android.app.Activity

interface ReviewManager {

    suspend fun requestReview(activity: Activity)
}
