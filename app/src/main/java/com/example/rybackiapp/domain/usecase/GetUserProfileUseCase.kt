package com.example.rybackiapp.domain.usecase

import com.example.rybackiapp.domain.model.MainProfile
import com.example.rybackiapp.domain.repository.ProfileRepository
import javax.inject.Inject

class GetUserProfileUseCase @Inject constructor(
    private val profileRepository: ProfileRepository
) {
    suspend operator fun invoke(): Result<MainProfile?> {
        return profileRepository.getUserProfile()
    }
}