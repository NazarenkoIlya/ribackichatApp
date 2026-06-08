package com.example.rybackiapp.presentation.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rybackiapp.domain.usecase.ObserveFontSizeMassageUseCase
import com.example.rybackiapp.domain.usecase.ObserveNotificationUseCase
import com.example.rybackiapp.domain.usecase.ObserveThemeUseCase
import com.example.rybackiapp.domain.usecase.SetFontSizeMassageUseCase
import com.example.rybackiapp.domain.usecase.SetNotificationUseCase
import com.example.rybackiapp.domain.usecase.SetThemeUseCase
import com.example.rybackiapp.presentation.screens.settings.state.SettingEvent
import com.example.rybackiapp.presentation.screens.settings.state.SettingState
import com.example.rybackiapp.presentation.screens.settings.state.SettingUIState
import com.example.rybackiapp.presentation.screens.settings.state.Theme
import dagger.hilt.android.lifecycle.HiltViewModel

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val observeFontSizeMassageUseCase: ObserveFontSizeMassageUseCase,
    private val observeNotificationUseCase: ObserveNotificationUseCase,
    private val observeThemeUseCase: ObserveThemeUseCase,
    private val setFontSizeMassageUseCase: SetFontSizeMassageUseCase,
    private val setNotificationUseCase: SetNotificationUseCase,
    private val setThemeUseCase: SetThemeUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(SettingUIState())
    val state: StateFlow<SettingUIState> = _state

init {
    loadSetting()
}
    private fun loadSetting() {
        combine(
            observeFontSizeMassageUseCase().catch { emit(12) },
            observeNotificationUseCase().catch { emit(true) },
            observeThemeUseCase().catch { emit("light") }
        ) { fontSize, notifications, theme ->

            SettingUIState(
                notificationEnable = notifications,
                theme = Theme.fromValue(theme),
                fontSize = fontSize,
                state = SettingState.Success
            )
        }.onStart {
            emit(
                SettingUIState(
                    state = SettingState.Loading
                )
            )
        }.catch { exception ->
            _state.value = SettingUIState(
                state = SettingState.Error(
                    message = "Failed to load settings: ${exception.message}"
                )
            )
        }.onEach { newState ->
            _state.value = newState
        }.launchIn(viewModelScope)
    }


    fun onEvent(settingEvent: SettingEvent) {
        when (settingEvent) {
            is SettingEvent.FontSizeMessageChanged -> {
                viewModelScope.launch(Dispatchers.IO) {
                    setFontSizeMassageUseCase(
                        settingEvent.size
                    )
                }

            }

            is SettingEvent.NotificationEnableChanged -> {
                viewModelScope.launch(Dispatchers.IO) {
                    setNotificationUseCase(
                        settingEvent.enable
                    )
                }
            }

            is SettingEvent.ThemeChanged -> {
                viewModelScope.launch(Dispatchers.IO) {
                    setThemeUseCase(
                        settingEvent.theme
                    )
                }
            }
        }
    }
}