package com.example.rybackiapp.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.rybackiapp.R


@Composable
fun MessageSenderComponent(
    sendMessage: String = "Жил старик со своею старухой" +
            " У самого синего моря;" +
            " Они жили в ветхой землянке" +
            " Ровно тридцать лет и три года." +
            " Старик ловил неводом рыбу," +
            " Старуха пряла свою пряжу.",
    messagePreview: String = "",
    isEditMessage: Boolean = false,
    sendMessageClick: () -> Unit = {},
    sendEditMessageClick: () -> Unit = {},
    closeClick: () -> Unit = {},
    createListMessageClick: () -> Unit = {},
    messageChanged: (String) -> Unit = {},
) {
    val send = painterResource(id = R.drawable.ic_send)
    val editSend = painterResource(id = R.drawable.ic_edit_send)
    val list = painterResource(id = R.drawable.ic_list_circle)
    val close = painterResource(id = R.drawable.ic_close)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
    ) {

        if (isEditMessage) {
            Box {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .offset(y = 12.dp)
                        .border(
                            color = Color.Gray,
                            width = 1.dp,
                            shape = RectangleShape
                        )
                ) {
                    Text(
                        text = "Edit message",
                        modifier = Modifier.padding(top = 4.dp, start = 5.dp),
                        fontSize = 12.sp,
                        color = Color.Blue
                    )

                    Text(
                        text = messagePreview,
                        modifier = Modifier.padding(bottom = 16.dp, top = 4.dp, start = 5.dp),
                        softWrap = false
                    )
                }
                IconButton(
                    onClick = { closeClick() },
                    modifier = Modifier.align(Alignment.CenterEnd)
                ) {
                    Icon(close, contentDescription = "close")
                }
            }

        }

        OutlinedTextField(
            value = sendMessage,
            onValueChange = { messageChanged(it) },
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Color.White,
                    shape = RoundedCornerShape(16.dp)
                ),
            shape = RoundedCornerShape(16.dp),
            placeholder = {
                Text("Message")
            },
            trailingIcon = {
                Row {
                    TooltipBoxComponent(
                        text = "Add List Message",
                        content = {
                            IconButton(onClick = { createListMessageClick() }) {
                                Icon(list, contentDescription = "Add List Message")
                            }
                        }
                    )
                    if (isEditMessage) {
                        TooltipBoxComponent(
                            text = "Send Edit Message",
                            content = {
                                IconButton(onClick = { sendEditMessageClick() }) {
                                    Icon(editSend, contentDescription = "send edit message")
                                }
                            }
                        )
                    } else {
                        TooltipBoxComponent(
                            text = "Send Message",
                            content = {
                                IconButton(onClick = { sendMessageClick() }) {
                                    Icon(send, contentDescription = "send message")
                                }
                            }
                        )
                    }

                }
            }
        )
    }
}

@Composable
@Preview(showBackground = true)
fun MessageComponentView() {
    MessageSenderComponent()
}