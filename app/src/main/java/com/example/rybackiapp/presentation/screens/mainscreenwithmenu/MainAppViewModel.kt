package com.example.rybackiapp.presentation.screens.mainscreenwithmenu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rybackiapp.R
import com.example.rybackiapp.domain.usecase.ObserveUnreadChatsUseCase
import com.example.rybackiapp.domain.usecase.ObserveUserIdUseCase
import com.example.rybackiapp.manager.ResourcesManager
import com.example.rybackiapp.presentation.navigation.Screen
import com.example.rybackiapp.presentation.screens.mainscreenwithmenu.state.MenuItemState
import com.example.rybackiapp.presentation.screens.mainscreenwithmenu.state.MenuState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainAppViewModel @Inject constructor(
    private val observeUserIdUseCase: ObserveUserIdUseCase,
    private val observeUnreadChatsUseCase: ObserveUnreadChatsUseCase,
    private val resourcesManager: ResourcesManager
) : ViewModel() {

    private val _state = MutableStateFlow<MenuState>(MenuState())
    val state: StateFlow<MenuState> = _state


    init {
        loadMenu()
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
                    it.copy(listOf(chats, users, account))
                }
            }
        }
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    val state1 = observeUserIdUseCase.invoke().flatMapLatest { uid ->


        observeUnreadChatsUseCase.invoke(uid = uid)

    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = 0// MenuState()
    )
}