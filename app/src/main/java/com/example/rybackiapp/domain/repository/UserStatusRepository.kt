package com.example.rybackiapp.domain.repository

import kotlinx.coroutines.flow.Flow

interface UserStatusRepository {
    fun observeConnectionStatus(): Flow<Boolean>
    suspend fun setUserOnline(userId: String)
    fun observeUserOnline(userId: String): Flow<Boolean>
}