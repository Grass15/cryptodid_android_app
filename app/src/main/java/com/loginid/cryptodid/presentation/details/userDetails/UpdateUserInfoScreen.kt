package com.loginid.cryptodid.presentation.details.userDetails

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun updateUserInfo(
    //Here goes the state and the viewModel
){
    
    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Box(modifier = Modifier.padding(15.dp),
            ){
            Text(text = "Here goes the update ser data", style = TextStyle(fontSize = 30.sp))
        }
    }

}