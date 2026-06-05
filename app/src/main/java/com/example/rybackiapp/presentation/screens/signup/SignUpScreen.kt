package com.example.rybackiapp.presentation.screens.signup

import android.content.pm.ActivityInfo
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.rybackiapp.R
import com.example.rybackiapp.presentation.navigation.Screen
import com.example.rybackiapp.presentation.screens.signin.state.SignInEvent.SignInBtnClicked
import com.example.rybackiapp.presentation.screens.signup.state.SignUpEvent
import com.example.rybackiapp.presentation.screens.signup.state.SignUpEvent.SignUpBtnClicked
import com.example.rybackiapp.presentation.screens.signup.state.SignUpState
import com.example.rybackiapp.presentation.screens.signup.state.SignUpUIState
import com.example.rybackiapp.utils.AuthResult

@Composable
fun SignUpScreen(
    logIn: () -> Unit,
    //navigateTo: (Screen) -> Unit
) {

    val viewModel: SignUpViewModel = hiltViewModel()
    val state by viewModel.state.collectAsState()
    val activity = LocalActivity.current
    LaunchedEffect(state.signUpState) {
        if (state.signUpState is SignUpState.Success) {
            //navigateTo(Screen.Account)
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
    SignUpView(
        state = state,
        onEvent = viewModel::onEvent,
        //     onNavigateTo = navigateTo
    )
}

@Composable
fun SignUpView(
    state: SignUpUIState = SignUpUIState(),
    onEvent: (SignUpEvent) -> Unit = {},
    // onNavigateTo: (Screen) -> Unit = {}
) {

    val background = painterResource(id = R.drawable.ic_background)
    val chatIcon = painterResource(id = R.drawable.ic_app_icon1)

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
            //verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Image(
                painter = chatIcon,
                contentDescription = "hellp"
            )
            if (state.signUpState is SignUpState.Error) {
                Text(state.signUpState.message, color = MaterialTheme.colorScheme.error)
            }
            OutlinedTextField(
                value = state.email.text,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                // textStyle = TextStyle(fontSize = 10.sp),
                shape = RoundedCornerShape(16.dp),
                onValueChange = { newText -> onEvent(SignUpEvent.OnEmailChanged(newText)) },
                placeholder = { Text("Email") },
                isError = state.email.isError,
                supportingText = {
                    if (state.email.isError) {
                        state.email.error?.let { Text(it) }
                    }
                }
            )
            OutlinedTextField(
                value = state.name.text,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                // textStyle = TextStyle(fontSize = 10.sp),
                shape = RoundedCornerShape(16.dp),
                onValueChange = { newName -> onEvent(SignUpEvent.OnNameChanged(newName)) },
                placeholder = { Text("Name") },
                isError = state.name.isError,
                supportingText = {
                    if (state.name.isError) {
                        state.name.error?.let { Text(it) }
                    }
                }
            )

            OutlinedTextField(
                value = state.tagName.text,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                // textStyle = TextStyle(fontSize = 10.sp),
                shape = RoundedCornerShape(16.dp),
                onValueChange = { newName -> onEvent(SignUpEvent.OnOnTagNameChanged(newName)) },
                placeholder = { Text("TagName") },
                isError = state.tagName.isError,
                supportingText = {
                    if (state.tagName.isError) {
                        state.tagName.error?.let { Text(it) }
                    }
                }
            )

            OutlinedTextField(
                value = state.year.text,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                // textStyle = TextStyle(fontSize = 10.sp),
                shape = RoundedCornerShape(16.dp),
                onValueChange = { newYear -> onEvent(SignUpEvent.OnYearChanged(newYear)) },
                placeholder = { Text("Year") },
                isError = state.year.isError,
                supportingText = {
                    if (state.year.isError) {
                        state.year.error?.let { Text(it) }
                    }
                }
            )

            OutlinedTextField(
                value = state.password.text,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                //textStyle = TextStyle(fontSize = 25.sp),
                onValueChange = { newText -> onEvent(SignUpEvent.OnPasswordChanged(newText)) },
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

            OutlinedTextField(
                value = state.repeatPassword.text,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                //textStyle = TextStyle(fontSize = 25.sp),
                onValueChange = { newText -> onEvent(SignUpEvent.OnRepeatPasswordChanged(newText)) },
                shape = RoundedCornerShape(16.dp),
                placeholder = { Text("Repeat Password") },
                isError = state.repeatPassword.isError,
                supportingText = {
                    if (state.repeatPassword.isError) {
                        state.repeatPassword.error?.let { Text(it) }
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
                onClick = { onEvent(SignUpBtnClicked) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color.Black
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                border = BorderStroke(1.dp, Color.DarkGray),
                enabled = state.buttonEnable
            ) { Text("Sign Up", modifier = Modifier.padding(8.dp), fontSize = 18.sp) }

        }
    }

}

@Preview(showBackground = true)
@Composable
fun SignInScreenPreview() {
    SignUpView()
}