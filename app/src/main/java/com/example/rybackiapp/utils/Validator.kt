package com.example.rybackiapp.utils

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Calendar

object Validator {

    fun validateEmail(email: String): String? {
        val regex = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
        return when {
            email.isBlank() -> "Email cannot be empty"
            !regex.matches(email) -> "Invalid email format"
            else -> null
        }
    }

    fun validatePassword(password: String): String? {
        return when {
            password.isBlank() -> "The password cannot be empty"
            password.length < 8 -> "The password must be at least 8 characters long"
            else -> null

        }
    }

    fun validateName(name: String): String? {
        return when {
            name.isBlank() -> "The name cannot be empty"
            !name.matches(Regex("^[a-zA-Z0-9]+$")) -> "The name can only contain letters and numbers"
            else -> null
        }
    }

    fun validateTagName(tagName: String): String? {

        return when {
            tagName.isBlank() -> "Tag cannot be empty"
            !tagName.startsWith("@") -> "Tag must start with @"
            tagName.length < 3 -> "Tag must have at least two character after @"
            !tagName.drop(1)
                .matches(Regex("^[a-zA-Z0-9]+$")) -> "Tag can only contain letters and numbers after @"

            else -> null
        }
    }

    fun validateYear(year: String): String? {

        val currentTime = System.currentTimeMillis()
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = currentTime
        val lastYear = calendar.get(Calendar.YEAR) - 14
        val yearInt = year.toIntOrNull()
        return when {
            year.isBlank() -> "The year cannot be empty"
            yearInt == null -> "Please enter a valid year"
            yearInt < 1888 -> "The year cannot be less 1888"
            yearInt > lastYear -> "The year cannot be more ${lastYear}"
            else -> null
        }
    }
}