package com.example.rybackiapp.presentation.screens.chatdetail.state


import com.example.rybackiapp.R
import com.example.rybackiapp.domain.model.MessageDraft
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


data class MessageUI(
    val messageId: String = "",
    val senderId: String = "",
    val isOwner: Boolean = false,
    val isEdit: Boolean? = null,
    val text: String = "",
    val timestamp: Long = 0
) {
    val formattedTime: String
        get() = SimpleDateFormat("HH:mm", Locale.getDefault())
            .format(Date(timestamp))

    val formattedDate: String
        get() = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
            .format(Date(timestamp))
}

data class ChatPreview(
    val name: String = "Default",
    val imageUrl: String? = null,
    val imageDefault: Int = R.drawable.ic_user0,
    val status: Boolean = false
)


data class UserPreviewUI(
    val uid: String = "",
    val name: String = "Default",
    val imageUrl: String? = null,
)

data class MessageWithUserName(
    val userPreview: UserPreviewUI = UserPreviewUI(),
    val messageUI: MessageUI = MessageUI()

)

data class PrivateChatDetailsState(
    val chatId: String = "",
    val preview: ChatPreview = ChatPreview(),
    val messages: List<MessageWithUserName> = emptyList(),
    val editMessageId: String? = null,
    val messagePreview: String = "",
    val isEditing: Boolean = false,
    val sendMessage: String = "",
)


sealed class ChatDetailEvent {
    object SendMessageBtnClicked : ChatDetailEvent()
    object SaveMessageDraft : ChatDetailEvent()
    data class LoadChatDetail(val chatId: String) : ChatDetailEvent()
    data class OnMessageChanged(val text: String) : ChatDetailEvent()
    data class DeleteMessage(val messageId: String) : ChatDetailEvent()
    data class MessageEditing(val messageId: String, val isEditing: Boolean) : ChatDetailEvent()
    data class CloseEditing(val isEditing: Boolean) : ChatDetailEvent()
    data class SendEditMessageBtnClicked(val messageId: String) : ChatDetailEvent()
}
