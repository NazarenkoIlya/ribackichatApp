package com.example.rybackiapp.domain.usecase


import com.example.rybackiapp.domain.model.UserProfileList
import com.example.rybackiapp.domain.repository.UsersProfileRepository
import javax.inject.Inject

class SearchWithTagUserProfileUseCase @Inject constructor(
    private val usersProfileRepository: UsersProfileRepository
) {
    suspend operator fun invoke(tagName: String): Result<UserProfileList> {
        return try {
            val users = usersProfileRepository.searchWithTagUsersProfileList(tagName)
            Result.success(users)
        } catch (e: Exception) {
            Result.failure(Exception(e.message))
        }
    }
}