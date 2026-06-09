package com.example.rybackiapp.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.rybackiapp.R
import com.example.rybackiapp.presentation.navigation.Screen


@Composable
fun UserCard(
    userid: String = "",
    chatId: String = "",
    userName: String = "Name",
    userYear: String = "2000",
    isYou: Boolean = true,
    isOnline: Boolean = false,
    onNavigateToUserProfile: (String) -> Unit = {},
    onNavigateToChatDetail: (String) -> Unit = {},
    imageUrl: String? = null,
    defaultImage: Int = R.drawable.ic_user0,
) {
    Card(
        modifier = Modifier
            .padding(2.dp)
            .fillMaxWidth()
            .height(100.dp)
            .clickable {

                if (!isYou) {
                    onNavigateToUserProfile(
                        Screen.UserProfile.createRoute(userid, chatId)
                    )
                } else {
                    onNavigateToUserProfile(
                        Screen.Account.route
                    )
                }


            },
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp,
            pressedElevation = 8.dp
        ),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(5.dp, Color.Gray)
    ) {

        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {


            val painter = rememberAsyncImagePainter(
                model = imageUrl,
                error = painterResource(id = defaultImage),
                placeholder = painterResource(id = defaultImage),
                fallback = painterResource(id = defaultImage)
            )

            Box(modifier = Modifier.weight(0.35f)){
                Image(
                    painter = painter,
                    modifier = Modifier
                        .background(Color.Blue, shape = RoundedCornerShape(18.dp))
                        .fillMaxHeight()
                        .fillMaxWidth()
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
                    .padding(horizontal = 8.dp)
                    .weight(0.65f),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {

                Text(userName, fontSize = 18.sp, lineHeight = 18.sp)
                Text(userYear, fontSize = 12.sp, lineHeight = 12.sp)
                if (isYou) Text(
                    "It's you",
                    fontSize = 10.sp,
                    lineHeight = 10.sp,
                    color = Color.Blue
                )
                TextButton(
                    {
                        onNavigateToChatDetail(
                            Screen.ChatDetail.createRoute(
                                chatId = chatId
                            )
                        )
                    },
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text("Write a message", fontSize = 12.sp)
                }
            }

        }
    }
}

@Preview(showBackground = true)
@Composable
fun UserCardView() {
    UserCard()
}