package com.loginid.cryptodid.presentation.authentication.register

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.loginid.cryptodid.R
import com.loginid.cryptodid.presentation.authentication.login.Status
import com.loginid.cryptodid.presentation.authentication.register.mock_template.MockRegisterScreenViewModel
import com.loginid.cryptodid.presentation.authentication.register.mock_template.RegisterScreenViewModelBase
import com.loginid.cryptodid.presentation.navigation.screens.AuthScreen
import com.loginid.cryptodid.presentation.theme.firstBackGroundColor
import com.loginid.cryptodid.presentation.theme.inputTextColor
import com.loginid.cryptodid.presentation.theme.secondBackGroundColor


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun RegisterScreen(
    navController: NavController,
    viewModel: RegisterScreenViewModelBase,
    currentState: RegisterDataState
) {

    val context = LocalContext.current
    //communicating with the viewModel
    // val currentState by viewModel.state.collectAsState()
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    val image = painterResource(id = R.drawable.img)

    val passwordVisibility = remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.firstBackGroundColor)
            .padding(1.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,

        ) {

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.firstBackGroundColor)
                .weight(2f)
                .padding(10.dp),
            contentAlignment = Alignment.Center

        ) {
            //The login Image goes here
            Image(painter = image, contentDescription = "Login id logo")

        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .weight(6f)
                .padding(2.dp),
            contentAlignment = Alignment.TopCenter

        ) {

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp))
                    .background(MaterialTheme.colors.secondBackGroundColor)
                    .padding(15.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    Text(
                        text = "Sign Up",
                        color = MaterialTheme.colors.primary,
                        fontStyle = MaterialTheme.typography.h3.fontStyle,
                        fontSize = MaterialTheme.typography.h3.fontSize,
                        fontWeight = FontWeight.Bold,
                    )
                    Spacer(modifier = Modifier.size(45.dp))
                    //TextFields

                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .onFocusChanged { focusState ->
                                if (!focusState.isFocused) {
                                    keyboardController?.hide()
                                    Log.d("focus", "lost focus")
                                }
                            },
                        label = {
                            Text(text = "First name")
                        },
                        textStyle = TextStyle(color = MaterialTheme.colors.inputTextColor),
                        leadingIcon = {
                            IconButton(
                                onClick = {}
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Person,
                                    contentDescription = "Person ICon"
                                )
                            }
                        },

                        value = currentState.firstname,
                        onValueChange = {
                            viewModel.onRegisterEvent(RegisterEvents.SetFirstName(it))
                        },
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Done,
                            keyboardType = KeyboardType.Email,
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = { keyboardController?.hide() },
                        ),

                        )

                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .onFocusChanged { focusState ->
                                if (!focusState.isFocused) {
                                    keyboardController?.hide()
                                    Log.d("focus", "lost focus")
                                }
                            },
                        label = {
                            Text(text = "Last name")
                        },
                        textStyle = TextStyle(color = MaterialTheme.colors.inputTextColor),
                        leadingIcon = {
                            IconButton(
                                onClick = {}
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Person,
                                    contentDescription = "Person ICon"
                                )
                            }
                        },

                        value = currentState.lastname,
                        onValueChange = {
                            viewModel.onRegisterEvent(RegisterEvents.SetLastName(it))
                        },
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Done,
                            keyboardType = KeyboardType.Email,
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = { keyboardController?.hide() },
                        ),

                        )

                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .onFocusChanged { focusState ->
                                if (!focusState.isFocused) {
                                    keyboardController?.hide()
                                    Log.d("focus", "lost focus")
                                }
                            },
                        label = {
                            Text(text = "username/email")
                        },
                        textStyle = TextStyle(color = MaterialTheme.colors.inputTextColor),
                        leadingIcon = {
                            IconButton(
                                onClick = {}
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Email,
                                    contentDescription = "Email ICon"
                                )
                            }
                        },

                        value = currentState.username,
                        onValueChange = {
                            viewModel.onRegisterEvent(RegisterEvents.SetEmailName(it))
                        },
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Done,
                            keyboardType = KeyboardType.Email,
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = { keyboardController?.hide() },
                        ),

                        )


                    //  Spacer(modifier = Modifier.size(17.dp))
                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        label = {
                            Text(text = "Password")
                        },
                        singleLine = true,
                        textStyle = TextStyle(color = MaterialTheme.colors.inputTextColor),

                        trailingIcon = {
                            IconButton(onClick = {passwordVisibility.value = !passwordVisibility.value}) {
                                val eyeIcon = if (passwordVisibility.value)
                                    Icons.Filled.Visibility
                                else  Icons.Filled.VisibilityOff
                                val desc = if(passwordVisibility.value) "hide password" else "show password"
                                Icon(imageVector = eyeIcon, contentDescription = desc)
                            }
                        },
                        visualTransformation = if(passwordVisibility.value) VisualTransformation.None else PasswordVisualTransformation(),
                        leadingIcon = {
                            IconButton(
                                onClick = {},
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Lock,
                                    contentDescription = "Password ICon",
                                )
                            }
                        },
                        value = currentState.password,
                        onValueChange = {
                            viewModel.onRegisterEvent(RegisterEvents.SetPassword(it))
                        },
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Done,
                            keyboardType = KeyboardType.Email,
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = { keyboardController?.hide() },
                        )

                    )

                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        label = {
                            Text(text = "RePassword")
                        },
                        singleLine = true,
                        textStyle = TextStyle(color = MaterialTheme.colors.inputTextColor),

                        trailingIcon = {
                            IconButton(onClick = {passwordVisibility.value = !passwordVisibility.value}) {
                                val eyeIcon = if (passwordVisibility.value)
                                    Icons.Filled.Visibility
                                else  Icons.Filled.VisibilityOff
                                val desc = if(passwordVisibility.value) "hide password" else "show password"
                                Icon(imageVector = eyeIcon, contentDescription = desc)
                            }
                        },
                        visualTransformation = if(passwordVisibility.value) VisualTransformation.None else PasswordVisualTransformation(),
                        leadingIcon = {
                            IconButton(
                                onClick = {},
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Lock,
                                    contentDescription = "Password ICon",
                                )
                            }
                        },
                        value = currentState.repassword,
                        onValueChange = {
                            viewModel.onRegisterEvent(RegisterEvents.SetRePassword(it))
                        },
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Done,
                            keyboardType = KeyboardType.Email,
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = { keyboardController?.hide() },
                        )

                    )


                    //  Spacer(modifier = Modifier.size(17.dp))
                    Button(
                        onClick = {
                            //viewModel.onRegisterEvent(RegisterEvents.DemoRegister)
                            viewModel.onRegisterEvent(RegisterEvents.Register)
                        },
                        colors  = ButtonDefaults.buttonColors(
                            contentColor = Color.White
                        ),

                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = "Login")

                    }
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                navController.navigate(AuthScreen.Login.route)
                            }, contentAlignment = Alignment.Center

                    ) {
                        Text(
                            text = "Log In to your account",
                            color = MaterialTheme.colors.primary, textDecoration = TextDecoration.Underline,
                        )
                    }

                }
            } //lazy end
        }
    }


    when (currentState.status) {
        Status.ERROR -> {
            Log.d("Auth", "errror")
            Log.d("Auth", currentState.userId + " | " + currentState.repassword)
            Toast.makeText(context,"Oops something went wrong", Toast.LENGTH_SHORT).show()
            viewModel.resetStatus()
        }
        Status.SUCCESS -> {
            Log.d("Auth", "Success")
            viewModel.resetStatus()
            navController.navigate(route = AuthScreen.Login.route)
        }
        Status.FAILLED -> {
            Log.d("Auth", "Failled")

            Toast.makeText(context,"Error while trying to save",Toast.LENGTH_SHORT).show()
            viewModel.resetStatus()
        }
        Status.LOADING -> {
            Log.d("Auth", "Loading")
        }
        Status.NO_ACTION -> {
            Log.d("Auth", "NO action")
        }
    }

}

@Preview(showBackground = true)
@Composable
fun PreviewRegisterScreen(){
    RegisterScreen(navController = rememberNavController(), currentState = RegisterDataState(), viewModel = MockRegisterScreenViewModel())
}