package com.example.rybackiapp.presentation.navigation


import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import com.example.rybackiapp.presentation.screens.mainscreenwithmenu.MainAppScreen
import com.example.rybackiapp.presentation.navigation.AppStartViewModel.NavGraphs
import com.example.rybackiapp.presentation.screens.account.old_version.AccountScreen
import com.example.rybackiapp.presentation.screens.chatdetail.ChatDetailScreen
import com.example.rybackiapp.presentation.screens.chatlist.old_version.ChatListScreen
import com.example.rybackiapp.presentation.screens.editprofile.EditProfileScreen
import com.example.rybackiapp.presentation.screens.settings.SettingsScreen
import com.example.rybackiapp.presentation.screens.signin.SignInScreen
import com.example.rybackiapp.presentation.screens.signup.SignUpScreen
import com.example.rybackiapp.presentation.screens.userprofile.UserProfileScreen
import com.example.rybackiapp.presentation.screens.users.UserProfilesScreen


sealed class Screen(val route: String) {

    object SignIn : Screen("signin")
    object SignUp : Screen("signup")


    object ChatList : Screen(route = "chatlist")
    object UserProfileList : Screen(route = "users")
    object Account : Screen(route = "account")


    object EditProfile : Screen("edit")
    object Settings : Screen("settings")

    data class UserProfile(val userId: String, val chatId: String) :
        Screen("user_profile/{userId}/{chatId}") {

        companion object {
            fun createRoute(userId: String, chatId: String): String = "user_profile/$userId/$chatId"
            const val ROUTE = "user_profile/{userId}/{chatId}"
        }
    }

    data class ChatDetail(
        val chatId: String
    ) : Screen("chatdetail/{chatId}") {

        companion object {
            fun createRoute(chatId: String) = "chatdetail/$chatId"
            const val ROUTE = "chatdetail/{chatId}"
        }
    }
}


@Composable
fun AppNavigation(
    modifier: Modifier = Modifier,
    startDestination: String? = null,
    navHostController: NavHostController
) {

    startDestination?.let { start ->
        NavHost(
            modifier = modifier,
            navController = navHostController,
            startDestination = start
        ) {

            navigation(
                route = NavGraphs.AUTH_GRAPH,
                startDestination = Screen.SignIn.route
            ) {
                composable(Screen.SignIn.route) {
                    SignInScreen(
                        logIn = {
                            navHostController.navigate(NavGraphs.MAIN_APP_GRAPH) {
                                popUpTo(NavGraphs.AUTH_GRAPH) { inclusive = true }
                                launchSingleTop = true
                            }
                        },
                        navigateTo = { navigateTo ->
                            navHostController.navigate(navigateTo.route)
                        }
                    )
                }

                composable(Screen.SignUp.route) {
                    SignUpScreen {
                        navHostController.navigate(NavGraphs.MAIN_APP_GRAPH) {
                            popUpTo(NavGraphs.AUTH_GRAPH) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                }
            }

            navigation(
                route = NavGraphs.MAIN_APP_GRAPH,
                startDestination = Screen.Account.route
            ) {
                composable(Screen.ChatList.route) {
                    MainAppScreen(
                        navHostController = navHostController,
                        currentScreen = Screen.ChatList
                    ) {
                        ChatListScreen(
                            navigateTo = { navigateTo ->
                                navHostController.navigate(navigateTo)
                            }
                        )
                    }
                }

                composable(Screen.UserProfileList.route) {
                    MainAppScreen(
                        navHostController = navHostController,
                        currentScreen = Screen.UserProfileList
                    ) {
                        UserProfilesScreen(
                            onNavigateToUserProfile = { navigateTo ->
                                if (navigateTo == Screen.Account.route) {
                                    navHostController.navigate(navigateTo) {
                                        popUpTo(0) { inclusive = true }
                                        launchSingleTop = true
                                    }
                                } else {
                                    navHostController.navigate(navigateTo)
                                }
                            },
                            onNavigateToChatDetail = { navigateTo ->
                                navHostController.navigate(navigateTo)
                            }
                        )
                    }
                }

                composable(Screen.Account.route) {
                    MainAppScreen(
                        navHostController = navHostController,
                        currentScreen = Screen.Account
                    ) {
                        AccountScreen(
                            navigateTo = { navigateTo ->
                                navHostController.navigate(navigateTo.route)
                            },
                            onNavigateToChatDetail = { navigateTo ->
                                navHostController.navigate(navigateTo)
                            },
                            onLogout = {
                                navHostController.navigate(NavGraphs.AUTH_GRAPH) {
                                    popUpTo(NavGraphs.MAIN_APP_GRAPH) { inclusive = true }
                                    launchSingleTop = true
                                }
                            }
                        )
                    }
                }

                composable(Screen.EditProfile.route) {
                    EditProfileScreen(
                        onBackClick = {
                            navHostController.popBackStack()
                        }
                    )
                }

                composable(Screen.Settings.route) {
                    SettingsScreen(
                        onBackClick = {
                            navHostController.popBackStack()
                        }
                    )
                }

                composable(
                    route = Screen.UserProfile.ROUTE,
                    arguments = listOf(
                        navArgument("userId") {
                            type = NavType.StringType
                            nullable = false
                        },
                        navArgument("chatId") {
                            type = NavType.StringType
                            nullable = false
                        }
                    )
                ) { backStackEntry ->
                    val userId = backStackEntry.arguments?.getString("userId") ?: return@composable
                    val chatId = backStackEntry.arguments?.getString("chatId") ?: return@composable

                    UserProfileScreen(
                        userId = userId,
                        chatId = chatId,
                        onBackClick = { navHostController.popBackStack() },
                        onNavigateTo = { navigateTo ->
                            navHostController.navigate(navigateTo)
                        }
                    )
                }

                composable(
                    route = Screen.ChatDetail.ROUTE,
                    arguments = listOf(
                        navArgument("chatId") {
                            type = NavType.StringType
                            nullable = false
                        }
                    )
                ) { backStackEntry ->
                    val chatId = backStackEntry.arguments?.getString("chatId") ?: return@composable
                    ChatDetailScreen(
                        chatId = chatId,
                        onBackClick = {
                            navHostController.popBackStack()
                        }
                    )
                }
            }
        }
    }
}