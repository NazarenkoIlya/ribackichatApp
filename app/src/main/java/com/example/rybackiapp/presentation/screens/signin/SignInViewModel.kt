package com.example.rybackiapp.presentation.screens.signin

import android.util.Log
import com.example.rybackiapp.presentation.screens.signin.state.SignInState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rybackiapp.domain.usecase.SignInUseCase
import com.example.rybackiapp.presentation.screens.signin.state.SignInEvent
import com.example.rybackiapp.utils.AuthResult
import com.example.rybackiapp.utils.Field
import com.example.rybackiapp.utils.Validator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class SignInViewModel @Inject constructor(
    private val signInUseCase: SignInUseCase
) : ViewModel() {


//    private val _authResult = MutableStateFlow<AuthResult?>(null)
//    val authResult: StateFlow<AuthResult?> = _authResult

    private val _signInState = MutableStateFlow(SignInState())
    val signInState: StateFlow<SignInState> = _signInState
    private val _events = MutableSharedFlow<SignInEvent>(replay = 1)

    init {
        _events
            .filterIsInstance<SignInEvent.OnEmailChanged>()
            .map { it.text }
            .debounce(300)
            .filter { it.length >= 3 }
            .onEach { email ->
                validateAndUpdateEmailState(email)
            }
            .launchIn(viewModelScope)

        _events
            .filterIsInstance<SignInEvent.OnPasswordChanged>()
            .map { it.text }
            .debounce(300)
            .filter { it.length >= 3 }
            .onEach { password ->
                validateAndUpdatePasswordState(password)
            }
            .launchIn(viewModelScope)
    }


    fun onEvent(signInEvent: SignInEvent) {
        when (signInEvent) {
            is SignInEvent.SignInBtnClicked -> signIn(
                _signInState.value.email.text,
                _signInState.value.password.text
            )

            is SignInEvent.OnEmailChanged -> {
                _signInState.update {
                    it.copy(
                        email = it.email.copy(
                            text = signInEvent.text
                        )
                    )
                }

                _events.tryEmit(
                    signInEvent
                )
            }

            is SignInEvent.OnPasswordChanged -> {

                _signInState.update {
                    it.copy(
                        password = it.password.copy(
                            text = signInEvent.text
                        )
                    )
                }

                _events.tryEmit(
                    signInEvent
                )
            }

        }
    }

    private fun signIn(email: String, password: String) {
        viewModelScope.launch {
            signInUseCase(email, password)
                .onSuccess {
                    _signInState.update {
                        it.copy(authResult = AuthResult.Success)
                    }

                }.onFailure {
                    _signInState.update { signIn ->
                        signIn.copy(authResult = AuthResult.Error(it.message ?: "Unknown error"))
                    }
                }
        }
    }


    private fun validateAndUpdateEmailState(email: String) {

        _signInState.update { currentState ->

            val emailValid = Validator.validateEmail(email)
            val isEmailValid = email.isNotEmpty() && emailValid == null
            val isPasswordValid = _signInState.value.password.isCorrect

            val email = Field(
                text = email,
                isError = !isEmailValid,
                isCorrect = isEmailValid,
                error = emailValid
            )
            val isButtonEnable = isEmailValid && isPasswordValid

            currentState.copy(email = email, buttonEnable = isButtonEnable)
        }
    }

    private fun validateAndUpdatePasswordState(password: String) {
        _signInState.update { currentState ->

            val passwordValid = Validator.validatePassword(password)
            val isPasswordValid = password.isNotEmpty() && passwordValid == null
            val isEmailValid = currentState.email.isCorrect

            val password =
                Field(
                    text = password,
                    isError = !isPasswordValid,
                    isCorrect = isPasswordValid,
                    error = passwordValid
                )
            val isButtonEnable = isEmailValid && isPasswordValid

            currentState.copy(password = password, buttonEnable = isButtonEnable)
        }
    }
}