package com.example.rybackiapp.presentation.screens.userprofile.state


sealed class UserProfileState {
    object Loading : UserProfileState()
    object Success : UserProfileState()
    data class Error(val message: String) : UserProfileState()
}

data class Profile(
    val id: String = "",
    val name: String? = null,
    val year: String? = null,
    val mainPhotoUrl: String? = null,
    val tagName: String? = null,
    val interests: String? = null,
    val chatId: String = ""
)

data class UserProfileUiState(
    val state: UserProfileState = UserProfileState.Success,
    val profile: Profile = Profile(),
    val isMute: Boolean = false
)

sealed class UserProfileEvent{
    data class Mute(val chatId: String): UserProfileEvent()
    data class Unmute(val chatId: String): UserProfileEvent()
}