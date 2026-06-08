package com.example.rybackiapp.data.storage

import kotlinx.coroutines.flow.Flow

interface SettingStorage {
    suspend fun setNotificationEnable(enabled: Boolean)
    suspend fun setTheme(theme: String)
    suspend fun setFontSizeMessage(size: Int)

    fun observeNotificationEnable(): Flow<Boolean>
    fun observeTheme(): Flow<String>
    fun observeFontSizeMessage(): Flow<Int>

    fun observeMutedChat(): Flow<Set<String>>
    fun isChatMuted(chatId: String): Flow<Boolean>

    suspend fun muteChat(chatId: String)
    suspend fun unmuteChat(chatId: String)
}