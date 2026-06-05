package com.example.rybackiapp.domain.repository

import com.example.rybackiapp.domain.model.InterestItem
import com.example.rybackiapp.domain.model.MainProfile
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {
    suspend fun getUserProfile(): Result<MainProfile>
    suspend fun createUserProfile(id: String, name: String, year: String, tagName: String): Result<Unit>
    suspend fun editUserProfile(
        id: String,
        name: String,
        year: String,
        tagName: String,
        interests: Set<InterestItem>?
    ): Result<Unit>

    fun observeUserUserProfile(): Flow<MainProfile>
    suspend fun uploadMainImage(id: String, url: String)

    suspend fun observeUserId(): Flow<String>
}