package com.example.rybackiapp.presentation.screens.editprofile


import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.rybackiapp.R
import com.example.rybackiapp.presentation.screens.editprofile.state.EditProfileEvent
import com.example.rybackiapp.presentation.screens.editprofile.state.EditProfileEvent.EditYearChanged
import com.example.rybackiapp.presentation.screens.editprofile.state.EditProfileEvent.EditNameChanged
import com.example.rybackiapp.presentation.screens.editprofile.state.EditProfileEvent.SaveBtnClicked
import com.example.rybackiapp.presentation.screens.editprofile.state.EditProfileUIState
import com.example.rybackiapp.presentation.screens.editprofile.state._EditProfileState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    onBackClick: () -> Unit,
) {
    val viewModel: EditProfileViewModel = hiltViewModel()
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
                        text = "User profile",
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
                EditProfileView(
                    state = state,
                    onEvent = viewModel::onEvent,
                    modifier = Modifier.padding(innerPadding),
                )
            }


        }
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileView(
    state: EditProfileUIState = EditProfileUIState(state = _EditProfileState.Loading),
    onEvent: (EditProfileEvent) -> Unit = {},
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier,
) {

    var showBottomSheet by rememberSaveable { mutableStateOf(false) }
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        when (state.state) {
            is _EditProfileState.Error -> Text(
                state.state.message,
                color = MaterialTheme.colorScheme.error
            )

            is _EditProfileState.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier.size(50.dp)
                )
            }

            is _EditProfileState.Success -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Transparent),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    OutlinedTextField(
                        value = state.profile.name.newValue,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 4.dp),
                        // textStyle = TextStyle(fontSize = 10.sp),
                        shape = RoundedCornerShape(16.dp),
                        onValueChange = { onEvent(EditNameChanged(it)) },
                        placeholder = { Text("Name") },
                        isError = state.profile.name.isError,
                        supportingText = {
                            if (state.profile.name.isError) {
                                state.profile.name.error?.let { Text(it) }
                            }
                        }
                    )

                    OutlinedTextField(
                        value = state.profile.tagName.newValue,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 4.dp),
                        shape = RoundedCornerShape(16.dp),
                        onValueChange = { onEvent(EditProfileEvent.EditTagNameChanged(it)) },
                        placeholder = { Text("TagName") },
                        isError = state.profile.tagName.isError,
                        supportingText = {
                            if (state.profile.tagName.isError) {
                                state.profile.tagName.error?.let { Text(it) }
                            }
                        }
                    )

                    OutlinedTextField(
                        value = state.profile.year.newValue,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 4.dp),
                        shape = RoundedCornerShape(16.dp),
                        onValueChange = { onEvent(EditYearChanged(it)) },
                        placeholder = { Text("Year") },
                        isError = state.profile.year.isError,
                        supportingText = {
                            if (state.profile.year.isError) {
                                state.profile.year.error?.let { Text(it) }
                            }
                        }
                    )


                    val displayText = if (state.profile.interests.isEmpty()) {
                        "Нажмите, чтобы выбрать интересы"
                    } else {
                        "Интересы: ${state.profile.interests.joinToString(", ") { it.name }}"
                    }

                    Card(
                        shape = RoundedCornerShape(12.dp),
                        //elevation = CardDefaults.cardElevation(4.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.Transparent,
                            contentColor = Color.Black
                        ),
                    ) {
                        Text(
                            text = displayText,
                            fontSize = 18.sp,
                            modifier = Modifier
                                .padding(horizontal = 24.dp, vertical = 16.dp)
                                .clickable { showBottomSheet = true },
                            color = Color.Black
                        )
                    }

                    Spacer(modifier = Modifier.height(60.dp))

                    Button(
                        onClick = { onEvent(SaveBtnClicked) },

                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent,
                            contentColor = Color.Black
                        ),

                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        border = BorderStroke(3.dp, Color.DarkGray)
                    ) { Text("Save", modifier = Modifier.padding(12.dp), fontSize = 28.sp) }
                }
            }

            _EditProfileState.Saved -> TODO()
        }
    }

    if (showBottomSheet) {
        var tempSelectedInterests by rememberSaveable { mutableStateOf(state.profile.interests) }
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
                    text = "Выберите интересы",
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 16.dp)
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
                                        val isSelected = interest in tempSelectedInterests
                                        FilterChip(
                                            selected = isSelected,
                                            onClick = {
                                                tempSelectedInterests = if (isSelected) {
                                                    tempSelectedInterests - interest
                                                } else {
                                                    tempSelectedInterests + interest
                                                }
                                            },
                                            label = { Text(interest.name) },
                                            colors = FilterChipDefaults.filterChipColors(
                                                selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                                                selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                                            )
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
                        Text("Cancel")
                    }

                }
                Button(

                    onClick = {
                        onEvent(EditProfileEvent.EditInterestsChanged(tempSelectedInterests))

                        scope.launch { sheetState.hide() }.invokeOnCompletion {
                            if (!sheetState.isVisible) showBottomSheet = false
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Select")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EditProfilePreview() {
    EditProfileView()
}