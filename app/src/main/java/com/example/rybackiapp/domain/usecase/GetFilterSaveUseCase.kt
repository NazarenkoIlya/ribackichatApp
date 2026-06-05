package com.example.rybackiapp.domain.usecase

import com.example.rybackiapp.domain.model.Filter
import com.example.rybackiapp.domain.repository.FilterRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFilterSaveUseCase @Inject constructor(
    private val filterRepository: FilterRepository
) {
    operator fun invoke(): Flow<Filter> = filterRepository.getFilter()
}