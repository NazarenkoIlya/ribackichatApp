package com.example.rybackiapp.presentation.components

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.rybackiapp.R


@Composable
fun MessageCard(
    isOwn: Boolean = true,
    message: String = "No massage!!",
    messageTextSize: Int = 12,
    isEdit: Boolean? = null,
    userName: String = "Name",
    isOnline: Boolean = false,
    date: String = "2000-07-08",
    time: String = "12:10",
    imageUrl: String? = null,
    deleteMessage: () -> Unit = {},
    editMessage: () -> Unit = {},
    defaultImage: Int = R.drawable.ic_user0,
) {
    var showMenu by remember { mutableStateOf(false) }
    val context = LocalContext.current
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 8.dp),
        contentAlignment = if (isOwn) {
            Alignment.CenterEnd
        } else {
            Alignment.CenterStart
        }
    ) {
        Card(
            modifier = Modifier
                .padding(2.dp)
                .fillMaxWidth(0.8f)
                .background(
                    color = Color.White,
                    shape = RoundedCornerShape(32.dp)
                )
                .combinedClickable(
                    onClick = {},
                    onLongClick = {
                        if (isOwn) showMenu = true
                    }
                ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 2.dp,
                pressedElevation = 8.dp
            ),
            shape = RoundedCornerShape(32.dp),
            border = BorderStroke(1.dp, if (isOwn) Color.Blue else Color.Gray)
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp, horizontal = 16.dp),
                verticalAlignment = Alignment.Top
            ) {

                val painter = rememberAsyncImagePainter(
                    model = imageUrl,
                    error = painterResource(id = defaultImage),
                    placeholder = painterResource(id = defaultImage),
                    fallback = painterResource(id = defaultImage)
                )

                Box{
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
                }



                Spacer(modifier = Modifier.width(15.dp))

                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.Start
                ) {
                    Row {
                        Text(
                            userName,
                            fontSize = 16.sp,
                            fontStyle = FontStyle.Normal,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        if (isEdit == true) {
                            Text(
                                " (edit)",
                                fontSize = 12.sp,
                                fontStyle = FontStyle.Normal,
                                color = Color.Blue,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                    Text(
                        "$date $time",
                        fontSize = 12.sp,
                        fontStyle = FontStyle.Normal,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    SelectionContainer {
                        Text(
                            message,
                            fontSize = messageTextSize.sp,
                            maxLines = Int.MAX_VALUE,
                            overflow = TextOverflow.Visible
                        )
                    }
                }
            }




            DropdownMenu(
                expanded = showMenu,
                onDismissRequest = { showMenu = false },
                modifier = Modifier
                    .background(Color.White, RoundedCornerShape(8.dp))
            ) {
                DropdownMenuItem(
                    text = { Text("Редактировать") },
                    onClick = {
                        editMessage.invoke()
                        Toast.makeText(context, "Edit message", Toast.LENGTH_LONG).show()
                        showMenu = false
                    }
                )
                DropdownMenuItem(
                    text = { Text("Удалить") },
                    onClick = {
                        deleteMessage.invoke()
                        Toast.makeText(context, "Delete message", Toast.LENGTH_LONG).show()
                        showMenu = false
                    }
                )
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun MessageCardView() {
    MessageCard()
}