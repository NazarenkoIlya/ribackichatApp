package com.example.rybackiapp.presentation.screens.chatlist.old_version


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rybackiapp.domain.model.PrivateChat
import com.example.rybackiapp.domain.model.PrivateUserChatPreview
import com.example.rybackiapp.domain.usecase.DeleteChatUseCase
import com.example.rybackiapp.domain.usecase.ObserveUserChatsUseCase
import com.example.rybackiapp.domain.usecase.ObserveUnreadMessagesUseCase
import com.example.rybackiapp.domain.usecase.ObserveUserChatPreview
import com.example.rybackiapp.domain.usecase.ObserveUserIdUseCase
import com.example.rybackiapp.domain.usecase.ObserveUserOnlineUseCase
import com.example.rybackiapp.presentation.screens.chatlist.old_version.state.ChatListEvent

import com.example.rybackiapp.presentation.screens.chatlist.old_version.state.ChatListState
import com.example.rybackiapp.presentation.screens.chatlist.old_version.state.ChatListUIState
import com.example.rybackiapp.presentation.screens.chatlist.old_version.state.ChatPreview
import com.example.rybackiapp.presentation.screens.chatlist.old_version.state.ChatUi
import com.example.rybackiapp.presentation.screens.chatlist.old_version.state.ChatWithUserName
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flattenMerge
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatListViewModel @Inject constructor(
    private val observeUserChatsUseCase: ObserveUserChatsUseCase,
    private val observeUnreadMessagesUseCase: ObserveUnreadMessagesUseCase,
    private val observeUserIdUseCase: ObserveUserIdUseCase,
    private val observeUserChatPreview: ObserveUserChatPreview,
    private val deleteChatUseCase: DeleteChatUseCase,
    private val observeUserOnlineUseCase: ObserveUserOnlineUseCase

) : ViewModel() {


    @OptIn(ExperimentalCoroutinesApi::class)
    val chatListState: StateFlow<ChatListUIState> =
        observeUserIdUseCase()
            .flatMapLatest { uid ->
                observeUserChatsUseCase(uid)
                    .flatMapLatest { chats ->
                        if (chats.isEmpty()) {
                            flowOf(
                                ChatListUIState(
                                    chatList = emptyList(),
                                    state = ChatListState.Success
                                )
                            )
                        } else {
                            // Создаем Flow для каждого чата
                            val chatFlows = chats.map { chat ->
                                chat as PrivateChat
                                combine(
                                    observeUnreadMessagesUseCase(chat.chatId, uid),
                                    getChatPreviewFlow(chat, uid),
                                    getOnlineStatusFlow(chat, uid)
                                ) { unreadCount, preview, isOnline ->

                                    val chatUi = ChatUi(
                                        chatId = chat.chatId,
                                        isOwn = chat.lastSenderId == uid,
                                        participants = chat.participants,
                                        lastMessage = chat.lastMessage ?: "",
                                        lastTimestamp = chat.lastTimestamp ?: 0,
                                        unreadMessage = unreadCount
                                    )

                                    ChatWithUserName(
                                        chatPreview = preview.toMap().copy(isOnline = isOnline),
                                        chatUI = chatUi
                                    )
                                }
                            }


                            combine<ChatWithUserName, List<ChatWithUserName>>(chatFlows) { chatList ->
                                chatList.toList()
                            }.map { chatUis ->
                                ChatListUIState(
                                    chatList = chatUis,
                                    state = ChatListState.Success
                                )
                            }
                        }
                    }
            }
            .catch { exception ->
                emit(
                    ChatListUIState(
                        state = ChatListState.Error(exception.message ?: "Unknown error")
                    )
                )
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = ChatListUIState()
            )

    // Вспомогательные функции
    private fun getChatPreviewFlow(chat: PrivateChat, uid: String): Flow<PrivateUserChatPreview> {
        return flow {
            val otherUserId = chat.chatId.split("_")
                .let { ids ->
                    if (ids.distinct().size == 1) ids.first()
                    else ids.first { it != uid }
                }

            emitAll(observeUserChatPreview.invoke(otherUserId))
        }
    }

    private fun getOnlineStatusFlow(chat: PrivateChat, uid: String): Flow<Boolean> {
        return flow {
            val otherUserId = chat.chatId.split("_")
                .let { ids ->
                    if (ids.distinct().size == 1) ids.first()
                    else ids.first { it != uid }
                }

            emitAll(observeUserOnlineUseCase(otherUserId))
        }
    }

//    @OptIn(ExperimentalCoroutinesApi::class)
//    val chatListState: StateFlow<ChatListUIState> =
//        observeUserIdUseCase()
//            .flatMapLatest { uid ->
//                observeUserChatsUseCase(uid)
//                    .map { chats ->
//                        val chatFlows = chats.map { chat ->
//                            observeUnreadMessagesUseCase(chat.chatId, uid)
//                                .map { unreadCount ->
//
//                                    chat as PrivateChat
//
//                                    val chat = ChatUi(
//                                        chatId = chat.chatId,
//                                        isOwn = chat.lastSenderId == uid,
//                                        participants = chat.participants,
//                                        lastMessage = chat.lastMessage ?: "",
//                                        lastTimestamp = chat.lastTimestamp ?: 0,
//                                        unreadMessage = unreadCount
//                                    )
//                                    // val preview =
//                                    observeUserChatPreview.invoke(
//                                        chat.chatId.split("_")
//                                            .let { ids ->
//                                                if (ids.distinct().size == 1) ids.first()
//                                                else ids.first { it != uid }
//                                            }
//                                    ).collect { preview ->
//                                        observeUserOnlineUseCase(preview.uid).collect {
//                                            ChatWithUserName(
//                                                chatPreview = preview.toMap(),
//                                                chatUI = chat
//                                            )
//                                        }
//                                    }
//
//
//
//
//
//                                }
//                        }
//                        if (chatFlows.isEmpty()) {
//                            flowOf(emptyList<ChatWithUserName>())
//
//                        } else {
//                            combine(chatFlows) { chatUis ->
//                                chatUis.toList()
//                            }
//                        }
//                    }
//                    .flattenMerge()
//                    .map { chatUis ->
//
//                        ChatListUIState(
//                            chatList = chatUis,
//                            state = ChatListState.Success
//                        )
//                    }
//            }
//            .catch { exception ->
//                emit(
//                    ChatListUIState(
//                        state = ChatListState.Error(exception.message ?: "Unknown error")
//                    )
//                )
//            }
//            .stateIn(
//                scope = viewModelScope,
//                started = SharingStarted.WhileSubscribed(5000),
//                initialValue = ChatListUIState()
//            )


    fun onEvent(chatListEvent: ChatListEvent) {
        when (chatListEvent) {
            is ChatListEvent.DeleteChat -> {
                viewModelScope.launch(Dispatchers.IO) {
                    deleteChatUseCase(chatListEvent.chatId)
                }
            }
        }
    }


    fun PrivateUserChatPreview.toMap(): ChatPreview {
        return ChatPreview(
            name = name,
            imageUrl = mainPhotoUrl,
            isOnline = status
        )
    }
}

