package com.example.rybackiapp.domain.usecase

import com.example.rybackiapp.domain.model.InterestGroup
import com.example.rybackiapp.domain.repository.InterestRepository
import javax.inject.Inject

class LoadInterestsUseCase @Inject constructor(
    private val interestRepository: InterestRepository
) {
    suspend operator fun invoke(): List<InterestGroup?> {
        return interestRepository.loadInterests()
    }
}