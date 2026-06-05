package com.example.rybackiapp.domain.usecase

import com.example.rybackiapp.domain.repository.UsersProfileRepository
import javax.inject.Inject

class ObserveUserChatPreview @Inject constructor(
    private val usersProfileRepository: UsersProfileRepository
) {
    suspend operator fun invoke(uid: String) =
        usersProfileRepository.observeUserChatProfilePreview(uid)
}