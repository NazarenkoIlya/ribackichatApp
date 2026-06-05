package com.example.rybackiapp.domain.repository

import com.example.rybackiapp.domain.model.InterestGroup

interface InterestRepository {
    suspend fun loadInterests(): List<InterestGroup>
}