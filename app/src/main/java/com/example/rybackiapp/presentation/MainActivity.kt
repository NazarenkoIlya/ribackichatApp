package com.example.rybackiapp.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.rybackiapp.R
import com.example.rybackiapp.presentation.navigation.AppNavigation
import com.example.rybackiapp.presentation.navigation.AppStartViewModel
import com.example.rybackiapp.presentation.ui.theme.RybackiAppTheme
import com.google.firebase.database.database
import com.google.firebase.Firebase
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RybackiAppTheme {
                MainContent()
            }
        }
    }
}

@Composable
fun MainContent(
    modifier: Modifier = Modifier
) {

    val viewModel: AppStartViewModel = hiltViewModel()
    val startDestination by viewModel.startDestination.collectAsState()
    val theme by viewModel.theme.collectAsState()
    RybackiAppTheme(theme = theme){
        Box(
            modifier = modifier
                .fillMaxSize()
        ) {

            AppNavigation(
                navHostController = rememberNavController(),
                startDestination = startDestination,
                modifier = modifier
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun GreetingPreview() {
    RybackiAppTheme {
        MainContent()
    }
}

