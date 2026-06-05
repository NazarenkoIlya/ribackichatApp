package com.example.rybackiapp.presentation.screens.chatlist.old_version.state

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


sealed class ChatListState {
    object Loading : ChatListState()
    object Success : ChatListState()
    data class Error(val message: String) : ChatListState()
}


data class ChatPreview(
    val name: String = "Default",
    val imageUrl: String? = null,
    val status: Boolean = false
)

data class ChatUi(
    val chatId: String = "",
    val isOwn: Boolean = false,
    val participants: List<String> = emptyList(),
    val lastMessage: String = "",
    val lastTimestamp: Long = 0,
    val unreadMessage: Int = 0
) {
    val formattedTime: String
        get() = SimpleDateFormat("HH:mm", Locale.getDefault())
            .format(Date(lastTimestamp))

    val formattedDate: String
        get() = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
            .format(Date(lastTimestamp))
}

data class ChatWithUserName(
    val chatPreview: ChatPreview = ChatPreview(),
    val chatUI: ChatUi = ChatUi()

)

data class ChatListUIState(
    val state: ChatListState = ChatListState.Loading,
    val chatList: List<ChatWithUserName> = emptyList()
)

sealed class ChatListEvent{
    data class DeleteChat(val chatId: String): ChatListEvent()
}

