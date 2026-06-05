package com.example.rybackiapp.domain.repository

import com.example.rybackiapp.domain.model.Filter
import kotlinx.coroutines.flow.Flow

interface FilterRepository {
    fun getFilter(): Flow<Filter>
    suspend fun setFilter(filer: Filter)
}