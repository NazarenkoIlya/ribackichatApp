package com.example.rybackiapp.domain.model

import io.ktor.http.CacheControl

data class Filter(
    val minAge: Int,
    val maxAge: Int,
    val desirableInterests: Set<String>,
    val unwantedInterests: Set<String>
)
