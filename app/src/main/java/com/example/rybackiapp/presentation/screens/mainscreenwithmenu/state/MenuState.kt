package com.example.rybackiapp.presentation.screens.mainscreenwithmenu.state

import com.example.rybackiapp.presentation.navigation.Screen

data class MenuItemState(
    val screen: Screen = Screen.Account,
    val eventText: String? = null,
    val icon: Int = 0,
    val label: String = "Аккаунт",
)

data class MenuState(
    val items: List<MenuItemState> = emptyList()
)

