package ru.resodostudio.muzyakich.core.domain

import android.app.Activity
import kotlinx.coroutines.flow.first
import ru.resodostudio.muzyakich.core.data.repository.SongsRepository
import ru.resodostudio.muzyakich.core.data.repository.util.ReviewManager
import javax.inject.Inject

class CheckAndShowReviewUseCase @Inject constructor(
    private val songsRepository: SongsRepository,
    private val reviewManager: ReviewManager,
) {
    suspend operator fun invoke(activity: Activity) {
        if (songsRepository.getTotalPlayCount().first() >= 10) {
            reviewManager.requestReview(activity)
        }
    }
}
