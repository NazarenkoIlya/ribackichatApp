package com.example.rybackiapp.domain.usecase

import com.example.rybackiapp.domain.model.UserProfileList
import com.example.rybackiapp.domain.repository.UsersProfileRepository
import javax.inject.Inject

class GetUserProfilesListUseCase @Inject constructor(
    private val usersProfileRepository: UsersProfileRepository
) {
    suspend operator fun invoke(): Result<UserProfileList> {
        return usersProfileRepository.getUsersProfileList()
    }
}