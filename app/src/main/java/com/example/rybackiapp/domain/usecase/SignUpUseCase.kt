package com.example.rybackiapp.domain.usecase

import com.example.rybackiapp.domain.model.AuthResult
import com.example.rybackiapp.domain.repository.AuthRepository
import javax.inject.Inject

class SignUpUseCase @Inject constructor(private val authRepository: AuthRepository) {
    suspend operator fun invoke(email: String, password: String): Result<AuthResult> {
        return authRepository.signUpEmail(email, password)
    }
}