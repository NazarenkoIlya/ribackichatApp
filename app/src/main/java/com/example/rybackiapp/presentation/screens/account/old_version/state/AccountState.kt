package com.example.rybackiapp.presentation.screens.account.old_version.state

import android.content.Context
import android.net.Uri
import java.io.File

sealed class AccountState {
    object Loading : AccountState()
    object Success : AccountState()
    data class Error(val message: String) : AccountState()
    object SignOut : AccountState()
}


data class AccountProfile(
    val id: String = "",
    val chatId: String = "",
    val name: String? = null,
    val email: String? = null,
    val year: String? = null,
    val tagName: String? = null,
    val interests: String? = null,
    val mainPhotoUrl: String? = null,
    val photos: Map<String, String>? = null
)

data class AccountUIState(
    val accountState: AccountState = AccountState.Success,
    val accountProfile: AccountProfile = AccountProfile(),
    val isOnline: Boolean = false
)


sealed class AccountEvent {
    object SignOutBtnClicked : AccountEvent()
    data class OnImageSelected(val uri: Uri, val context: Context) : AccountEvent()

}