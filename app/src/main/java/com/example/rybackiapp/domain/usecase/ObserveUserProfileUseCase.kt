package com.example.rybackiapp.domain.usecase

import com.example.rybackiapp.domain.model.MainProfile
import com.example.rybackiapp.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveUserProfileUseCase @Inject constructor(
    private val profileRepository: ProfileRepository
) {
    suspend operator fun invoke(): Flow<MainProfile> {
        return profileRepository.observeUserUserProfile()
    }
}