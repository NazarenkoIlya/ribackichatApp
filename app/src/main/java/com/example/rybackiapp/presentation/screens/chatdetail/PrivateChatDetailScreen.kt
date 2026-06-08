package com.example.rybackiapp.presentation.screens.chatdetail


import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.rybackiapp.R
import com.example.rybackiapp.presentation.components.ListMessageComponentView
import com.example.rybackiapp.presentation.components.MessageCard
import com.example.rybackiapp.presentation.components.MessageSenderComponent
import com.example.rybackiapp.presentation.components.PrivateChatPreviewComponent
import com.example.rybackiapp.presentation.screens.chatdetail.state.ChatDetailEvent
import com.example.rybackiapp.presentation.screens.chatdetail.state.ChatDetailEvent.LoadChatDetail
import com.example.rybackiapp.presentation.screens.chatdetail.state.ChatDetailEvent.SendMessageBtnClicked
import com.example.rybackiapp.presentation.screens.chatdetail.state.PrivateChatDetailsState
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import kotlin.Unit


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatDetailScreen(
    chatId: String,
    onBackClick: () -> Unit,
) {

    Log.d("NNNNN", "invoke: ${chatId}")

    val background = painterResource(id = R.drawable.ic_background)
    var isFirstLoad by remember { mutableStateOf(true) }

    val listState = rememberLazyListState()
    val viewModel: PrivateChatDetailViewModel = hiltViewModel()
    val context = LocalContext.current


    val chatDetailState by viewModel.state.collectAsState()
    LaunchedEffect(chatId) {
        if (chatId.isNotBlank()) viewModel.onEvent(LoadChatDetail(chatId))
    }

    LaunchedEffect(Unit) {
        viewModel.errorMessage.collect {
            Toast.makeText(context, it, Toast.LENGTH_LONG)
        }
    }


    LaunchedEffect(chatDetailState.messages) {
        if (isFirstLoad && chatDetailState.messages.isNotEmpty()) {
            listState.scrollToItem(chatDetailState.messages.size - 1) // без анимации для первого раза
            isFirstLoad = false
        }
    }

    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            .distinctUntilChanged()
            .collect { lastVisibleIndex ->

                if (lastVisibleIndex != null &&
                    lastVisibleIndex >= chatDetailState.messages.size - 1 &&
                    chatDetailState.messages.isNotEmpty()
                ) {
                    viewModel.resetUnread(chatId)
                }
            }
    }





    DisposableEffect(Unit) {
        onDispose {
            if (!chatDetailState.sendMessage.isBlank() && !chatDetailState.isEditing) {
                viewModel.onEvent(ChatDetailEvent.SaveMessageDraft)
                Toast.makeText(context, "Draft Saved", Toast.LENGTH_SHORT).show()
            }
        }
    }


    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            ImageVector.vectorResource(R.drawable.ic_back),
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                },
                title = {
                    PrivateChatPreviewComponent(chatDetailState.preview)
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    scrolledContainerColor = MaterialTheme.colorScheme.primaryContainer
                ),
                scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
            )
        },
        modifier = Modifier.background(Color.Transparent),
        content = { innerPadding ->

            Box {
                Image(
                    modifier = Modifier.fillMaxSize(),
                    painter = background,
                    contentDescription = "background ",
                    contentScale = ContentScale.Crop
                )
                ChatDetailScreenView(
                    chatDetailState = chatDetailState,
                    listState = listState,
                    modifier = Modifier.padding(innerPadding),
                    onEvent = viewModel::onEvent
                )
            }
        }
    )


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatDetailScreenView(
    listState: LazyListState = rememberLazyListState(),
    chatDetailState: PrivateChatDetailsState = PrivateChatDetailsState(),
    modifier: Modifier = Modifier,
    onEvent: (ChatDetailEvent) -> Unit = {},
) {

    //var text by remember { mutableStateOf("") }
    val send = painterResource(id = R.drawable.ic_send)
    val messages = chatDetailState.messages
    var showBottomSheet by rememberSaveable { mutableStateOf(false) }
    Column(
        modifier = modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        LazyColumn(
            state = listState,
            modifier = Modifier.weight(1f)
        ) {
            items(
                items = messages,
                key = { message -> message.messageUI.messageId }
            )
            { message ->
                with(message) {

                    MessageCard(
                        isOwn = messageUI.isOwner,
                        message = messageUI.text,
                        userName = userPreview.name,
                        imageUrl = userPreview.imageUrl,
                        date = message.messageUI.formattedDate,
                        time = message.messageUI.formattedTime,
                        deleteMessage = {
                            onEvent(ChatDetailEvent.DeleteMessage(messageUI.messageId))
                        },
                        editMessage = {
                            onEvent(ChatDetailEvent.MessageEditing(messageUI.messageId, true))
                        },
                        isEdit = messageUI.isEdit,
                        messageTextSize = chatDetailState.textSize.messageTextSize
                    )
                }
            }
        }

        MessageSenderComponent(
            sendMessage = chatDetailState.sendMessage,
            messagePreview = chatDetailState.messagePreview,
            isEditMessage = chatDetailState.isEditing,
            sendMessageClick = { onEvent(SendMessageBtnClicked) },
            closeClick = { onEvent(ChatDetailEvent.CloseEditing(isEditing = false)) },
            createListMessageClick = { showBottomSheet = true },
            messageChanged = { onEvent(ChatDetailEvent.OnMessageChanged(it)) },
            sendEditMessageClick = {
                onEvent(
                    ChatDetailEvent.SendEditMessageBtnClicked(
                        chatDetailState.editMessageId!!
                    )
                )
            }
        )


        if (showBottomSheet) {
            val sheetState = rememberModalBottomSheetState()
            val scope = rememberCoroutineScope()
            ModalBottomSheet(
                modifier = Modifier.fillMaxSize(),
                onDismissRequest = { showBottomSheet = false },
                sheetState = sheetState,
                contentColor = MaterialTheme.colorScheme.surface
            ) {
                ListMessageComponentView(
                    message = chatDetailState.sendMessage,
                    exitBottomSheet = { showBottomSheet = false },
                    applyListMessage = { message ->
                        onEvent(ChatDetailEvent.OnMessageChanged(message))
                    },
                    applyBottomSheet = {
                        scope.launch { sheetState.hide() }.invokeOnCompletion {
                            if (!sheetState.isVisible) showBottomSheet = false
                        }
                    }
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun SignInScreenPreview() {
    ChatDetailScreenView()
}