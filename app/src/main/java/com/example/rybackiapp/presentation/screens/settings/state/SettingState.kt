package com.example.rybackiapp.presentation.screens.settings.state

import android.app.Notification
import com.example.rybackiapp.presentation.screens.editprofile.state.EditProfileEvent


sealed class SettingState {

    object Loading : SettingState()
    object Success : SettingState()
    data class Error(val message: String) : SettingState()
}

data class SettingUIState(
    val notificationEnable: Boolean = true,
    val theme: Theme = Theme.LIGHT,
    val fontSize: Int = 16,
    val state: SettingState = SettingState.Loading
)

enum class Theme(val theme: String) {
    LIGHT("light"),
    DARK("dark"),
    SYSTEM("system");

    companion object {
        fun fromValue(value: String?): Theme {
            return when (value?.lowercase()) {
                "light" -> LIGHT
                "dark" -> DARK
                "system", null -> SYSTEM
                else -> {
                    SYSTEM
                }
            }
        }

        fun toValue(theme: Theme): String = theme.theme
    }
}

sealed class SettingEvent {
    data class NotificationEnableChanged(val enable: Boolean) : SettingEvent()
    data class ThemeChanged(val theme: String) : SettingEvent()
    data class FontSizeMessageChanged(val size: Int) : SettingEvent()
}