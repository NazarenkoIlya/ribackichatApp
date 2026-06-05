package com.example.rybackiapp.domain.usecase

import com.example.rybackiapp.domain.model.UserProfile
import com.example.rybackiapp.domain.repository.UsersProfileRepository
import javax.inject.Inject

class GetUserProfilesUseCase @Inject constructor(
    private val usersProfileRepository: UsersProfileRepository
) {
    suspend operator fun invoke(id: String): Result<UserProfile> {
        return usersProfileRepository.getUsersProfile(id)
    }
}