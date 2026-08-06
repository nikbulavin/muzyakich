package ru.resodostudio.muzyakich.core.domain

import kotlinx.coroutines.flow.first
import ru.resodostudio.muzyakich.core.data.repository.SongsRepository
import javax.inject.Inject

class CheckIfReviewNeededUseCase @Inject constructor(
    private val songsRepository: SongsRepository,
) {
    suspend operator fun invoke(): Boolean = songsRepository.getTotalPlayCount().first() >= 10
}
