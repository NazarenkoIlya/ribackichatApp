package com.example.rybackiapp.data.repository

import android.content.res.AssetManager
import com.example.rybackiapp.data.mappers.toMap
import com.example.rybackiapp.data.model.InterestsJson
import com.example.rybackiapp.domain.model.InterestGroup
import com.example.rybackiapp.domain.repository.InterestRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import javax.inject.Inject

class InterestRepositoryImpl @Inject constructor(
    private val assetManager: AssetManager
) : InterestRepository {

    override suspend fun loadInterests(): List<InterestGroup> {
        return withContext(Dispatchers.IO) {
            val jsonString = assetManager.open("interests.json")
                .bufferedReader()
                .use { it.readText() }

            val json = Json { ignoreUnknownKeys = true }
            json.decodeFromString<InterestsJson>(jsonString).groups.map { it.toMap() }
        }
    }
}