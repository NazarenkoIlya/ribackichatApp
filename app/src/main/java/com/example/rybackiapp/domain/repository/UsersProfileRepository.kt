package com.example.rybackiapp.domain.repository

import com.example.rybackiapp.domain.model.Filter
import com.example.rybackiapp.domain.model.PrivateUserChatPreview
import com.example.rybackiapp.domain.model.UserPreview
import com.example.rybackiapp.domain.model.UserProfile
import com.example.rybackiapp.domain.model.UserProfileList
import kotlinx.coroutines.flow.Flow

interface UsersProfileRepository {
    suspend fun getUsersProfileList(): Result<UserProfileList>
    suspend fun getUsersProfile(id: String): Result<UserProfile>
    suspend fun searchUsersProfileList(search: String, filter: Filter): UserProfileList
    suspend fun searchWithTagUsersProfileList(tag: String): UserProfileList
    fun observeUserProfilePreview(id: String): Flow<UserPreview>
    fun observeUserChatProfilePreview(id: String): Flow<PrivateUserChatPreview>
}