package com.loginid.cryptodid.presentation.issuer

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.loginid.cryptodid.R
import com.loginid.cryptodid.data.local.entity.VCType
import com.loginid.cryptodid.presentation.MainActivity
import com.loginid.cryptodid.presentation.MainActivity.Companion.getFilesFolder
import com.loginid.cryptodid.presentation.home.vc.VCViewModel.VCEnteryState
import com.loginid.cryptodid.presentation.home.vc.VCViewModel.VCViewModel
import com.loginid.cryptodid.presentation.theme.firstBackGroundColor
import com.loginid.cryptodid.presentation.theme.inputTextColor
import com.loginid.cryptodid.presentation.theme.secondBackGroundColor
import java.util.*


external fun encryptSin(n1: Int, filepath: String?, attribute: String?): Int

@Composable
fun InsuranceNumberScreen(navController: NavController) {

    var firstName: String = "Fabien"
    val lastName: String = "KORGO"
    val city: String = "KENITRA"

    val path = getFilesFolder()

    val sinImg = painterResource(id = R.drawable.transunion)
    var showProgress = false


    var vcViewModel: VCViewModel = hiltViewModel()
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
            //Image(painter = sinImg, contentDescription = "Trans Union Img")

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
                        text = "Insurance",
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
                                    Log.d("focus", "lost focus")
                                }
                            },
                        label = {
                            Text(text = "First Name")
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
                        value = firstName,
                        onValueChange = {
                            // firstName = it
                        },
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Done,
                            keyboardType = KeyboardType.Email,
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {  },
                        ),

                        )
                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .onFocusChanged { focusState ->
                                if (!focusState.isFocused) {
                                    Log.d("focus", "lost focus")
                                }
                            },
                        label = {
                            Text(text = "Last Name")
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
                        value = lastName,
                        onValueChange = {
                            // firstName = it
                        },
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Done,
                            keyboardType = KeyboardType.Email,
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {  },
                        ),

                        )
                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .onFocusChanged { focusState ->
                                if (!focusState.isFocused) {
                                    Log.d("focus", "lost focus")
                                }
                            },
                        label = {
                            Text(text = "City")
                        },
                        textStyle = TextStyle(color = MaterialTheme.colors.inputTextColor),
                        leadingIcon = {
                            IconButton(
                                onClick = {}
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.LocationCity,
                                    contentDescription = "City ICon"
                                )
                            }
                        },
                        value = city,
                        onValueChange = {
                            // firstName = it
                        },
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Done,
                            keyboardType = KeyboardType.Email,
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {  },
                        ),

                        )

                    Button(
                        onClick = {
                            //showProgress = true
                            encryptSin(123456789, java.lang.String.valueOf(path), "sin")
                            vcViewModel.saveVC(
                                VCEnteryState(
                                    experationDate = Date(),
                                    issuerName = "SIN Issuer",
                                    VCTypeText = "SIN data",
                                    VCTypeEnum = VCType.INSURANCE_NUMBER,
                                    VCTitle = "SIN",
                                    VCContentOverview = "",
                                    VCAttribute = 123456789
                                )
                            )
                            navController.popBackStack()

                        },
                        colors  = ButtonDefaults.buttonColors(
                            contentColor = Color.White
                        ),

                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = "Get Your Insurance Number")

                    }

                }
            }
        }
    }

}