package com.example.rybackiapp.presentation.components

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.rybackiapp.R
import com.example.rybackiapp.presentation.navigation.Screen

@Composable
fun ChatCard(
    chatId: String = "",
    isOwn: Boolean = false,
    lastMessage: String = "No massage!!",
    userName: String = "Name",
    unreadMessages: Int = 2,
    imageUrl: String? = null,
    isOnline: Boolean = false,
    date: String = "2000-07-08",
    time: String = "12:10",
    defaultImage: Int = R.drawable.ic_user0,
    onNavigateToChatDetail: (String) -> Unit = {},
    deleteChat: () -> Unit = {},
) {
    var showDeleteDialog by remember { mutableStateOf(false) }
    val crossCircleIcon = painterResource(id = R.drawable.ic_cross_circle)
    Card(
        modifier = Modifier
            .padding(horizontal = 2.dp, vertical = 5.dp)
            .fillMaxWidth()
            .clickable {
                onNavigateToChatDetail(
                    Screen.ChatDetail.createRoute(chatId = chatId)
                )
            },
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp,
            pressedElevation = 8.dp
        ),
        shape = RoundedCornerShape(32.dp),
        border = BorderStroke(1.dp, Color.Gray)
    ) {

        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 2.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {


            val painter = rememberAsyncImagePainter(
                model = imageUrl,
                error = painterResource(id = defaultImage),  // При ошибке загрузки
                placeholder = painterResource(id = defaultImage), // Пока загружается
                fallback = painterResource(id = defaultImage) // Если ничего не работает
            )
            Box(modifier = Modifier.weight(0.15f)) {
                Image(
                    painter = painter,
                    modifier = Modifier
                        .background(Color.Blue, shape = RoundedCornerShape(18.dp))
                        .size(54.dp)
                        .clip(RoundedCornerShape(18.dp)),
                    contentDescription = "empty_userprofile_photo",
                    contentScale = ContentScale.Crop
                )
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(1.dp)
                        .size(16.dp)
                        .background(
                            color = if (isOnline) Color.Green else Color.Red,
                            shape = CircleShape
                        )
                )
            }


            Column(
                modifier = Modifier
                    .weight(0.75f)
                    .padding(horizontal = 15.dp, vertical = 2.dp),
                horizontalAlignment = Alignment.Start,
                //verticalArrangement = Arrangement.Center
            ) {


                Text(
                    userName,
                    fontSize = 20.sp,
                    fontStyle = FontStyle.Normal,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    "$date $time",
                    fontSize = 12.sp,
                    fontStyle = FontStyle.Normal,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
                Text(
                    (if (isOwn) "You: " else "") + lastMessage,
                    fontSize = 15.sp,
                    maxLines = 1,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
            }
            if (unreadMessages != 0)
                Text(
                    unreadMessages.toString(),
                    modifier = Modifier.weight(0.1f),
                    fontSize = 15.sp,
                    color = Color.Blue
                )
            TooltipBoxComponent(
                text = "Delete chat",
                content = {
                    IconButton(onClick = { showDeleteDialog = true }) {
                        Icon(
                            crossCircleIcon,
                            contentDescription = "Delete chat"
                        )
                    }
                }
            )
        }
    }


    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("delete the chat?") },
            text = { Text("Are you sure you want to delete this chat? This action cannot be undone.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        deleteChat()
                        showDeleteDialog = false
                    }
                ) {
                    Text("Delete", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
@Preview(showBackground = true)
fun ChatCardView() {
    ChatCard()
}