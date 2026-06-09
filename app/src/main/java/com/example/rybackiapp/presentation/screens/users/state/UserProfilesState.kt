package com.example.rybackiapp.presentation.screens.users.state


data class InterestItemUI(
    val id: Int,
    val name: String,
    val nameEng: String
)

data class Interest(
    val id: Int = 0,
    val name: String = "",
    val interestState: InterestState = InterestState.NEUTRAL
)

data class InterestGroupUI(
    val id: Int,
    val name: String,
    val nameEng: String,
    val items: List<InterestItemUI>
)

sealed class UserProfilesState {
    object Loading : UserProfilesState()
    object Success : UserProfilesState()
    data class Error(val message: String) : UserProfilesState()
}

data class Profile(
    val id: String = "",
    val isYou: Boolean = false,
    val name: String? = null,
    val isOnline: Boolean = false,
    val year: String? = null,
    val mainPhotoUrl: String? = null,
    val chatId: String = ""
)

data class ProfileList(
    val profiles: List<Profile> = emptyList()
)


data class FilterUI(
    val minAge: Float = 14f,
    val maxAge: Float = 100f,
    val desirableInterests: Set<Int> = emptySet(),
    val unwantedInterests: Set<Int> = emptySet()
)

data class UserProfilesUIState(
    val state: UserProfilesState = UserProfilesState.Success,
    val profileList: ProfileList = ProfileList(),
    val interestsGroups: List<InterestGroupUI?> = emptyList(),
    val filterUI: FilterUI = FilterUI(),
    val search: String = ""
)

enum class InterestState {
    NEUTRAL,
    POSITIVE,
    NEGATIVE
}


sealed class UsersProfileEvent {
    data class SaveFiler(val filterUI: FilterUI) : UsersProfileEvent()
    object OnResetFilter : UsersProfileEvent()
    data class SearchTextFieldChanged(val search: String) : UsersProfileEvent()
    data object SearchClicked : UsersProfileEvent()
}