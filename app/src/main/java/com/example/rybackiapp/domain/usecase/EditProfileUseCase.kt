package com.example.rybackiapp.domain.usecase


import com.example.rybackiapp.domain.model.InterestItem
import com.example.rybackiapp.domain.repository.ProfileRepository
import javax.inject.Inject

class EditProfileUseCase @Inject constructor(
    private val profileRepository: ProfileRepository
) {
    suspend operator fun invoke(
        id: String,
        name: String,
        year: String,
        tagName: String,
        interests: Set<InterestItem>
    ): Result<Unit> {
        return profileRepository.editUserProfile(
            id = id,
            name = name,
            year = year,
            tagName = tagName,
            interests = interests,
        )
    }
}