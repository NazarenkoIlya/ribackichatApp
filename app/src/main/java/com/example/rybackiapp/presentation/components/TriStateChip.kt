package com.example.rybackiapp.presentation.components

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.rybackiapp.presentation.screens.users.state.InterestState
import  androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.rybackiapp.R
import com.example.rybackiapp.presentation.screens.users.state.Interest
import com.example.rybackiapp.presentation.screens.users.state.InterestItemUI

@Composable
fun TriStateChip(
    interest: Interest = Interest(name = "Кошки"),
    //state: InterestState = InterestState.NEUTRAL,
    onStateChange: (Interest) -> Unit = {},
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
    // Цвета для разных состояний
    val (backgroundColor, contentColor, icon) = when (interest.interestState) {
        InterestState.POSITIVE -> Triple(
            Color(0xFF4CAF50).copy(alpha = 0.2f),
            Color(0xFF4CAF50),
            ImageVector.vectorResource(R.drawable.ic_like)
        )

        InterestState.NEGATIVE -> Triple(
            Color(0xFFF44336).copy(alpha = 0.2f),
            Color(0xFFF44336),
            ImageVector.vectorResource(R.drawable.ic_dislike)

        )

        InterestState.NEUTRAL -> Triple(
            Color(0xFFE0E0E0),                      // Серый фон
            Color(0xFF757575),                      // Серый текст
            null                                     // Без иконки
        )
    }

    val onClick = {
        val newState = when (interest.interestState) {
            InterestState.NEUTRAL -> InterestState.POSITIVE
            InterestState.POSITIVE -> InterestState.NEGATIVE
            InterestState.NEGATIVE -> InterestState.NEUTRAL
        }
        onStateChange(interest.copy(interestState = newState))
    }

    AssistChip(
        onClick = onClick,
        label = {
            Text(
                text = interest.name,
                color = contentColor
            )
        },
        leadingIcon = if (icon != null) {
            {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = contentColor
                )
            }
        } else null,
        modifier = modifier,
        colors = AssistChipDefaults.assistChipColors(
            containerColor = backgroundColor
        ),
        border = if (interest.interestState == InterestState.NEUTRAL) {
            BorderStroke(1.dp, Color.Gray.copy(alpha = 0.5f))
        } else null
    )
}

@Preview(showBackground = true)
@Composable
fun TriStateChipView() {
    TriStateChip()
}