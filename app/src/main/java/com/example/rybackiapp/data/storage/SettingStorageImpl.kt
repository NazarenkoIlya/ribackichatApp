package com.example.rybackiapp.data.storage

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import com.example.rybackiapp.di.SettingsDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
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

    override fun observeMutedChat(): Flow<Set<String>> {
        return dataStore.data.map { it[MUTED_CHATS_KEY] ?: emptySet() }
    }

    override fun isChatMuted(chatId: String): Flow<Boolean> {
        return observeMutedChat()
            .map { mutedSet ->

                Log.d("MUTE", "isChatMuted:${mutedSet}")
                Log.d("MUTE", "Raw muted set from DataStore: $mutedSet")
                Log.d("MUTE", "Set contains empty string? ${mutedSet.contains("")}")
                mutedSet.contains(chatId)
            }
            .distinctUntilChanged()
    }

    override suspend fun muteChat(chatId: String) {
        dataStore.edit {
            val currentSet = it[MUTED_CHATS_KEY]?.toMutableSet() ?: mutableSetOf()
            currentSet.add(chatId)
            it[MUTED_CHATS_KEY] = currentSet
        }
    }

    override suspend fun unmuteChat(chatId: String) {
        dataStore.edit {
            val currentSet = it[MUTED_CHATS_KEY]?.toMutableSet() ?: mutableSetOf()
            currentSet.remove(chatId)
            it[MUTED_CHATS_KEY] = currentSet
        }
    }

    companion object Option {
        private val NOTIFICATION_KEY = booleanPreferencesKey("notification_enable")
        private val THEME_KEY = stringPreferencesKey("theme")
        private val FONT_SIZE_KEY = intPreferencesKey("font_size")
        private val MUTED_CHATS_KEY = stringSetPreferencesKey("muted_chats")
    }
}