package com.loginid.cryptodid.presentation.home.biometrics

import android.app.Activity
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun BiometricPromptScreen() {
    /*
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

     */
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