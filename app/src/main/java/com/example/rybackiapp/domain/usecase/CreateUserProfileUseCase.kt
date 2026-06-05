package com.example.rybackiapp.domain.usecase

import com.example.rybackiapp.domain.repository.ProfileRepository
import java.time.Year
import javax.inject.Inject

class CreateUserProfileUseCase @Inject constructor(
    private val profileRepository: ProfileRepository
) {
    suspend operator fun invoke(
        id: String,
        name: String,
        year: String,
        tagName: String
    ): Result<Unit> {
        return profileRepository.createUserProfile(id, name, year, tagName)
    }
}