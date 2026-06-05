package com.example.rybackiapp.domain.repository

import com.example.rybackiapp.domain.model.AuthResult


interface AuthRepository {
    suspend fun signInEmail(email: String, password: String): Result<Unit>
    suspend fun signUpEmail(email: String, password: String): Result<AuthResult>
    suspend fun signOut(): Result<Unit>

    fun isUserAuthorized(): Boolean
}