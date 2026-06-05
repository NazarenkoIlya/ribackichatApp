package com.example.rybackiapp.presentation.screens.chatlist.old_version

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.rybackiapp.R
import com.example.rybackiapp.domain.model.PrivateChat
import com.example.rybackiapp.presentation.components.ChatCard
import com.example.rybackiapp.presentation.screens.chatlist.old_version.state.ChatListEvent
import com.example.rybackiapp.presentation.screens.chatlist.old_version.state.ChatListState
import com.example.rybackiapp.presentation.screens.chatlist.old_version.state.ChatListUIState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatListScreen(
    navigateTo: (String) -> Unit
) {
    val viewModel: ChatListViewModel = hiltViewModel()
    val state by viewModel.chatListState.collectAsState(ChatListUIState())

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Chats",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                },
                actions = {
//                    IconButton(onClick = { navigateTo(Screen.EditProfile) }) {
//                        Icon(Icons.Default.Edit, contentDescription = "Edit")
//                    }
//                    IconButton(onClick = { /* Действие 2 */ }) {
//                        Icon(Icons.Default.MoreVert, contentDescription = "Еще")
//                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    scrolledContainerColor = MaterialTheme.colorScheme.primaryContainer
                ),
                scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
            )
        },
        content = { innerPadding ->

            Box {
                Image(
                    painter = painterResource(R.drawable.ic_background),
                    modifier = Modifier.fillMaxSize(),
                    contentDescription = "background",
                    contentScale = ContentScale.Crop // Добавьте это для правильного масштаба
                )
                ChatListView(
                    state = state,
                    navigateTo = navigateTo,
                    onEvent = viewModel::onEvent,
                    modifier = Modifier.padding(innerPadding)
                )
            }

        }
    )

}

@Composable
fun ChatListView(
    state: ChatListUIState = ChatListUIState(),
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier,
    onEvent: (ChatListEvent) -> Unit = {},
    navigateTo: (String) -> Unit = {}
) {

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp)
            .background(Color.Transparent)
    ) {

        when (state.state) {

            is ChatListState.Error -> Text(
                state.state.message,
                color = MaterialTheme.colorScheme.error
            )

            ChatListState.Loading -> CircularProgressIndicator(
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(50.dp)
            )

            is ChatListState.Success -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .align(Alignment.TopCenter)
                ) {
                    items(state.chatList) { chatUi ->

                        ChatCard(
                            chatId = chatUi.chatUI.chatId,
                            isOwn = chatUi.chatUI.isOwn,
                            lastMessage = chatUi.chatUI.lastMessage,
                            unreadMessages = chatUi.chatUI.unreadMessage,
                            userName = chatUi.chatPreview.name,
                            imageUrl = chatUi.chatPreview.imageUrl,
                            onNavigateToChatDetail = navigateTo,
                            date = chatUi.chatUI.formattedDate,
                            time = chatUi.chatUI.formattedTime,
                            deleteChat = {
                                onEvent(ChatListEvent.DeleteChat(chatUi.chatUI.chatId))
                            }
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ChatListPreview() {
    ChatListView()
}