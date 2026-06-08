package com.example.rybackiapp.data.repository


import com.example.rybackiapp.data.storage.SettingStorage
import com.example.rybackiapp.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SettingsRepositoryImpl @Inject constructor(
    private val settingStorage: SettingStorage
) : SettingsRepository {
    override suspend fun setNotificationEnable(enabled: Boolean) {
        settingStorage.setNotificationEnable(enabled)
    }

    override suspend fun setTheme(theme: String) {
        settingStorage.setTheme(theme)
    }

    override suspend fun setFontSizeMessage(size: Int) {
        settingStorage.setFontSizeMessage(size)
    }

    override fun observeNotificationEnable(): Flow<Boolean> {
        return  settingStorage.observeNotificationEnable()
    }

    override fun observeTheme(): Flow<String> {
        return settingStorage.observeTheme()
    }

    override fun observeFontSizeMessage(): Flow<Int> {
        return settingStorage.observeFontSizeMessage()
    }
}