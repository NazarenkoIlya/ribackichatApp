package com.example.rybackiapp.presentation.screens.mainscreenwithmenu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rybackiapp.R
import com.example.rybackiapp.domain.usecase.ObserveConnectionStatusUseCase
import com.example.rybackiapp.domain.usecase.ObserveUnreadChatsUseCase
import com.example.rybackiapp.domain.usecase.ObserveUserIdUseCase
import com.example.rybackiapp.domain.usecase.SetUserOnlineUseCase
import com.example.rybackiapp.manager.ResourcesManager
import com.example.rybackiapp.presentation.navigation.Screen
import com.example.rybackiapp.presentation.screens.mainscreenwithmenu.state.MenuItemState
import com.example.rybackiapp.presentation.screens.mainscreenwithmenu.state.MenuState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainAppViewModel @Inject constructor(
    private val observeUserIdUseCase: ObserveUserIdUseCase,
    private val observeUnreadChatsUseCase: ObserveUnreadChatsUseCase,
    private val observeConnectionStatusUseCase: ObserveConnectionStatusUseCase,
    private val setUserOnlineUseCase: SetUserOnlineUseCase,
    private val resourcesManager: ResourcesManager
) : ViewModel() {

    private val _state = MutableStateFlow<MenuState>(MenuState())
    val state: StateFlow<MenuState> = _state


    init {
        loadMenu()
        observeConnectionStatus()
    }

    private fun loadMenu() {
        viewModelScope.launch {
            val account = MenuItemState(
                screen = Screen.Account,
                icon = R.drawable.ic_account,
                label = "Account"
            )

            val users = MenuItemState(
                screen = Screen.UserProfileList,
                icon = R.drawable.ic_users,
                label = "Users"
            )

            val id = observeUserIdUseCase.invoke().first()

            observeUnreadChatsUseCase.invoke(uid = id).collect { it ->
                val chats = MenuItemState(
                    screen = Screen.ChatList,
                    eventText = if (it == 0) "" else it.toString(),
                    icon = R.drawable.ic_chat11,
                    label = "Chats"
                )

                _state.update {
                    it.copy(items = listOf(chats, users, account))
                }
            }
        }
    }


    private fun observeConnectionStatus() {
        observeConnectionStatusUseCase()
            .distinctUntilChanged()
            .onEach { isConnected ->
                if (isConnected) {
                    viewModelScope.launch {
                        val id = observeUserIdUseCase.invoke().first()
                        setUserOnlineUseCase(id)
                    }
                }
            }.launchIn(viewModelScope)
    }

}