package com.example.rybackiapp.presentation.navigation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rybackiapp.domain.usecase.CheckAuthStateUseCase
import com.example.rybackiapp.domain.usecase.ObserveThemeUseCase
import com.example.rybackiapp.presentation.screens.settings.state.Theme
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class AppStartViewModel @Inject constructor(
    private val checkAuthStateUseCase: CheckAuthStateUseCase,
    private val observeThemeUseCase: ObserveThemeUseCase
) : ViewModel() {

    private val _startDestination = MutableStateFlow<String?>(null)
    val startDestination: StateFlow<String?> = _startDestination

    private val _theme = observeThemeUseCase()
        .map { themeString ->
            Theme.fromValue(themeString)
        }
        .catch { exception ->
            Log.e("Theme", "Error converting theme", exception)
            emit(Theme.SYSTEM)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = Theme.SYSTEM
        )
    val theme: StateFlow<Theme> = _theme

    init {
        decideStartGraph()

    }

    private fun decideStartGraph() {
        _startDestination.value = if (checkAuthStateUseCase()) {
            NavGraphs.MAIN_APP_GRAPH
        } else {
            NavGraphs.AUTH_GRAPH
        }
    }


    object NavGraphs {
        const val AUTH_GRAPH = "auth_graph"
        const val MAIN_APP_GRAPH = "main_app_graph"
    }
}