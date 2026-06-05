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


//    val database = Firebase.database
//    val myRef = database.reference
//    myRef.child("BBBBB").child("CCCCC")
//                      .setValue("AAAAAAA")


}

@Composable
@Preview(showBackground = true)
fun GreetingPreview() {
    RybackiAppTheme {
        MainContent()
    }
}

//@Composable
//fun RegistrationScreen(
//    modifier: Modifier = Modifier
//) {
//    var email by remember { mutableStateOf("") }
//    var password by remember { mutableStateOf("") }
//    var message by remember { mutableStateOf("") }
//    var messages by remember { mutableStateOf("") }
//
//    val database = Firebase.database
//    val myRef = database.getReference("message")
//    val auth = Firebase.auth
//
//
//    LaunchedEffect(Unit) {
//        myRef.addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                Log.d("AAAA", "onDataChange: ${snapshot.value}}")
//                auth.currentUser?.let {
//                    messages = snapshot.children.joinToString("\n") { it ->
//                        val user = it.getValue(User::class.java)
//                        "${user?.name}: ${user?.text}"
//                    }
//                }
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//            }
//
//        })
//    }
//
//    Log.d("AAAA", "RegistrationScreen: ${auth.currentUser?.email}")
//    Column {
//        Text(
//            messages,
//            modifier = Modifier
//                .padding(5.dp)
//                .fillMaxHeight(0.2f)
//                .fillMaxWidth()
//                .border(width = 2.dp, color = Color.Black)
//                .padding(start = 10.dp)
//        )
//
//        TextField(
//            value = message,
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(18.dp),
//            textStyle = TextStyle(fontSize = 25.sp),
//            onValueChange = { newText -> message = newText },
//            placeholder = { Text("Message!") }
//        )
//        TextField(
//            value = email,
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(18.dp),
//            textStyle = TextStyle(fontSize = 25.sp),
//            onValueChange = { newText -> email = newText },
//            placeholder = { Text("Email!") }
//        )
//        TextField(
//            value = password,
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(18.dp),
//            textStyle = TextStyle(fontSize = 25.sp),
//            onValueChange = { newText -> password = newText },
//            placeholder = { Text("Password!") }
//        )
//        Button(
//            onClick = {
//                if (message.isNotEmpty())
//                    myRef.child(myRef.push().key ?: "")
//                        .setValue(User(auth.currentUser?.email ?: "Guest: ", message))
//                message = ""
//            },
//
//            shape = RoundedCornerShape(15.dp),
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(10.dp)
//        ) {
//            Text("Send Message", fontSize = 28.sp)
//        }
//        Button(
//            onClick = { signIn(auth, email, password) },
//
//            shape = RoundedCornerShape(15.dp),
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(10.dp)
//        ) {
//            Text("SignIN", fontSize = 28.sp)
//        }
//        Button(
//            onClick = {
//                signUp(auth, email, password)
//            },
//            shape = RoundedCornerShape(15.dp),
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(10.dp)
//        ) {
//            Text("SignUP", fontSize = 28.sp)
//        }
//
//        Button(
//            onClick = {
//                signOut(auth)
//            },
//            shape = RoundedCornerShape(15.dp),
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(10.dp)
//        ) {
//            Text("SignOut", fontSize = 28.sp)
//        }
//    }
//
//
//}
//
//private fun signUp(auth: FirebaseAuth, email: String, password: String) {
//    auth.createUserWithEmailAndPassword(email, password)
//        .addOnCompleteListener {
//            if (it.isSuccessful) {
//                Log.d("AAAA", "signUp: Sign Up successful")
//            } else {
//                Log.d("AAAA", "signUp: Sign Up failure")
//            }
//        }
//}
//
//private fun signIn(auth: FirebaseAuth, email: String, password: String) {
//    auth.signInWithEmailAndPassword(email, password)
//        .addOnCompleteListener {
//            if (it.isSuccessful) {
//                Log.d("AAAA", "signUp: Sign In successful")
//            } else {
//                Log.d("AAAA", "signUp: Sign In failure")
//            }
//        }
//}
//
//private fun signOut(auth: FirebaseAuth) {
//    auth.signOut()
//}
//
//@Composable
//fun LoginScreen(
//    modifier: Modifier = Modifier
//) {
//
//    var email by remember { mutableStateOf("") }
//    var password by remember { mutableStateOf("") }
//    var isPasswordVisible by remember { mutableStateOf(false) }
//
//}
//
//
//// Preview для Android Studio
//@Preview(showBackground = true)
//@Composable
//fun RegistrationScreenPreview() {
//    RybackiAppTheme {
//        RegistrationScreen()
//    }
//}

