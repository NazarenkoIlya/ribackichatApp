package com.example.rybackiapp.presentation.screens.userprofile

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rybackiapp.domain.model.InterestGroup
import com.example.rybackiapp.domain.model.UserProfile
import com.example.rybackiapp.domain.usecase.GetUserProfileUseCase
import com.example.rybackiapp.domain.usecase.GetUserProfilesUseCase
import com.example.rybackiapp.domain.usecase.IsMutedChatUseCase
import com.example.rybackiapp.domain.usecase.LoadInterestsUseCase
import com.example.rybackiapp.domain.usecase.MuteChatUseCase
import com.example.rybackiapp.domain.usecase.UnmuteUseCase
import com.example.rybackiapp.presentation.screens.userprofile.state.Profile
import com.example.rybackiapp.presentation.screens.userprofile.state.UserProfileEvent
import com.example.rybackiapp.presentation.screens.userprofile.state.UserProfileState
import com.example.rybackiapp.presentation.screens.userprofile.state.UserProfileUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserProfileViewModel @Inject constructor(
    private val getUserProfilesUseCase: GetUserProfilesUseCase,
    private val loadInterestsUseCase: LoadInterestsUseCase,
    private val isMutedChatUseCase: IsMutedChatUseCase,
    private val muteChatUseCase: MuteChatUseCase,
    private val unmuteChatUseCase: UnmuteUseCase

) : ViewModel() {

    private val _state = MutableStateFlow<UserProfileUiState>(UserProfileUiState())
    val state: StateFlow<UserProfileUiState> = _state


    fun loadProfile(id: String, chatId: String) {
        viewModelScope.launch {
            val interestsGroup = loadInterestsUseCase()

            getUserProfilesUseCase(id)
                .onSuccess { profile ->

                    _state.update {
                        it.copy(
                            state = UserProfileState.Success,
                            profile = profile.toMap(interestsGroup),
                            isMute = false
                        )
                    }

                    isMutedChatUseCase(chatId)
                        .onEach { isMute ->
                            Log.d("MUTE", "loadProfile: $isMute")
                            _state.update { currentState ->
                                currentState.copy(isMute = isMute)
                            }
                        }
                        .launchIn(viewModelScope)

                }
                .onFailure { error ->
                    _state.update {
                        it.copy(state = UserProfileState.Error(error.message ?: "Error"))
                    }
                }
        }
    }

    fun onEvent(event: UserProfileEvent) {
        when (event) {
            is UserProfileEvent.Mute -> {
                viewModelScope.launch(Dispatchers.IO) {
                    Log.d("MUTE", "onEvent: ${event.chatId}")
                    muteChatUseCase(event.chatId)
                }
            }

            is UserProfileEvent.Unmute -> {
                viewModelScope.launch(Dispatchers.IO) {
                    Log.d("MUTE", "onEvent: ${event.chatId}")
                    unmuteChatUseCase(event.chatId)
                }
            }
        }
    }

    private fun UserProfile.toMap(groups: List<InterestGroup?>): Profile {
        return Profile(
            id = id,
            name = name,
            year = year,
            mainPhotoUrl = mainPhotoUrl,
            tagName = tagName,
            chatId = chatId,
            interests = groups
                .asSequence()
                .filterNotNull()
                .flatMap { it.items }
                .filter { interests?.toSet()?.contains(it.id) == true }
                .joinToString(", ") { it.name }
        )
    }
}