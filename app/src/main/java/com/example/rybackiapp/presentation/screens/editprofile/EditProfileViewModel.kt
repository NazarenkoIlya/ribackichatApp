package com.example.rybackiapp.presentation.screens.editprofile


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rybackiapp.domain.model.InterestGroup
import com.example.rybackiapp.domain.model.InterestItem
import com.example.rybackiapp.domain.model.MainProfile
import com.example.rybackiapp.domain.usecase.EditProfileUseCase
import com.example.rybackiapp.domain.usecase.GetUserProfileUseCase
import com.example.rybackiapp.domain.usecase.LoadInterestsUseCase
import com.example.rybackiapp.presentation.screens.editprofile.state.EditProfileEvent
import com.example.rybackiapp.presentation.screens.editprofile.state.EditProfileUIState
import com.example.rybackiapp.presentation.screens.editprofile.state.EditableField
import com.example.rybackiapp.presentation.screens.editprofile.state.InterestGroupUI
import com.example.rybackiapp.presentation.screens.editprofile.state.InterestItemUI
import com.example.rybackiapp.presentation.screens.editprofile.state._EditProfile
import com.example.rybackiapp.presentation.screens.editprofile.state._EditProfileState
import com.example.rybackiapp.utils.TagNameAlreadyExistsException
import com.example.rybackiapp.utils.Validator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val editProfileUseCase: EditProfileUseCase,
    private val loadInterestsUseCase: LoadInterestsUseCase


) : ViewModel() {
    private val _state = MutableStateFlow(EditProfileUIState())
    val state: StateFlow<EditProfileUIState> = _state

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            val interestsDeferred = async { loadInterestsUseCase() }
            val profileDeferred = async { getUserProfileUseCase() }

            val interestsGroup = interestsDeferred.await()
            val mainProfile = profileDeferred.await()

            _state.update { state ->
                val newInterestsGroups = interestsGroup.map { it?.toMap() }
                state.copy(
                    state = mainProfile.fold(
                        onSuccess = { _EditProfileState.Success },
                        onFailure = { _EditProfileState.Error(it.message ?: "Error") }),
                    interestsGroups = newInterestsGroups,
                    profile = if (mainProfile.isSuccess) {
                        mainProfile.getOrNull()?.toEditProfile(interestsGroup) ?: _EditProfile()
                    } else {
                        state.profile
                    }
                )
            }
        }
    }

    fun onEvent(editProfileEvent: EditProfileEvent) {
        when (editProfileEvent) {
            is EditProfileEvent.EditYearChanged -> {

                val currentState = _state.value
                val currentYear = currentState.profile.year

                _state.update {
                    it.copy(
                        profile = currentState.profile.copy(
                            year = currentYear.copy(
                                newValue = editProfileEvent.year
                            )
                        )
                    )
                }
            }

            is EditProfileEvent.EditNameChanged -> {
                val currentState = _state.value
                val currentName = currentState.profile.name


                _state.update {
                    it.copy(
                        profile = currentState.profile.copy(
                            name = currentName.copy(
                                newValue = editProfileEvent.newName
                            )
                        )
                    )
                }
            }

            EditProfileEvent.SaveBtnClicked -> {

                val currentState = _state.value
                val currentProfile = currentState.profile

                viewModelScope.launch {
                    _state.update { it.copy(state = _EditProfileState.Loading) }
                    delay(500)

                    with(currentProfile) {

                        val newNameValid = Validator.validateName(name.newValue)
                        val newTagNameValid = Validator.validateTagName(tagName.newValue)
                        val newYearValid = Validator.validateYear(year.newValue)

                        val isNewNameValid = newNameValid == null
                        val isNewTagNameValid = newTagNameValid == null
                        val isNewYearValid = newYearValid == null

                        if (!isNewNameValid) {
                            _state.update {
                                it.copy(
                                    profile = currentProfile.copy(
                                        name = name.copy(
                                            error = newNameValid,
                                            isError = true
                                        )
                                    ),
                                    state = _EditProfileState.Success
                                )
                            }
                            return@launch
                        }

                        if (!isNewTagNameValid) {
                            _state.update {
                                it.copy(
                                    profile = currentProfile.copy(
                                        tagName = tagName.copy(
                                            error = newTagNameValid,
                                            isError = true
                                        )
                                    ),

                                    state = _EditProfileState.Success
                                )
                            }
                            return@launch
                        }

                        if (!isNewYearValid) {
                            _state.update {
                                it.copy(
                                    profile = currentProfile.copy(
                                        year = year.copy(
                                            error = newYearValid,
                                            isError = true
                                        )
                                    ),
                                    state = _EditProfileState.Success
                                )
                            }
                            return@launch
                        }

                        editProfileUseCase(
                            id = id,
                            name = name.newValue,
                            year = year.newValue,
                            tagName = tagName.newValue,
                            interests = interests.map { it.toMap() }.toSet(),
                        ).onSuccess {
                            _state.update {
                                it.copy(
                                    state = _EditProfileState.Success,
                                    profile = currentProfile.copy(
                                        name = name.copy(currentValue = name.newValue, isError = false),
                                        year = year.copy(currentValue = year.newValue, isError = false),
                                        tagName = tagName.copy(currentValue = tagName.newValue, isError = false),
                                        interests = interests
                                    )
                                )
                            }
                        }.onFailure { error ->

                            when (error) {
                                is TagNameAlreadyExistsException -> {
                                    _state.update {
                                        it.copy(
                                            profile = currentProfile.copy(
                                                tagName = tagName.copy(
                                                    error = error.message,
                                                    isError = true
                                                )
                                            ),
                                            state = _EditProfileState.Success
                                        )
                                    }
                                }

                                else -> {
                                    _state.update {
                                        it.copy(
                                            state = _EditProfileState.Error(
                                                error.message ?: "Error"
                                            )
                                        )
                                    }
                                }
                            }


                        }
                    }
                }
            }

            is EditProfileEvent.EditInterestsChanged -> {

                val currentProfile = _state.value.profile
                val interests = _state.value.interestsGroups
                    .filterNotNull()
                    .flatMap { it.items }
                    .filter { it in editProfileEvent.interestItems }
                    .toSet()

                _state.update {
                    it.copy(
                        profile = currentProfile.copy(interests = interests)
                    )
                }
            }

            is EditProfileEvent.EditTagNameChanged -> {
                val currentState = _state.value
                val currentTagName = currentState.profile.tagName


                _state.update {
                    it.copy(
                        profile = currentState.profile.copy(
                            tagName = currentTagName.copy(
                                newValue = editProfileEvent.tagName
                            )
                        )
                    )
                }
            }
        }
    }

    fun MainProfile.toEditProfile(groups: List<InterestGroup?>): _EditProfile {

        return _EditProfile(
            id = id,
            name = EditableField(name.toString(), newValue = name.toString()),
            tagName = EditableField(tagName.toString(), newValue = tagName.toString()),
            year = EditableField(year.toString(), newValue = year.toString()),
            interests = groups
                .asSequence()
                .filterNotNull()
                .flatMap { it.items }
                .filter { interests?.toSet()?.contains(it.id) == true }
                .map { it.toMap() }
                .toSet()
        )
    }

    private fun InterestItem.toMap(): InterestItemUI {
        return InterestItemUI(
            id = id,
            name = name,
            nameEng = nameEng
        )
    }

    private fun InterestItemUI.toMap(): InterestItem {
        return InterestItem(
            id = id,
            name = name,
            nameEng = nameEng
        )
    }

    private fun InterestGroup.toMap(): InterestGroupUI {
        return InterestGroupUI(
            id = id,
            name = name,
            nameEng = nameEng,
            items = items.map { it.toMap() }
        )
    }

}