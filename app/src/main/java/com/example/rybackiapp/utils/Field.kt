package com.example.rybackiapp.utils


data class Field(
    val text: String = "",
    val isError: Boolean = false,
    val isCorrect: Boolean =  false,
    val error: String? = null
)