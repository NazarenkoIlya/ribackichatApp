package com.example.rybackiapp.data.storage

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.rybackiapp.di.SettingsDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SettingStorageImpl(
    @SettingsDataStore private val dataStore: DataStore<Preferences>
) : SettingStorage {
    override suspend fun setNotificationEnable(enabled: Boolean) {
        dataStore.edit {
            it[NOTIFICATION_KEY] = enabled
        }
    }

    override suspend fun setTheme(theme: String) {
        dataStore.edit {
            it[THEME_KEY] = theme
        }
    }

    override suspend fun setFontSizeMessage(size: Int) {
        dataStore.edit {
            it[FONT_SIZE_KEY] = size
        }
    }

    override fun observeNotificationEnable(): Flow<Boolean> {
        return dataStore.data.map { it[NOTIFICATION_KEY] ?: true }
    }

    override fun observeTheme(): Flow<String> {
        return dataStore.data.map { it[THEME_KEY] ?: "light" }
    }

    override fun observeFontSizeMessage(): Flow<Int> {
        return dataStore.data.map { it[FONT_SIZE_KEY] ?: 12 }
    }

    companion object Option {
        private val NOTIFICATION_KEY = booleanPreferencesKey("notification_enable")
        private val THEME_KEY = stringPreferencesKey("theme")
        private val FONT_SIZE_KEY = intPreferencesKey("font_size")
    }
}