package com.example.rybackiapp.presentation.screens.signin.state

import com.example.rybackiapp.utils.AuthResult
import com.example.rybackiapp.utils.Field


data class SignInState(
    val authResult: AuthResult? = null,
    val email: Field = Field(),
    val password: Field = Field(),
    val buttonEnable: Boolean = false
)


sealed class SignInEvent {
    object SignInBtnClicked : SignInEvent()

    data class OnPasswordChanged(val text: String) : SignInEvent()
    data class OnEmailChanged(val text: String) : SignInEvent()
}