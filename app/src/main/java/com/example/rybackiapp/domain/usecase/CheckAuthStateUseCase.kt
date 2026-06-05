package com.example.rybackiapp.domain.usecase

import com.example.rybackiapp.domain.repository.AuthRepository
import javax.inject.Inject

class CheckAuthStateUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    operator fun invoke(): Boolean {
        return repository.isUserAuthorized()
    }
}