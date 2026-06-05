package com.example.rybackiapp.presentation.screens.signin


import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.ActivityInfo
import android.util.Log
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.rybackiapp.R
import com.example.rybackiapp.presentation.navigation.Screen
import com.example.rybackiapp.presentation.screens.signin.state.SignInEvent
import com.example.rybackiapp.presentation.screens.signin.state.SignInEvent.SignInBtnClicked
import com.example.rybackiapp.presentation.screens.signin.state.SignInState
import com.example.rybackiapp.utils.AuthResult


@Composable
fun SignInScreen(
    logIn: () -> Unit,
    navigateTo: (Screen) -> Unit
) {

    val viewModel: SignInViewModel = hiltViewModel()
    val state by viewModel.signInState.collectAsState()
    //val authResult by viewModel.authResult.collectAsState()
    val activity = LocalActivity.current
//    LaunchedEffect(authResult) {
//        if (authResult is AuthResult.Success) {
//            logIn()
//        }
//    }

    LaunchedEffect(state.authResult) {
        if (state.authResult is AuthResult.Success) {
            logIn()
        }
    }
    DisposableEffect(Unit) {
        val originalOrientation = activity?.requestedOrientation
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        onDispose {
            // Возвращаем исходную ориентацию при уходе с экрана
            activity?.requestedOrientation = originalOrientation!!
        }
    }
    SignInView(
        state = state,
        onEvent = viewModel::onEvent,
        onNavigateTo = navigateTo
    )
}

@Composable
fun SignInView(
    //state: AuthResult? = null,
    state: SignInState = SignInState(),
    onEvent: (SignInEvent) -> Unit = {},
    onNavigateTo: (Screen) -> Unit = {}
) {
    val background = painterResource(id = R.drawable.ic_background)
    val chatIcon = painterResource(id = R.drawable.ic_app_icon1)
    //var email by rememberSaveable { mutableStateOf("") }
    //var password by rememberSaveable { mutableStateOf("") }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            modifier = Modifier.fillMaxSize(),
            painter = background,
            contentScale = ContentScale.Crop,
            contentDescription = "background "
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Transparent),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Image(
                painter = chatIcon,
                contentScale = ContentScale.Crop,
                contentDescription = "chat",
            )

            if (state.authResult is AuthResult.Error) {
                Text(state.authResult.message, color = MaterialTheme.colorScheme.error)
            }

            OutlinedTextField(
                value = state.email.text, //email,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 18.dp),
                // textStyle = TextStyle(fontSize = 10.sp),
                shape = RoundedCornerShape(16.dp),
                onValueChange = { newText -> /*email = newText*/
                    onEvent(
                        SignInEvent.OnEmailChanged(
                            newText
                        )
                    )
                },
                isError = state.email.isError,
                placeholder = { Text("Email") },
                supportingText = {
                    if (state.email.isError) {
                        state.email.error?.let { Text(it) }
                    }

                },
                colors = OutlinedTextFieldDefaults.colors(
                    // Цвет границы в состоянии ошибки
                    errorBorderColor = MaterialTheme.colorScheme.error,
                    errorLabelColor = MaterialTheme.colorScheme.error,
                    errorPlaceholderColor = MaterialTheme.colorScheme.error,
                    errorSupportingTextColor = MaterialTheme.colorScheme.error,
                    errorTrailingIconColor = MaterialTheme.colorScheme.error,

                    // Цвет фокуса (нормальное состояние)
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    focusedLabelColor = MaterialTheme.colorScheme.primary,

                    // Цвет обычного состояния
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                    unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            )
            OutlinedTextField(
                value = state.password.text,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 18.dp),
                //textStyle = TextStyle(fontSize = 25.sp),
                onValueChange = { newText -> /*password = newText*/
                    onEvent(
                        SignInEvent.OnPasswordChanged(
                            newText
                        )
                    )
                },
                shape = RoundedCornerShape(16.dp),
                placeholder = { Text("Password") },
                isError = state.password.isError,
                supportingText = {
                    if (state.password.isError) {
                        state.password.error?.let { Text(it) }
                    }

                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                visualTransformation = PasswordVisualTransformation()
            )

        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp)
                .align(Alignment.BottomCenter)
        ) {
            Button(
                onClick = { onEvent(SignInBtnClicked) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color.Black
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                border = BorderStroke(1.dp, Color.DarkGray),
                enabled = state.buttonEnable
            ) { Text("Sign In", modifier = Modifier.padding(8.dp), fontSize = 18.sp) }

            TextButton(
                onClick = {
                    onNavigateTo(Screen.SignUp)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Create account", fontSize = 19.sp)
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun SignInScreenPreview() {
    SignInView()
}