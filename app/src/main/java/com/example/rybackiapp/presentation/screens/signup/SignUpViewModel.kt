package com.example.rybackiapp.presentation.screens.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rybackiapp.domain.usecase.CreateUserProfileUseCase
import com.example.rybackiapp.domain.usecase.SignInUseCase
import com.example.rybackiapp.domain.usecase.SignUpUseCase
import com.example.rybackiapp.presentation.screens.signup.state.SignUpEvent
import com.example.rybackiapp.presentation.screens.signup.state.SignUpState
import com.example.rybackiapp.presentation.screens.signup.state.SignUpUIState
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
import java.time.Year
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val signUpUseCase: SignUpUseCase,
    private val createUserProfileUseCase: CreateUserProfileUseCase
) : ViewModel() {

//    private val _state = MutableStateFlow<SignUpState>(SignUpState.Idle)
//    val state: StateFlow<SignUpState> = _state

    private val _state = MutableStateFlow(SignUpUIState())
    val state: StateFlow<SignUpUIState> = _state

    private val _events = MutableSharedFlow<SignUpEvent>(replay = 1)

    init {
        _events
            .filterIsInstance<SignUpEvent.OnEmailChanged>()
            .map { it.text }
            .debounce(300)
            .filter { it.length >= 3 }
            .onEach { email ->
                validateAndUpdateEmailState(email)
            }
            .launchIn(viewModelScope)

        _events
            .filterIsInstance<SignUpEvent.OnPasswordChanged>()
            .map { it.text }
            .debounce(300)
            .filter { it.length >= 3 }
            .onEach { password ->
                validateAndUpdatePasswordState(password)
            }
            .launchIn(viewModelScope)

        _events
            .filterIsInstance<SignUpEvent.OnRepeatPasswordChanged>()
            .map { it.text }
            .debounce(300)
            .filter { it.length >= 3 }
            .onEach { password ->
                validateAndUpdateRepeatPasswordState(password)
            }
            .launchIn(viewModelScope)

        _events
            .filterIsInstance<SignUpEvent.OnNameChanged>()
            .map { it.text }
            .debounce(300)
            .filter { it.length >= 3 }
            .onEach { name ->
                validateAndUpdateNameState(name)
            }
            .launchIn(viewModelScope)

        _events
            .filterIsInstance<SignUpEvent.OnYearChanged>()
            .map { it.text }
            .debounce(300)
            .filter { it.length >= 3 }
            .onEach { year ->
                validateAndUpdateYearState(year)
            }
            .launchIn(viewModelScope)

        _events
            .filterIsInstance<SignUpEvent.OnYearChanged>()
            .map { it.text }
            .debounce(300)
            .filter { it.length >= 3 }
            .onEach { year ->
                validateAndUpdateYearState(year)
            }
            .launchIn(viewModelScope)

        _events
            .filterIsInstance<SignUpEvent.OnOnTagNameChanged>()
            .map { it.text }
            .debounce(300)
            .filter { it.length >= 3 }
            .onEach { tagName ->
                validateAndUpdateTagNameState(tagName)
            }
            .launchIn(viewModelScope)
    }

    fun onEvent(signUpEvent: SignUpEvent) {
        when (signUpEvent) {
            is SignUpEvent.SignUpBtnClicked -> signUp(
                email = state.value.email.text,
                password = state.value.password.text,
                name = state.value.name.text,
                year = state.value.year.text,
                tagName = state.value.tagName.text
            )

            is SignUpEvent.OnEmailChanged -> {
                _state.update {
                    it.copy(
                        email = it.email.copy(
                            text = signUpEvent.text
                        )
                    )
                }
                _events.tryEmit(
                    signUpEvent
                )
            }

            is SignUpEvent.OnNameChanged -> {
                _state.update {
                    it.copy(
                        name = it.name.copy(
                            text = signUpEvent.text
                        )
                    )
                }
                _events.tryEmit(
                    signUpEvent
                )
            }

            is SignUpEvent.OnPasswordChanged -> {
                _state.update {
                    it.copy(
                        password = it.password.copy(
                            text = signUpEvent.text
                        )
                    )
                }
                _events.tryEmit(
                    signUpEvent
                )
            }

            is SignUpEvent.OnRepeatPasswordChanged -> {
                _state.update {
                    it.copy(
                        repeatPassword = it.repeatPassword.copy(
                            text = signUpEvent.text
                        )
                    )
                }
                _events.tryEmit(
                    signUpEvent
                )
            }

            is SignUpEvent.OnYearChanged -> {
                _state.update {
                    it.copy(
                        year = it.year.copy(
                            text = signUpEvent.text
                        )
                    )
                }
                _events.tryEmit(
                    signUpEvent
                )
            }

            is SignUpEvent.OnOnTagNameChanged -> {
                _state.update {
                    it.copy(
                        tagName = it.tagName.copy(
                            text = signUpEvent.text
                        )
                    )
                }
                _events.tryEmit(
                    signUpEvent
                )
            }
        }
    }

    private fun signUp(
        email: String,
        password: String,
        name: String,
        year: String,
        tagName: String
    ) {
        viewModelScope.launch {
            _state.update {
                it.copy(signUpState = SignUpState.Loading)
            }

            signUpUseCase(email, password)
                .onSuccess { authResult ->
                    val id = authResult.id

                    createUserProfileUseCase(
                        id = id,
                        name = name,
                        year = year,
                        tagName = tagName
                    ).onSuccess {
                        _state.update {
                            it.copy(signUpState = SignUpState.Success)
                        }
                    }.onFailure {

                        _state.update { state ->
                            state.copy(
                                signUpState = SignUpState.Error(
                                    it.message ?: "Profile save error"
                                )
                            )
                        }
                    }

                }.onFailure {

                    _state.update { state ->
                        state.copy(
                            signUpState = SignUpState.Error(
                                it.message ?: "Registration error"
                            )
                        )
                    }
                }
        }
    }


    private fun validateAndUpdateEmailState(email: String) {

        _state.update { currentState ->

            val emailValid = Validator.validateEmail(email)

            val isEmailValid = email.isNotEmpty() && emailValid == null
            val isNameValid = currentState.name.isCorrect
            val isYearValid = currentState.year.isCorrect
            val isPasswordValid = currentState.password.isCorrect
            val isRepeatPasswordValid = currentState.repeatPassword.isCorrect
            val isTagNameValid = currentState.tagName.isCorrect


            val email = Field(
                text = email,
                isError = !isEmailValid,
                isCorrect = isEmailValid,
                error = emailValid
            )
            val isButtonEnable =
                isEmailValid && isPasswordValid && isNameValid && isYearValid && isRepeatPasswordValid && isTagNameValid

            currentState.copy(email = email, buttonEnable = isButtonEnable)
        }
    }

    private fun validateAndUpdatePasswordState(password: String) {
        _state.update { currentState ->

            val passwordValid = Validator.validatePassword(password)
            val isPasswordValid = password.isNotEmpty() && passwordValid == null
            val isEmailValid = currentState.email.isCorrect
            val isNameValid = currentState.name.isCorrect
            val isYearValid = currentState.year.isCorrect
            val isRepeatPasswordValid = currentState.repeatPassword.isCorrect
            val isTagNameValid = currentState.tagName.isCorrect

            val password =
                Field(
                    text = password,
                    isError = !isPasswordValid,
                    isCorrect = isPasswordValid,
                    error = passwordValid
                )
            val isButtonEnable =
                isEmailValid && isPasswordValid && isNameValid && isYearValid && isRepeatPasswordValid && isTagNameValid

            currentState.copy(password = password, buttonEnable = isButtonEnable)
        }
    }

    private fun validateAndUpdateRepeatPasswordState(repeatPassword: String) {
        _state.update { currentState ->

            val repeatPasswordValid = Validator.validatePassword(repeatPassword)

            val isRepeatPasswordValid =
                repeatPassword.isNotEmpty() && repeatPasswordValid == null && repeatPassword == currentState.password.text
            val isEmailValid = currentState.email.isCorrect
            val isNameValid = currentState.name.isCorrect
            val isYearValid = currentState.year.isCorrect
            val isPasswordValid = currentState.password.isCorrect
            val isTagNameValid = currentState.tagName.isCorrect

            val password =
                Field(
                    text = repeatPassword,
                    isError = !isRepeatPasswordValid,
                    isCorrect = isRepeatPasswordValid,
                    error = repeatPasswordValid
                )
            val isButtonEnable =
                isEmailValid && isPasswordValid && isNameValid && isYearValid && isRepeatPasswordValid && isTagNameValid

            currentState.copy(repeatPassword = password, buttonEnable = isButtonEnable)
        }
    }

    private fun validateAndUpdateNameState(name: String) {
        _state.update { currentState ->


            val isRepeatPasswordValid = currentState.repeatPassword.isCorrect
            val isEmailValid = currentState.email.isCorrect

            val nameValid = Validator.validateName(name)
            val isNameValid = name.isNotEmpty() && nameValid == null
            val isYearValid = currentState.year.isCorrect
            val isPasswordValid = currentState.password.isCorrect
            val isTagNameValid = currentState.tagName.isCorrect

            val name =
                Field(
                    text = name,
                    isError = !isNameValid,
                    isCorrect = isNameValid,
                    error = nameValid
                )
            val isButtonEnable =
                isEmailValid && isPasswordValid && isNameValid && isYearValid && isRepeatPasswordValid && isTagNameValid

            currentState.copy(name = name, buttonEnable = isButtonEnable)
        }
    }

    private fun validateAndUpdateYearState(year: String) {
        _state.update { currentState ->


            val isRepeatPasswordValid = currentState.repeatPassword.isCorrect
            val isEmailValid = currentState.email.isCorrect


            val isNameValid = currentState.name.isCorrect
            val yearValid = Validator.validateYear(year)
            val isYearValid = yearValid == null
            val isPasswordValid = currentState.password.isCorrect
            val isTagNameValid = currentState.tagName.isCorrect

            val year =
                Field(
                    text = year,
                    isError = !isYearValid,
                    isCorrect = isYearValid,
                    error = yearValid
                )
            val isButtonEnable =
                isEmailValid && isPasswordValid && isNameValid && isYearValid && isRepeatPasswordValid && isTagNameValid

            currentState.copy(year = year, buttonEnable = isButtonEnable)
        }
    }

    private fun validateAndUpdateTagNameState(tagName: String) {
        _state.update { currentState ->


            val isRepeatPasswordValid = currentState.repeatPassword.isCorrect
            val isEmailValid = currentState.email.isCorrect


            val isNameValid = currentState.name.isCorrect
            val tagNameValid = Validator.validateTagName(tagName)
            val isTagNameValid = tagNameValid == null
            val isYearValid = currentState.year.isCorrect
            val isPasswordValid = currentState.password.isCorrect


            val tagName =
                Field(
                    text = tagName,
                    isError = !isTagNameValid,
                    isCorrect = isTagNameValid,
                    error = tagNameValid
                )
            val isButtonEnable =
                isEmailValid && isPasswordValid && isNameValid && isYearValid && isRepeatPasswordValid && isTagNameValid

            currentState.copy(tagName = tagName, buttonEnable = isButtonEnable)
        }
    }
}