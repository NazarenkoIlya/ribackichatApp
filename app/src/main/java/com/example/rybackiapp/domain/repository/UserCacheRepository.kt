package com.example.rybackiapp.domain.repository

import kotlinx.coroutines.flow.Flow


interface UserIdCacheRepository {
    fun observeUserId(): Flow<String>
    fun onUserLogout()
}