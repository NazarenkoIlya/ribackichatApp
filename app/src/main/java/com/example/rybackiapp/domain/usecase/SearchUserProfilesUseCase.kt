package com.example.rybackiapp.domain.usecase

import com.example.rybackiapp.domain.model.Filter
import com.example.rybackiapp.domain.model.UserProfileList
import com.example.rybackiapp.domain.repository.UsersProfileRepository
import javax.inject.Inject

class SearchUserProfilesUseCase @Inject constructor(
    private val usersProfileRepository: UsersProfileRepository
) {
    suspend operator fun invoke(searchName: String, filter: Filter): Result<UserProfileList> {
        return try {
            val users = usersProfileRepository.searchUsersProfileList(searchName, filter)
            Result.success(users)
        } catch (e: Exception) {
            Result.failure(Exception(e.message))
        }
    }
}