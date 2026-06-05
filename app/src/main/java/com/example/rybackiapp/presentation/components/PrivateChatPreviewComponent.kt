package com.example.rybackiapp.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.rybackiapp.R
import com.example.rybackiapp.presentation.navigation.Screen
import com.example.rybackiapp.presentation.screens.chatdetail.state.ChatPreview


@Composable
fun PrivateChatPreviewComponent(
    chatPreview: ChatPreview = ChatPreview()
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 8.dp)
    ) {

        Row {
            val painter = rememberAsyncImagePainter(
                model = chatPreview.imageUrl,
                error = painterResource(id = chatPreview.imageDefault),  // При ошибке загрузки
                placeholder = painterResource(id = chatPreview.imageDefault), // Пока загружается
                fallback = painterResource(id = chatPreview.imageDefault) // Если ничего не работает
            )

            Image(
                painter = painter,
                modifier = Modifier
                    .background(Color.Blue, shape = RoundedCornerShape(18.dp))
                    .size(50.dp)
                    .clip(RoundedCornerShape(18.dp)),
                alignment = Alignment.TopCenter,
                contentDescription = "empty_userprofile_photo",
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier
                    .weight(0.75f)
                    .padding(horizontal = 15.dp, vertical = 2.dp),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center
            ) {
                Text(chatPreview.name, fontSize = 16.sp, fontStyle = FontStyle.Normal)
                Text(
                    when (chatPreview.status) {
                        true -> "Online"
                        false -> "Offline"
                    }, fontSize = 12.sp
                )
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun PrivateChatPreviewComponentView() {
    PrivateChatPreviewComponent()
}