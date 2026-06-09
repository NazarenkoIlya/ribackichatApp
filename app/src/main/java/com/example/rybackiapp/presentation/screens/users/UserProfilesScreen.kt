package com.example.rybackiapp.presentation.screens.users

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.rybackiapp.presentation.components.UserCard
import com.example.rybackiapp.presentation.screens.users.state.UserProfilesState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.DpSize
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.rybackiapp.R
import com.example.rybackiapp.presentation.components.TooltipBoxComponent
import com.example.rybackiapp.presentation.components.TriStateChip
import com.example.rybackiapp.presentation.screens.users.state.FilterUI
import com.example.rybackiapp.presentation.screens.users.state.Interest
import com.example.rybackiapp.presentation.screens.users.state.InterestState
import com.example.rybackiapp.presentation.screens.users.state.UserProfilesUIState
import com.example.rybackiapp.presentation.screens.users.state.UsersProfileEvent
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserProfilesScreen(
    onNavigateToUserProfile: (String) -> Unit,
    onNavigateToChatDetail: (String) -> Unit
) {
    val viewModel: UserProfilesViewModel = hiltViewModel()
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadData()
    }



    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Users",
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
            Box {
                Image(
                    painter = painterResource(R.drawable.ic_background),
                    modifier = Modifier.fillMaxSize(),
                    contentDescription = "background",
                    contentScale = ContentScale.Crop
                )
                UserProfilesView(
                    state = state,
                    onNavigateToUserProfile = onNavigateToUserProfile,
                    onNavigateToChatDetail = onNavigateToChatDetail,
                    onEvent = viewModel::onEvent,
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }
    )


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserProfilesView(
    state: UserProfilesUIState = UserProfilesUIState(),
    onNavigateToUserProfile: (String) -> Unit = {},
    onNavigateToChatDetail: (String) -> Unit = {},
    onEvent: (UsersProfileEvent) -> Unit = {},
    modifier: Modifier = Modifier
) {

    var showBottomSheet by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
        ) {
            Box(
                contentAlignment = Alignment.BottomStart,

                ) {
                OutlinedTextField(
                    value = state.search,
                    onValueChange = { onEvent(UsersProfileEvent.SearchTextFieldChanged(it)) },
                    modifier = Modifier
                        // .fillMaxWidth()
                        .background(
                            Color.White,
                            shape = RoundedCornerShape(16.dp)
                        ),
                    shape = RoundedCornerShape(16.dp),
                    placeholder = {
                        Text(
                            "Search",
                        )
                    },
                )
                IconButton(
                    onClick = {
                        onEvent(UsersProfileEvent.SearchClicked)
                    },
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(5.dp)
                ) {
                    Icon(
                        ImageVector.vectorResource(R.drawable.ic_search),
                        contentDescription = "send message",
                    )
                }

            }
            TooltipBoxComponent(
                text = "Фильтр",
                content = {
                    IconButton(
                        onClick = {
                            showBottomSheet = true
                        }
                    ) {
                        Icon(
                            ImageVector.vectorResource(R.drawable.ic_filter),
                            contentDescription = "user filter",
                        )
                    }
                }
            )
            TooltipBoxComponent(
                text = "Сброс фильтра",
                content = {
                    IconButton(
                        onClick = {
                            onEvent(UsersProfileEvent.OnResetFilter)
                        }
                    ) {
                        Icon(
                            ImageVector.vectorResource(R.drawable.ic_reset),
                            contentDescription = "reset filter",
                        )
                    }
                }
            )
        }



        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            when (state.state) {
                is UserProfilesState.Error -> {
                    Text(
                        state.state.message,
                        color = MaterialTheme.colorScheme.error
                    )
                }

                UserProfilesState.Loading -> CircularProgressIndicator()
                is UserProfilesState.Success -> {

                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(state.profileList.profiles) { profile ->
                            Log.d("AAAAAA", "UserProfilesView: ${profile.id}")
                            UserCard(
                                userid = profile.id,
                                chatId = profile.chatId,
                                isOnline = profile.isOnline,
                                userName = profile.name ?: "",
                                userYear = profile.year ?: "",
                                onNavigateToUserProfile = onNavigateToUserProfile,
                                onNavigateToChatDetail = onNavigateToChatDetail,
                                isYou = profile.isYou,
                                imageUrl = profile.mainPhotoUrl
                            )
                        }

                    }
                }

            }
        }
    }


    if (showBottomSheet) {
        //var tempAgeRangeInterests by rememberSaveable { mutableStateOf(state.filter.ageRange) }
        var maxAge by rememberSaveable { mutableStateOf(state.filterUI.maxAge) }
        var minAge by rememberSaveable { mutableStateOf(state.filterUI.minAge) }

        var tempUnwantedInterests by rememberSaveable { mutableStateOf(state.filterUI.unwantedInterests) }
        var tempDesirableInterests by rememberSaveable { mutableStateOf(state.filterUI.desirableInterests) }

        val startInteractionSource = remember { MutableInteractionSource() }
        val endInteractionSource = remember { MutableInteractionSource() }
        val sheetState = rememberModalBottomSheetState()
        val scope = rememberCoroutineScope()
        ModalBottomSheet(
            modifier = Modifier.fillMaxSize(),
            onDismissRequest = { showBottomSheet = false },
            sheetState = sheetState,
            contentColor = MaterialTheme.colorScheme.surface
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            ) {
                Text(
                    text = "Выберите Фильтры",
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                RangeSlider(
                    value = minAge..maxAge,
                    onValueChange = {
                        minAge = it.start
                        maxAge = it.endInclusive

                        //tempAgeRangeInterests = it
                    },
                    valueRange = 14f..100f,
                    startInteractionSource = startInteractionSource,
                    endInteractionSource = endInteractionSource,
                    steps = 86,
                    startThumb = {
                        SliderDefaults.Thumb(
                            interactionSource = startInteractionSource,
                            colors = SliderDefaults.colors(thumbColor = Color.Blue),
                            thumbSize = DpSize(24.dp, 24.dp),

                            )
                    },
                    endThumb = {
                        SliderDefaults.Thumb(
                            interactionSource = endInteractionSource,
                            colors = SliderDefaults.colors(thumbColor = Color.Blue),
                            thumbSize = DpSize(24.dp, 24.dp),

                            )
                    },
                    colors = SliderDefaults.colors(
                        activeTrackColor = Color.Blue,
                        inactiveTrackColor = Color.LightGray,
                        //thumbColor = Color.Yellow,
                        activeTickColor = Color.Blue,
                        inactiveTickColor = Color.LightGray,
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Selected age: ${(minAge).toInt()} - ${(maxAge).toInt()}",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.primary
                )


                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.heightIn(max = 500.dp)
                ) {
                    items(state.interestsGroups) { group ->
                        Column {
                            group?.let {
                                Text(
                                    text = it.name,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = Color.DarkGray,
                                    modifier = Modifier.padding(bottom = 8.dp)
                                )

                                FlowRow(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalArrangement = Arrangement.spacedBy(8.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {

                                    it.items.forEach { interest ->


                                        val state = when (interest.id) {
                                            in tempDesirableInterests -> InterestState.POSITIVE
                                            in tempUnwantedInterests -> InterestState.NEGATIVE
                                            else -> InterestState.NEUTRAL
                                        }

                                        TriStateChip(
                                            interest = Interest(
                                                id = interest.id,
                                                name = interest.name,
                                                interestState = state
                                            ),
                                            onStateChange = { interest ->

                                                when (interest.interestState) {
                                                    InterestState.NEUTRAL -> {
                                                        tempUnwantedInterests -= interest.id
                                                        tempDesirableInterests -= interest.id
                                                    }

                                                    InterestState.POSITIVE -> {
                                                        tempUnwantedInterests -= interest.id
                                                        tempDesirableInterests += interest.id
                                                    }

                                                    InterestState.NEGATIVE -> {
                                                        tempUnwantedInterests += interest.id
                                                        tempDesirableInterests -= interest.id
                                                    }
                                                }
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = { showBottomSheet = false },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Отмена")
                    }
                }
                Button(
                    onClick = {

                        onEvent(
                            UsersProfileEvent.SaveFiler(
                                FilterUI(
                                    minAge = minAge,
                                    maxAge = maxAge,
                                    unwantedInterests = tempUnwantedInterests,
                                    desirableInterests = tempDesirableInterests
                                )
                            )
                        )
                        scope.launch {
                            sheetState.hide()
                        }.invokeOnCompletion {
                            if (!sheetState.isVisible) showBottomSheet = false
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Выбрать")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun UserProfilesPreview() {
    UserProfilesView()
}