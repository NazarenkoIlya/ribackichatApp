package com.example.rybackiapp.domain.usecase

import com.example.rybackiapp.domain.model.Filter
import com.example.rybackiapp.domain.repository.FilterRepository
import javax.inject.Inject

class SaveFilterUseCase @Inject constructor(
    private val filterRepository: FilterRepository
) {
    suspend operator fun invoke(filter: Filter) {
        filterRepository.setFilter(filter)
    }
}