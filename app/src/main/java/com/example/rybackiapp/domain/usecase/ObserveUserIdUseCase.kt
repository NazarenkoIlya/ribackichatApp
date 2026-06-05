package com.example.rybackiapp.domain.usecase

import com.example.rybackiapp.domain.model.UserPreview
import com.example.rybackiapp.domain.repository.UserIdCacheRepository
import com.example.rybackiapp.domain.repository.UsersProfileRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveUserIdUseCase @Inject constructor(
    private val userIdCacheRepository: UserIdCacheRepository,
) {

    operator fun invoke(): Flow<String> =
        userIdCacheRepository.observeUserId()
}