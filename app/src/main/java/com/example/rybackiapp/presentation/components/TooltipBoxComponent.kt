package com.example.rybackiapp.presentation.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.example.rybackiapp.R
import com.example.rybackiapp.presentation.screens.users.state.UsersProfileEvent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TooltipBoxComponent(
    text: String,
    content: @Composable (() -> Unit),
    modifier: Modifier = Modifier
) {
    val tooltipState = rememberTooltipState()
    val tooltipPosition = TooltipDefaults.rememberTooltipPositionProvider()
    TooltipBox(
        positionProvider = tooltipPosition,
        tooltip = {
            PlainTooltip {
                Text(text)
            }
        },
        state = tooltipState,
        modifier = modifier
    ) {
        content()
    }
}