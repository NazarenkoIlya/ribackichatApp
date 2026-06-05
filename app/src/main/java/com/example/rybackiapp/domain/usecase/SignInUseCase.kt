package com.example.rybackiapp.domain.usecase

import com.example.rybackiapp.domain.repository.AuthRepository
import javax.inject.Inject

class SignInUseCase @Inject constructor(private val authRepository: AuthRepository) {
    suspend operator fun invoke(email: String, password: String): Result<Unit> {
        return authRepository.signInEmail(email, password)
    }
}