package com.example.rybackiapp.presentation.screens.chatdetail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rybackiapp.domain.model.Message
import com.example.rybackiapp.domain.model.MessageDraft
import com.example.rybackiapp.domain.model.PrivateUserChatPreview
import com.example.rybackiapp.domain.model.UserPreview
import com.example.rybackiapp.domain.usecase.DeleteMessageDraftUseCase
import com.example.rybackiapp.domain.usecase.DeleteMessageUseCase
import com.example.rybackiapp.domain.usecase.EditMessageUseCase
import com.example.rybackiapp.domain.usecase.GetMessageDraftUseCase
import com.example.rybackiapp.domain.usecase.ObserveChatDetailUseCase
import com.example.rybackiapp.domain.usecase.ObserveFontSizeMassageUseCase
import com.example.rybackiapp.domain.usecase.ObserveUserChatPreview
import com.example.rybackiapp.domain.usecase.ObserveUserIdUseCase
import com.example.rybackiapp.domain.usecase.ObserveUserOnlineUseCase
import com.example.rybackiapp.domain.usecase.ObserveUserProfilePreview
import com.example.rybackiapp.domain.usecase.PushNotificationUseCase
import com.example.rybackiapp.domain.usecase.ResetUnreadCountUseCase
import com.example.rybackiapp.domain.usecase.SaveMessageDraftUseCase
import com.example.rybackiapp.domain.usecase.SendMessageInPrivateChatUseCase
import com.example.rybackiapp.domain.usecase.SetFontSizeMassageUseCase
import com.example.rybackiapp.presentation.screens.chatdetail.state.ChatDetailEvent
import com.example.rybackiapp.presentation.screens.chatdetail.state.ChatPreview
import com.example.rybackiapp.presentation.screens.chatdetail.state.MessageUI
import com.example.rybackiapp.presentation.screens.chatdetail.state.MessageWithUserName
import com.example.rybackiapp.presentation.screens.chatdetail.state.PrivateChatDetailsState
import com.example.rybackiapp.presentation.screens.chatdetail.state.TextSize
import com.example.rybackiapp.presentation.screens.chatdetail.state.UserPreviewUI
import com.example.rybackiapp.utils.core.MessageSender
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PrivateChatDetailViewModel @Inject constructor(
    private val sendMessageInPrivateChatUseCase: SendMessageInPrivateChatUseCase,
    private val observeChatDetailUseCase: ObserveChatDetailUseCase,
    private val resetUnreadCountUseCase: ResetUnreadCountUseCase,
    private val observeUserIdUseCase: ObserveUserIdUseCase,
    private val observeUserProfilePreview: ObserveUserProfilePreview,
    private val observeUserChatPreview: ObserveUserChatPreview,
    private val deleteMessageDraftUseCase: DeleteMessageDraftUseCase,
    private val getMessageDraftUseCase: GetMessageDraftUseCase,
    private val saveMessageDraftUseCase: SaveMessageDraftUseCase,
    private val deleteMessageUseCase: DeleteMessageUseCase,
    private val messageSender: MessageSender,
    private val editMessageUseCase: EditMessageUseCase,
    private val observeFontSizeMassageUseCase: ObserveFontSizeMassageUseCase,
    private val observeUserOnlineUseCase: ObserveUserOnlineUseCase,
    private val pushNotificationUseCase: PushNotificationUseCase
) : ViewModel() {


    private val _state = MutableStateFlow(PrivateChatDetailsState())
    val state: StateFlow<PrivateChatDetailsState> = _state


    val errorMessage = MutableSharedFlow<String?>()

    fun resetUnread(
        chatId: String,
    ) {
        viewModelScope.launch {
            resetUnreadCountUseCase.invoke(chatId)
        }
    }

    fun onEvent(chatDetailEvent: ChatDetailEvent) {
        when (chatDetailEvent) {
            is ChatDetailEvent.SendMessageBtnClicked -> {
                sendMessage(
                    text = _state.value.sendMessage,
                )
            }

            is ChatDetailEvent.LoadChatDetail -> {
                getMessageDraft(chatDetailEvent.chatId)
                observeChatDetail(chatDetailEvent.chatId)
            }

            is ChatDetailEvent.OnMessageChanged -> {
                _state.update {
                    it.copy(
                        sendMessage = chatDetailEvent.text
                    )
                }
            }

            ChatDetailEvent.SaveMessageDraft -> {
                viewModelScope.launch(Dispatchers.IO) {
                    saveMessageDraftUseCase.invoke(
                        MessageDraft(
                            chatId = _state.value.chatId,
                            text = _state.value.sendMessage
                        )
                    )
                }
            }

            is ChatDetailEvent.DeleteMessage -> {
                viewModelScope.launch(Dispatchers.IO) {

                    deleteMessageUseCase(
                        _state.value.chatId,
                        chatDetailEvent.messageId
                    ).onFailure {
                        errorMessage.emit(it.message)
                    }
                }
            }

            is ChatDetailEvent.MessageEditing -> {
                val message = _state.value.messages.firstOrNull { message ->
                    message.messageUI.messageId == chatDetailEvent.messageId
                }?.messageUI ?: MessageUI()

                _state.update { it ->
                    it.copy(
                        isEditing = chatDetailEvent.isEditing,
                        sendMessage = message.text,
                        messagePreview = message.text,
                        editMessageId = message.messageId
                    )
                }
            }

            is ChatDetailEvent.CloseEditing -> {
                _state.update { it ->
                    it.copy(
                        isEditing = chatDetailEvent.isEditing,
                        sendMessage = "",
                        editMessageId = null,
                        messagePreview = ""
                    )
                }
            }

            is ChatDetailEvent.SendEditMessageBtnClicked -> {
                sendEditMessage(
                    chatId = _state.value.chatId,
                    text = _state.value.sendMessage,
                    messageId = chatDetailEvent.messageId
                )
            }
        }
    }


    private fun getMessageDraft(chatId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            getMessageDraftUseCase.invoke(chatId)?.let { message ->
                _state.update {
                    it.copy(sendMessage = message.text)
                }
                deleteMessageDraftUseCase.invoke(chatId)
            }
        }
    }

//    @OptIn(ExperimentalCoroutinesApi::class)
//    private fun observeChatDetail(chatId: String) {
//
//        viewModelScope.launch {
//            combine(
//                observeChatDetailUseCase.invoke(chatId),
//                observeUserIdUseCase.invoke()
//            ) { messages, userId ->
//                messages to userId
//            }.collect { (messages, userId) ->
//
//
//                val textSize = observeFontSizeMassageUseCase().first()
//                val preview =
//                    observeUserChatPreview.invoke(
//                        chatId.split("_")
//                            .let { ids ->
//                                if (ids.distinct().size == 1) ids.first()
//                                else ids.first { it != userId }
//                            }
//                    ).first()
//                observeUserOnlineUseCase(preview.uid).collect {
//
//                }
//
//                _state.update { it ->
//                    it.copy(
//                        chatId = chatId,
//                        preview = preview.toMap(),
//                        messages = messages.map {
//                            val userPreview =
//                                observeUserProfilePreview.invoke(it.senderId).first().toMap()
//                            val message = it.toMap(userId)
//                            MessageWithUserName(
//                                userPreview = userPreview,
//                                messageUI = message
//                            )
//                        },
//                        textSize = TextSize(textSize)
//                    )
//                }
//            }
//        }
//    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun observeChatDetail(chatId: String) {
        viewModelScope.launch {
            combine(
                observeChatDetailUseCase.invoke(chatId),
                observeUserIdUseCase.invoke()
            ) { messages, userId ->
                messages to userId
            }.collect { (messages, userId) ->

                val textSize = observeFontSizeMassageUseCase().first()
                val otherUserId = chatId.split("_")
                    .let { ids ->
                        if (ids.distinct().size == 1) ids.first()
                        else ids.first { it != userId }
                    }

                val preview = observeUserChatPreview.invoke(otherUserId).first()

                val messagesWithUserNames = messages.map { message ->
                    val userPreview = observeUserProfilePreview.invoke(message.senderId).first().toMap()
                    val messageUi = message.toMap(userId)
                    MessageWithUserName(
                        userPreview = userPreview,
                        messageUI = messageUi
                    )
                }

                _state.update { it ->
                    it.copy(
                        chatId = chatId,
                        preview = preview.toMap(),
                        messages = messagesWithUserNames,
                        textSize = TextSize(textSize)
                    )
                }

                launch {
                    observeUserOnlineUseCase(otherUserId).collect { isOnline ->
                        _state.update { state ->
                            state.copy(
                                preview = state.preview.copy(isOnline = isOnline)
                            )
                        }
                    }
                }
            }
        }
    }

    private fun sendEditMessage(
        text: String,
        chatId: String,
        messageId: String,
    ) {
        if (chatId.isBlank() || text.isBlank()) return
        viewModelScope.launch(Dispatchers.IO) {

            try {
                editMessageUseCase(
                    chatId = chatId,
                    messageId = messageId,
                    text = text
                )
                _state.update {
                    it.copy(
                        isEditing = false,
                        sendMessage = "",
                    )
                }
            } catch (e: Exception) {
                messageSender.scheduleMessage(chatId = chatId, text = text)
                _state.update { it.copy(sendMessage = "") }
            }
        }
    }

    private fun sendMessage(
        text: String
    ) {
        val chatId = _state.value.chatId
        if (chatId.isBlank() || text.isBlank()) return

        viewModelScope.launch {
            try {
                sendMessageInPrivateChatUseCase(chatId = chatId, text = text)
                _state.update {
                    it.copy(
                        sendMessage = ""
                    )
                }
            } catch (e: Exception) {

                messageSender.scheduleMessage(chatId = chatId, text = text)
                _state.update { it.copy(sendMessage = "") }
            }
        }
    }

    fun Message.toMap(id: String): MessageUI {
        return MessageUI(
            messageId = messageId,
            senderId = senderId,
            isOwner = senderId == id,
            text = text,
            isEdit = isEdit,
            timestamp = timestamp
        )
    }

    fun PrivateUserChatPreview.toMap(): ChatPreview {
        return ChatPreview(
            name = name,
            imageUrl = mainPhotoUrl,
            isOnline = status
        )
    }

    fun UserPreview.toMap(): UserPreviewUI {
        return UserPreviewUI(
            uid = uid,
            name = name,
            imageUrl = mainPhotoUrl,
        )
    }
}