package com.example.rybackiapp.presentation.screens.signup.state

import com.example.rybackiapp.utils.Field

sealed class SignUpEvent {
    object SignUpBtnClicked : SignUpEvent()
    data class OnPasswordChanged(val text: String) : SignUpEvent()
    data class OnRepeatPasswordChanged(val text: String) : SignUpEvent()
    data class OnNameChanged(val text: String) : SignUpEvent()
    data class OnEmailChanged(val text: String) : SignUpEvent()
    data class OnOnTagNameChanged(val text: String) : SignUpEvent()
    data class OnYearChanged(val text: String) : SignUpEvent()
}

data class SignUpUIState(
    val signUpState: SignUpState = SignUpState.Idle,
    val email: Field = Field(),
    val name: Field = Field(),
    val tagName: Field = Field(),
    val year: Field = Field(),
    val password: Field = Field(),
    val repeatPassword: Field = Field(),
    val buttonEnable: Boolean = false
)


sealed class SignUpState {
    object Idle : SignUpState()
    object Loading : SignUpState()
    object Success : SignUpState()
    data class Error(val message: String) : SignUpState()
}