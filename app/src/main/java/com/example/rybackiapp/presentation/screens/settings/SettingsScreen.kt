package com.example.rybackiapp.presentation.screens.settings

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.rybackiapp.R
import com.example.rybackiapp.presentation.screens.settings.state.SettingEvent
import com.example.rybackiapp.presentation.screens.settings.state.SettingState
import com.example.rybackiapp.presentation.screens.settings.state.SettingUIState
import com.example.rybackiapp.presentation.screens.settings.state.Theme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onBackClick: () -> Unit,
) {
    val viewModel: SettingsViewModel = hiltViewModel()
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            ImageVector.vectorResource(R.drawable.ic_back),
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                },
                title = {
                    Text(
                        text = "Settings",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    scrolledContainerColor = MaterialTheme.colorScheme.primaryContainer
                ),
                scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
            )
        },


        content = { innerPadding ->

            Box(
                modifier = Modifier
                    .fillMaxSize(),
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_background),
                    modifier = Modifier.fillMaxSize(),
                    contentDescription = "background",
                    contentScale = ContentScale.Crop
                )
                SettingsView(
                    state = state,
                    viewModel::onEvent,
                    modifier = Modifier.padding(innerPadding),
                )
            }
        }
    )
}

@Composable
fun SettingsView(
    state: SettingUIState = SettingUIState(state = SettingState.Success),
    onEvent: (SettingEvent) -> Unit = {},
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {

    Column(modifier = modifier.fillMaxSize().padding(16.dp)) {

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Notification")
            Spacer(modifier = Modifier.weight(1f))
            Switch(
                checked = state.notificationEnable,
                onCheckedChange = {
                    onEvent(SettingEvent.NotificationEnableChanged(it))
                }
            )
        }
        Spacer(modifier = Modifier.height(12.dp))

        Text("Theme")
        Row {
            Theme.entries.forEach { themeOption ->
                FilterChip(
                    selected = state.theme.theme == themeOption.theme,
                    onClick = {
                        onEvent(SettingEvent.ThemeChanged(themeOption.theme))
                    },
                    label = {
                        Text(themeOption.theme)
                    },
                    modifier = Modifier.padding(end = 8.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        Text("Text size: ${state.fontSize} sp")
        Slider(
            value = state.fontSize.toFloat(),
            onValueChange = {
                onEvent(SettingEvent.FontSizeMessageChanged(it.toInt()))
            },
            valueRange = 12f..24f,
            steps = 6
        )
    }

}

@Preview(showBackground = true)
@Composable
fun SettingsViewPreview() {
    SettingsView()
}