package com.example.rybackiapp.presentation.screens.users

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rybackiapp.domain.model.Filter
import com.example.rybackiapp.domain.model.InterestGroup
import com.example.rybackiapp.domain.model.InterestItem
import com.example.rybackiapp.domain.model.UserProfile
import com.example.rybackiapp.domain.model.UserProfileList
import com.example.rybackiapp.domain.usecase.GetFilterSaveUseCase
import com.example.rybackiapp.domain.usecase.GetUserProfilesListUseCase
import com.example.rybackiapp.domain.usecase.LoadInterestsUseCase
import com.example.rybackiapp.domain.usecase.ObserveUserIdUseCase
import com.example.rybackiapp.domain.usecase.ObserveUserOnlineUseCase
import com.example.rybackiapp.domain.usecase.SaveFilterUseCase
import com.example.rybackiapp.domain.usecase.SearchUserProfilesUseCase
import com.example.rybackiapp.domain.usecase.SearchWithTagUserProfileUseCase
import com.example.rybackiapp.presentation.screens.users.state.FilterUI
import com.example.rybackiapp.presentation.screens.users.state.InterestGroupUI
import com.example.rybackiapp.presentation.screens.users.state.InterestItemUI
import com.example.rybackiapp.presentation.screens.users.state.Profile
import com.example.rybackiapp.presentation.screens.users.state.ProfileList
import com.example.rybackiapp.presentation.screens.users.state.UserProfilesState
import com.example.rybackiapp.presentation.screens.users.state.UserProfilesUIState
import com.example.rybackiapp.presentation.screens.users.state.UsersProfileEvent
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
import kotlin.text.toInt

@HiltViewModel
class UserProfilesViewModel @Inject constructor(
    val getUserProfilesListUseCase: GetUserProfilesListUseCase,
    private val observeUserIdUseCase: ObserveUserIdUseCase,
    private val loadInterestsUseCase: LoadInterestsUseCase,
    private val getFilterSaveUseCase: GetFilterSaveUseCase,
    private val saveFilterUseCase: SaveFilterUseCase,
    private val searchUserProfilesUseCase: SearchUserProfilesUseCase,
    private val searchWithTagUserProfileUseCase: SearchWithTagUserProfileUseCase,
    private val observeUserOnlineUseCase: ObserveUserOnlineUseCase

) : ViewModel() {

    private val _state = MutableStateFlow<UserProfilesUIState>(UserProfilesUIState())
    val state: StateFlow<UserProfilesUIState> = _state

    init {
        observeOnlineUser()
    }

    private fun observeOnlineUser() {

        val currentProfiles = _state.value.profileList.profiles

        currentProfiles.forEach { profile ->
            observeUserOnlineUseCase(profile.id)
                .onEach { isOnline ->
                    _state.update { state ->
                        val updatedProfiles = state.profileList.profiles.map { p ->
                            if (p.id == profile.id) p.copy(isOnline = isOnline)
                            else p
                        }
                        state.copy(
                            profileList = state.profileList.copy(
                                profiles = updatedProfiles
                            )
                        )
                    }
                }
                .launchIn(viewModelScope)
        }

    }

    fun loadData() {
        viewModelScope.launch {
            val interests = loadInterestsUseCase()
            val newInterestsGroups = interests.map { it?.toMap() }

            val filter = getFilterSaveUseCase().first()


            val uid = observeUserIdUseCase.invoke().first()

            getUserProfilesListUseCase()
                .onSuccess { profiles ->

                    _state.update { it ->
                        it.copy(
                            state = UserProfilesState.Success,
                            profileList = profiles.toMap(uid),
                            interestsGroups = newInterestsGroups,
                            filterUI = filter.toMapFilterUI()
                        )
                    }
                    observeOnlineUser()
                }
                .onFailure { error ->

                    _state.update { it ->
                        it.copy(
                            state = UserProfilesState.Error(error.message ?: "Error")
                        )
                    }
                }
        }
    }

    private fun generatePrivateChatId(currentId: String, uid: String) =
        listOf(currentId, uid).sorted().joinToString("_")

    private fun UserProfileList.toMap(uid: String): ProfileList =
        ProfileList(profiles = profiles.map { it.toMap(uid) })

    private fun UserProfile.toMap(uid: String): Profile = Profile(
        id = id,
        name = name,
        year = year,
        isYou = id == uid,
        mainPhotoUrl = mainPhotoUrl,
        chatId = chatId.ifEmpty { generatePrivateChatId(uid, id) }
    )

    fun onEvent(usersProfileEvent: UsersProfileEvent) {

        when (usersProfileEvent) {
            is UsersProfileEvent.SaveFiler -> {
                viewModelScope.launch(Dispatchers.IO) {
                    saveFilterUseCase(usersProfileEvent.filterUI.toMapFilter())
                    _state.update {
                        it.copy(filterUI = usersProfileEvent.filterUI)
                    }
                }
            }

            UsersProfileEvent.OnResetFilter -> {
                val filter = FilterUI()
                viewModelScope.launch(Dispatchers.IO) {
                    saveFilterUseCase(filter.toMapFilter())
                    _state.update {
                        it.copy(filterUI = filter)
                    }
                }
            }

            is UsersProfileEvent.SearchTextFieldChanged -> {
                _state.update {
                    it.copy(search = usersProfileEvent.search)
                }
            }

            UsersProfileEvent.SearchClicked -> {

                viewModelScope.launch(Dispatchers.IO) {
                    val uid = observeUserIdUseCase.invoke().first()
                    val search = _state.value.search


                    val users = if (search.startsWith("@")) {
                        searchWithTagUserProfileUseCase(search).fold(
                            onSuccess = { it.toMap(uid) },
                            onFailure = { ProfileList() },
                        )
                    } else {
                        searchUserProfilesUseCase(
                            search,
                            _state.value.filterUI.toMapFilter()
                        ).fold(
                            onSuccess = { it.toMap(uid) },
                            onFailure = { ProfileList() },
                        )
                    }
                    _state.update { it ->
                        it.copy(
                            state = UserProfilesState.Success,
                            profileList = users,
                        )
                    }

                }

            }
        }
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

    private fun Filter.toMapFilterUI(): FilterUI {
        return FilterUI(
            minAge = minAge.toFloat(),
            maxAge = maxAge.toFloat(),
            desirableInterests = desirableInterests.map { it.toInt() }.toSet(),
            unwantedInterests = unwantedInterests.map { it.toInt() }.toSet()
        )
    }

    private fun FilterUI.toMapFilter(): Filter {
        return Filter(
            minAge = minAge.toInt(),
            maxAge = maxAge.toInt(),
            desirableInterests = desirableInterests.map { it.toString() }.toSet(),
            unwantedInterests = unwantedInterests.map { it.toString() }.toSet()
        )
    }
}