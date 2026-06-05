package com.example.rybackiapp.presentation.navigation

import androidx.lifecycle.ViewModel
import com.example.rybackiapp.domain.usecase.CheckAuthStateUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class AppStartViewModel @Inject constructor(
    private val checkAuthStateUseCase: CheckAuthStateUseCase
) : ViewModel() {
//    private val _startDestination = MutableStateFlow<Screen?>(null)
//    val startDestination: StateFlow<Screen?> = _startDestination
//
//    init {
//        decideStartScreen()
//    }
//
//    private fun decideStartScreen() {
//        _startDestination.value = (if (checkAuthStateUseCase())
//            Screen.Account else Screen.SignIn)
//    }


    private val _startDestination = MutableStateFlow<String?>(null)
    val startDestination: StateFlow<String?> = _startDestination

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