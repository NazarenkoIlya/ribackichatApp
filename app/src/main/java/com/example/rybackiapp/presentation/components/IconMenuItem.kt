package com.example.rybackiapp.presentation.components

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.rybackiapp.R


@Composable
fun IconMenuItem(
    icon: Int = R.drawable.ic_chat11,
    isSelected: Boolean = false,
    text: String = "",
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {

    val color = if (isSelected) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
    }
    Box(
        contentAlignment = Alignment.Center,
        //  modifier = Modifier.background(Color.Blue)
    ) {
        Image(
            painter = painterResource(id = icon),
            contentDescription = text,
            modifier = Modifier.size(52.dp),
            colorFilter = ColorFilter.tint(color)
        )

        //Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = text,
            fontSize = 12.sp,
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.align(Alignment.BottomEnd)
        )
    }
}

@Composable
@Preview(showBackground = true)
fun IconMenuItemView() {
    IconMenuItem()
}