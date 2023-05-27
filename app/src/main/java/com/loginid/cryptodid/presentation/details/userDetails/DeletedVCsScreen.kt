package com.loginid.cryptodid.presentation.details.userDetails

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun deletedVCsScreen(
    //Here goes the state and the viewModel
){

    Column(modifier = Modifier.fillMaxSize().padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {

            Text(text = "Here goes the deleted VC", style = TextStyle(fontSize = 30.sp))
            Box(modifier = Modifier.fillMaxWidth()){
                Button(onClick = { /*TODO*/ }) {
                    Text(text = "Restore all")
                }
        }
    }

}