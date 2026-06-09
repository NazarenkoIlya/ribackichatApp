package com.example.rybackiapp.presentation.screens.account.old_version

import android.annotation.SuppressLint
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
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
import com.example.rybackiapp.presentation.screens.account.old_version.state.AccountEvent
import com.example.rybackiapp.presentation.screens.account.old_version.state.AccountEvent.OnImageSelected
import com.example.rybackiapp.presentation.screens.account.old_version.state.AccountState
import com.example.rybackiapp.presentation.screens.account.old_version.state.AccountUIState


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountScreen(
    navigateTo: (Screen) -> Unit,
    onNavigateToChatDetail: (String) -> Unit,
    onLogout: () -> Unit
) {

    val viewModel: AccountViewModel = hiltViewModel()
    val state by viewModel.state.collectAsState()


    LaunchedEffect(Unit) {
        viewModel.loadProfile()
    }

    Scaffold(
        content = { innerPadding ->

            Box {
                Image(
                    painter = painterResource(R.drawable.ic_background),
                    modifier = Modifier.fillMaxSize(),
                    contentDescription = "background",
                    contentScale = ContentScale.Crop
                )
                AccountView(
                    state = state,
                    onEvent = viewModel::onEvent,
                    onLogout = onLogout,
                    modifier = Modifier.padding(innerPadding),
                    navigateTo = navigateTo,
                    onNavigateToChatDetail = onNavigateToChatDetail
                )
            }

        }
    )


}


@Composable
fun AccountView(
    state: AccountUIState = AccountUIState(),
    onEvent: (AccountEvent) -> Unit = {},
    navigateTo: (Screen) -> Unit = {},
    onNavigateToChatDetail: (String) -> Unit = {},
    onLogout: () -> Unit = {},
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { uri ->
            onEvent(OnImageSelected(uri, context))
        }
    }


    Box {
        when (state.accountState) {
            is AccountState.Error -> {
                Log.d("Account", "AccountView: ${state.accountState.message}")
                Text(
                    state.accountState.message,
                    color = MaterialTheme.colorScheme.error
                )
            }

            AccountState.Loading -> CircularProgressIndicator()
            is AccountState.Success -> {
                val profile = state.accountProfile

                Column(
                    modifier = modifier.verticalScroll(rememberScrollState())
                ) {

                    ImageComponent(
                        state.accountProfile.mainPhotoUrl,
                        R.drawable.ic_user0,
                        isOnline = state.isOnline
                    )

                    Column(
                        modifier = Modifier
                            //.weight(.90f)
                            .padding(horizontal = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            profile.name ?: "Name",
                            fontSize = 34.sp,
                            lineHeight =1.sp
                        )
                        Text(
                            text = "Write a message",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier
                                .clickable {
                                    onNavigateToChatDetail(
                                        Screen.ChatDetail.createRoute(chatId = profile.chatId)
                                    )
                                }
                                .padding(top = 2.dp) // кастомный маленький отступ
                        )

                        Spacer(modifier = Modifier.padding(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {

                            IconButtonWithTextComponent(
                                icon = R.drawable.ic_camera,
                                text = "Photo",
                                modifier = Modifier.weight(0.33f),
                                onClick = {
                                    galleryLauncher.launch("image/*")
                                }
                            )
                            Spacer(modifier = Modifier.padding(2.dp))
                            IconButtonWithTextComponent(
                                icon = R.drawable.ic_edit,
                                text = "Edit",
                                modifier = Modifier.weight(0.33f),
                                onClick = {
                                    navigateTo(Screen.EditProfile)
                                }
                            )
                            Spacer(modifier = Modifier.padding(2.dp))
                            IconButtonWithTextComponent(
                                icon = R.drawable.ic_settings1,
                                text = "Settings",
                                modifier = Modifier.weight(0.33f),
                                onClick = {
                                    navigateTo(Screen.Settings)
                                }
                            )
                            Spacer(modifier = Modifier.padding(2.dp))
                            IconButtonWithTextComponent(
                                icon = R.drawable.ic_back,
                                text = "Sign Out",
                                modifier = Modifier.weight(0.33f),
                                onClick = { onEvent(AccountEvent.SignOutBtnClicked) }
                            )
                        }
                        Spacer(modifier = Modifier.padding(2.dp))
                        YourselfCard(
                            email = "",
                            aboutMe = profile.interests ?: "",
                            year = profile.year ?: "",
                            nameUser = profile.tagName ?: "",
                        )
                    }
                }
            }

            AccountState.SignOut -> {
                onLogout()
            }
        }

    }

}

@Preview(showBackground = true)
@Composable
fun SignInScreenPreview() {
    AccountView()
}