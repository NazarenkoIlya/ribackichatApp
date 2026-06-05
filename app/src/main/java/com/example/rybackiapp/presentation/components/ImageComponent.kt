package com.example.rybackiapp.presentation.components

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.rybackiapp.R


@Composable
fun ImageComponent(
    imageUrl: String? = null,
    defaultImage: Int = R.drawable.ic_user0,

    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {

    val painter = rememberAsyncImagePainter(
        model = imageUrl,
        error = painterResource(id = defaultImage),  // При ошибке загрузки
        placeholder = painterResource(id = defaultImage), // Пока загружается
        fallback = painterResource(id = defaultImage) // Если ничего не работает
    )

    Image(
        painter = painter,
        contentDescription = "Выбранное изображение",
        modifier = modifier
            .padding(16.dp)
            .fillMaxWidth()
            .size(300.dp)
            .background(Color.Blue, shape = RoundedCornerShape(18.dp))
            .clip(RoundedCornerShape(18.dp)),
        contentScale = ContentScale.Crop

    )
}

@Composable
@Preview(showBackground = true)
fun ImageComponentView() {
    ImageComponent()
}