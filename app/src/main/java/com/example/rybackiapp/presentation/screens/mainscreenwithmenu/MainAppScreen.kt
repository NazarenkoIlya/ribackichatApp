package com.example.rybackiapp.presentation.screens.mainscreenwithmenu

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.rybackiapp.presentation.navigation.Screen
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color

import androidx.hilt.navigation.compose.hiltViewModel
import com.example.rybackiapp.presentation.components.IconMenuItem
import com.example.rybackiapp.presentation.screens.mainscreenwithmenu.state.MenuState

@Composable
fun MainAppScreen(
    navHostController: NavHostController,
    currentScreen: Screen,
    content: @Composable () -> Unit
) {

    val viewModel: MainAppViewModel = hiltViewModel()
    val bottomNavScreens by viewModel.state.collectAsState(MenuState())
    val navBackStackEntry by navHostController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route


//    // Список экранов для нижнего меню
//    val bottomNavScreens = listOf(
//        Screen.ChatList,
//        Screen.UserProfileList,
//        Screen.Account
//    )

    Scaffold(
        bottomBar = {
            // Показываем нижнее меню только на корневых экранах
            if (bottomNavScreens.items.any { it.screen.route == currentRoute }) {
                NavigationBar(
                    containerColor =  MaterialTheme.colorScheme.background,
                    tonalElevation = 3.dp
                ) {
                    bottomNavScreens.items.forEach { screen ->
                        val isSelected = currentRoute == screen.screen.route

                        NavigationBarItem(
                            selected = isSelected,
                            onClick = {
                                if (!isSelected) {
                                    navHostController.navigate(screen.screen.route) {
                                        // Очищаем стек до этого экрана
                                        popUpTo(navHostController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            },
                            icon = {

                                IconMenuItem(
                                    icon = screen.icon,
                                    isSelected = isSelected,
                                    text = screen.eventText ?: ""

                                )
//                                Icon(
//                                    screen.icon ?: Icons.Default.MailOutline,
//                                    contentDescription = screen.label
//                                )
                            },
                            label = {
                                Text(screen.label ?: "")
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = MaterialTheme.colorScheme.primary,
                                selectedTextColor = MaterialTheme.colorScheme.primary,
                                unselectedIconColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                                unselectedTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            content()
        }
    }
}

// Вспомогательная функция для нахождения стартового дестинации
private fun NavGraph.findStartDestination(): NavDestination {
    return findNode(startDestinationId) ?: this
}