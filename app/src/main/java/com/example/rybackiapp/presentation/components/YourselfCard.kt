package com.example.rybackiapp.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.Year


@Composable
fun YourselfCard(
    email: String = "mary@mail.ru",
    aboutMe: String = "im big and fat cow",
    year: String = "2000",
    nameUser: String = "#mary_cow"
) {
    Card(
        modifier = Modifier
            .padding(horizontal = 2.dp, vertical = 5.dp)
            .background(color = Color.Transparent)
            .fillMaxWidth(),

        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp,
            pressedElevation = 8.dp
        ),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, Color.LightGray),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent // устанавливаем прозрачный фон Card
        )
    ) {
        Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 5.dp)) {

            if (email.isNotEmpty()) {
                YourselfItem(label = "Email", value = email)
            }
            if (aboutMe.isNotEmpty()) {
                YourselfItem(label = "Interests", value = aboutMe)
            }
            if (year.isNotEmpty()) {
                YourselfItem(label = "Year", value = year)
            }
            if (nameUser.isNotEmpty()) {
                YourselfItem(label = "TagName", value = nameUser)
            }
        }
    }
}

@Composable
fun YourselfItem(
    value: String = "im big and fat cow",
    label: String = "About me"
) {
    Column(
        //horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround
    ) {
        Text(value, fontSize = 15.sp, color = Color.White)
        Text(label, fontSize = 14.sp, color = Color.Gray)
        Spacer(modifier = Modifier.padding(2.dp))
    }
}

@Composable
@Preview(showBackground = true)
fun YourselfCardView() {
    YourselfCard()
}