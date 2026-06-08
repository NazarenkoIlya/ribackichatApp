package com.example.rybackiapp.presentation.screens.userprofile

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.rybackiapp.R
import com.example.rybackiapp.presentation.components.IconButtonWithTextComponent
import com.example.rybackiapp.presentation.components.ImageComponent
import com.example.rybackiapp.presentation.components.YourselfCard
import com.example.rybackiapp.presentation.navigation.Screen
import com.example.rybackiapp.presentation.screens.userprofile.state.UserProfileEvent
import com.example.rybackiapp.presentation.screens.userprofile.state.UserProfileState
import com.example.rybackiapp.presentation.screens.userprofile.state.UserProfileUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserProfileScreen(
    userId: String,
    chatId: String,
    onBackClick: () -> Unit,
    onNavigateTo: (String) -> Unit = {},
    onNavigateToChatDetail: (String) -> Unit = {}
) {
    val viewModel: UserProfileViewModel = hiltViewModel()
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadProfile(userId,chatId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            ImageVector.vectorResource(R.drawable.ic_back),
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                },
                title = {
                    Text(
                        text = "User profile",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    scrolledContainerColor = MaterialTheme.colorScheme.primaryContainer
                ),
                scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
            )
        },
        content = { innerPadding ->
            Box {
                Image(
                    painter = painterResource(R.drawable.ic_background),
                    modifier = Modifier.fillMaxSize(),
                    contentDescription = "background",
                    contentScale = ContentScale.Crop
                )
                UserProfileView(
                    state = state,
                    chatId = chatId,
                    onNavigateTo = onNavigateTo,
                    modifier = Modifier.padding(innerPadding),
                    onEvent = viewModel::onEvent
                )
            }
        }
    )
}

@Composable
fun UserProfileView(
    state: UserProfileUiState = UserProfileUiState(),
    onNavigateTo: (String) -> Unit = {},
    onEvent: (UserProfileEvent) -> Unit = {},
    chatId: String = "",
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {

    when (state.state) {
        is UserProfileState.Error -> {
            Text(
                state.state.message,
                color = MaterialTheme.colorScheme.error
            )
        }

        UserProfileState.Loading -> CircularProgressIndicator()
        is UserProfileState.Success -> {

            val profile = state.profile
            Column(
                modifier = modifier,
                verticalArrangement = Arrangement.Center
            ) {

                ImageComponent(
                    state.profile.mainPhotoUrl
                )
                Column(
                    modifier = Modifier
                        .weight(.90f)
                        .padding(horizontal = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        profile.name ?: " Alex ",
                        modifier = Modifier.padding(start = 12.dp),
                        fontSize = 34.sp
                    )
                    Spacer(modifier = Modifier.padding(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        IconButtonWithTextComponent(
                            icon = R.drawable.ic_chats1,
                            text = "Chat",
                            modifier = Modifier.weight(0.45f),
                            onClick = {
                                onNavigateTo(
                                    Screen.ChatDetail.createRoute(chatId)
                                )
                            }
                        )
                        Spacer(modifier = Modifier.padding(2.dp))
                        IconButtonWithTextComponent(
                            icon = if (state.isMute) R.drawable.ic_bell_off else R.drawable.ic_bell,
                            text = "Sound",
                            modifier = Modifier.weight(0.45f),
                            onClick = {
                                if (state.isMute) {
                                    onEvent(UserProfileEvent.Unmute(chatId))
                                } else {
                                    onEvent(UserProfileEvent.Mute(chatId))
                                }
                            }
                        )
                    }
                    YourselfCard(
                        email = "",
                        aboutMe = profile.interests ?: "",
                        year = profile.year ?: "",
                        nameUser = profile.tagName ?: "",
                    )
                }

            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun UserProfilePreview() {
    UserProfileView()
}