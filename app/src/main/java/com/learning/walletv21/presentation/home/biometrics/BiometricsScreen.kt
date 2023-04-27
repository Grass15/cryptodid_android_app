package com.learning.walletv21.presentation.home.biometrics

import android.app.Activity
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LifecycleOwner

@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun BiometricPromptScreen() {
    val context = LocalContext.current
    val activity = LocalContext.current as Activity
    val biometricAuthenticator = remember { BiometricAuthenticator(context,{ Log.d("frfr","f") }) }
    var showPrompt by remember { mutableStateOf(false) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Button(
            onClick = { showPrompt = true },
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            Text(text = "Authenticate with Biometric")
        }
    }

    if (showPrompt) {
        LaunchedEffect(true) {
            biometricAuthenticator.authenticate(activity)
            showPrompt = false
        }
    }
}

/*
@Composable
fun BiometricScreen(onClick: () -> Unit) {

    val username = remember {mutableStateOf(TextFieldValue())}
    val password = remember {mutableStateOf(TextFieldValue())}
    val checked = remember {mutableStateOf(false)}

    Column(verticalArrangement = Arrangement.Center, modifier = Modifier.padding(20.dp)) {
        OutlinedTextField(
            value = username.value,
            onValueChange = {
                username.value = it
            },
            leadingIcon = {Icon(Icons.Filled.Person, contentDescription = "person")},
            label = { Text(text = "Username")}
        )
        OutlinedTextField(
            value = password.value,
            onValueChange = {
                password.value = it
            },
            leadingIcon = {Icon(Icons.Filled.Edit, contentDescription = "person")},
            label = { Text(text = "password")}
        )

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Row{
                Text(text = "Enable Biometric Auth")
                Switch(checked = checked.value, onCheckedChange = {
                    checked.value = it

                    if (checked.value){
                        onClick()
                    }
                })
            }

            Button(onClick){
                Text(text = "Login")
            }
        }
    }

}


 */