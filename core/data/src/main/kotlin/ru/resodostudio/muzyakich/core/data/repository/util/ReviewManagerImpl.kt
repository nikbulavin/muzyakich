package ru.resodostudio.muzyakich.core.data.repository.util

import android.app.Activity
import android.content.Context
import com.google.android.play.core.review.ReviewManagerFactory
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

internal class ReviewManagerImpl @Inject constructor(
    @ApplicationContext private val context: Context,
) : ReviewManager {

    override suspend fun requestReview(activity: Activity) {
        val manager = ReviewManagerFactory.create(context)
        runCatching {
            val reviewInfo = manager.requestReviewFlow().await()
            manager.launchReviewFlow(activity, reviewInfo).await()
        }
    }
}
