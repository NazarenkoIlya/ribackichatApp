package com.example.rybackiapp.domain.usecase

import com.example.rybackiapp.domain.model.UserPreview
import com.example.rybackiapp.domain.repository.UsersProfileRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveUserProfilePreview @Inject constructor(
    private val usersProfileRepository: UsersProfileRepository
) {

    operator fun invoke(id: String): Flow<UserPreview> =
        usersProfileRepository.observeUserProfilePreview(id)
}