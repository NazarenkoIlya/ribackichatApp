package com.example.rybackiapp.presentation.screens.editprofile.state


data class InterestItemUI(
    val id: Int,
    val name: String,
    val nameEng: String
)

data class InterestGroupUI(
    val id: Int,
    val name: String,
    val nameEng: String,
    val items: List<InterestItemUI>
)

data class EditableField(
    val currentValue: String = "",
    val newValue: String = "",
    val isError: Boolean = false,
    val error: String? = ""
)

data class _EditProfile(
    val id: String = "",
    val name: EditableField = EditableField(),
    val tagName: EditableField = EditableField(),
    val year: EditableField = EditableField(),
    val interests: Set<InterestItemUI> = emptySet()
)


sealed class _EditProfileState {

    object Loading : _EditProfileState()
    object Success : _EditProfileState()
    data class Error(val message: String) : _EditProfileState()
    object Saved : _EditProfileState()
}

data class EditProfileUIState(
    val profile: _EditProfile = _EditProfile(),
    val state: _EditProfileState = _EditProfileState.Loading,
    val interestsGroups: List<InterestGroupUI?> = emptyList()
)

sealed class EditProfileEvent {
    data class EditNameChanged(val newName: String) : EditProfileEvent()
    data class EditYearChanged(val year: String) : EditProfileEvent()
    data class EditTagNameChanged(val tagName: String) : EditProfileEvent()
    data class EditInterestsChanged(val interestItems: Set<InterestItemUI>) : EditProfileEvent()
    object SaveBtnClicked : EditProfileEvent()
}

data class EditProfile(
    val id: String = "",
    val name: String = "",
    val newName: String = "",
    val email: String = "",
    val newAge: String = "",
    val age: String = "",
    val mainPhotoUrl: String? = null,
    val photos: Map<String, String>? = null
)

