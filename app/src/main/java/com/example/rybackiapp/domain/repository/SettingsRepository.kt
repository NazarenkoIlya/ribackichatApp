package com.example.rybackiapp.domain.repository

import kotlinx.coroutines.flow.Flow

interface SettingsRepository {

    suspend fun setNotificationEnable(enabled: Boolean)
    suspend fun setTheme(theme: String)
    suspend fun setFontSizeMessage(size: Int)

    fun observeNotificationEnable(): Flow<Boolean>
    fun observeTheme(): Flow<String>
    fun observeFontSizeMessage(): Flow<Int>
}