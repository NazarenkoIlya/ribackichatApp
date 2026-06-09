package com.example.rybackiapp.presentation.screens.account.old_version


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rybackiapp.domain.model.InterestGroup
import com.example.rybackiapp.domain.model.MainProfile
import com.example.rybackiapp.domain.usecase.EditMainImageUseCase
import com.example.rybackiapp.domain.usecase.GetPublicImageUrlUseCase
import com.example.rybackiapp.domain.usecase.GetUserProfileUseCase
import com.example.rybackiapp.domain.usecase.LoadInterestsUseCase
import com.example.rybackiapp.domain.usecase.ObserveUserOnlineUseCase
import com.example.rybackiapp.domain.usecase.ObserveUserProfileUseCase
import com.example.rybackiapp.domain.usecase.RemoveFcmTokenUseCase
import com.example.rybackiapp.domain.usecase.SignOutUseCase
import com.example.rybackiapp.domain.usecase.UploadUserAvatarUseCase
import com.example.rybackiapp.presentation.screens.account.old_version.state.AccountEvent
import com.example.rybackiapp.presentation.screens.account.old_version.state.AccountProfile
import com.example.rybackiapp.presentation.screens.account.old_version.state.AccountState.*
import com.example.rybackiapp.presentation.screens.account.old_version.state.AccountUIState
import com.example.rybackiapp.utils.core.UriToFileConverter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AccountViewModel @Inject constructor(
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val signOutUseCase: SignOutUseCase,
    private val removeFcmTokenUseCase: RemoveFcmTokenUseCase,
    private val uploadUserAvatarUseCase: UploadUserAvatarUseCase,
    private val getPublicImageUrlUseCase: GetPublicImageUrlUseCase,
    private val uriToFileConverter: UriToFileConverter,
    private val editMainImageUseCase: EditMainImageUseCase,
    private val observeUserProfileUseCase: ObserveUserProfileUseCase,
    private val loadInterestsUseCase: LoadInterestsUseCase,
    private val observeUserOnlineUseCase: ObserveUserOnlineUseCase

) : ViewModel() {
    private val _state = MutableStateFlow<AccountUIState>(AccountUIState())
    val state: StateFlow<AccountUIState> = _state


    fun loadProfile() {


        viewModelScope.launch {
            val interestsGroup = loadInterestsUseCase()
            observeUserProfileUseCase().collect {

                observeUserOnlineUseCase(it.id).collect { isOnline ->
                    _state.value = AccountUIState(
                        accountProfile = it.toMap(interestsGroup),
                        isOnline = isOnline
                    )
                }

            }
        }
    }

    fun onEvent(accountEvent: AccountEvent) {
        when (accountEvent) {
            AccountEvent.SignOutBtnClicked -> {
                viewModelScope.launch {
                    signOutUseCase().onSuccess {
                        _state.value = AccountUIState(SignOut)
                    }.onFailure {
                        _state.value = AccountUIState(
                            accountState = Error(it.message ?: "Error")
                        )
                    }

                }
            }

            is AccountEvent.OnImageSelected -> {

                viewModelScope.launch(Dispatchers.IO) {

                    try {

                        val file = uriToFileConverter.convertUriToFile(
                            uri = accountEvent.uri,
                            uid = _state.value.accountProfile.id,
                            context = accountEvent.context
                        )
                        file?.let {
                            uploadUserAvatarUseCase.invoke(
                                fileName = file.name,
                                uid = _state.value.accountProfile.id,
                                bytes = file.readBytes()
                            ).onSuccess {
                                editMainImageUseCase.invoke(_state.value.accountProfile.id, it)
                            }
                        }

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                }
            }
        }
    }


    private fun MainProfile.toMap(groups: List<InterestGroup?>): AccountProfile {
        return AccountProfile(
            id = id,
            name = name,
            email = email,
            year = year,
            tagName = tagName,
            mainPhotoUrl = mainPhotoUrl,
            photos = photos,
            interests = groups
                .asSequence()
                .filterNotNull()
                .flatMap { it.items }
                .filter { interests?.toSet()?.contains(it.id) == true }
                .joinToString(", ") { it.name },
            chatId = generatePrivateChatId(id, id)
        )
    }

    private fun generatePrivateChatId(currentId: String, uid: String) =
        listOf(currentId, uid).sorted().joinToString("_")
}

