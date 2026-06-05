package com.example.rybackiapp.domain.model


sealed interface ChatPreview

data class PrivateUserChatPreview(
    val uid: String = "",
    val name: String = "",
    val status: Boolean = false,
    val mainPhotoUrl: String? = null,
) : ChatPreview


//data class PrivateChatWithUnreadCount(
//    val chat: PrivateChat = PrivateChat(),
//    val unreadCount: Int = 0
//)
//data class ChatWithPreview(
//    val chatPreview: ChatPreview? = null,
//    val chatWithUnreadCount: PrivateChatWithUnreadCount = PrivateChatWithUnreadCount()
//)