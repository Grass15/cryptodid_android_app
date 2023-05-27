package com.loginid.cryptodid.presentation.navigation.drawer_navigation

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Sync
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.loginid.cryptodid.presentation.home.vc.VCViewModel.VCViewModel
import com.loginid.cryptodid.presentation.theme.RandomMaterial
import com.loginid.cryptodid.presentation.theme.firstBackGroundColor
import com.loginid.cryptodid.presentation.theme.inputTextColor
import com.loginid.cryptodid.utils.UserDataPrefrence


@Composable
fun FullDrawer(vcViewModel: VCViewModel = hiltViewModel(),onItemClick: (NavigationBodyItems) -> Unit){
    val userData = vcViewModel.userdata.collectAsState()
    Column(modifier = Modifier.fillMaxSize()){
        Box(modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.firstBackGroundColor)
            .weight(2f)){
            DrawerHeader(userData = userData.value!!)
        }
        Box(modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.firstBackGroundColor)
            .weight(5f)){
            DrawerBody(
                items = listOf(
                    NavigationBodyItems.DeletedVCs,
                    NavigationBodyItems.PersonalInfos,
                    NavigationBodyItems.Status,
                ),
                onItemClick = {onItemClick(it)}
            )
        }
        Box(modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.firstBackGroundColor)
            .weight(1f)){
            Divider(
                color = MaterialTheme.colors.inputTextColor,
                thickness = 2.dp,
                startIndent = 16.dp,
                modifier = Modifier.fillMaxWidth() //get doc gov vc
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {

                    }
                    .padding(16.dp)
            ){
                Icon(imageVector = Icons.Filled.Sync, contentDescription = "Synchronize account")
                Spacer(modifier = Modifier.width(16.dp))
                Text(text = "Sync you account",modifier = Modifier.weight(1f), style = TextStyle(fontSize = 18.sp))
                /*
                LinearProgressIndicator(
                progress = 0.5f, // Set the progress to 50%
                modifier = Modifier.fillMaxWidth() // Set the width of the progress bar to match its parent container
            )
                 */
            }
        }
    }

}

@Composable
fun DrawerHeader(userData: UserDataPrefrence){

    Column(modifier = Modifier.fillMaxSize()){
         Box(modifier = Modifier
             .fillMaxWidth()
             .padding(7.dp),
        contentAlignment = Alignment.CenterStart
        ){
             Column {
                 Text(text = userData.name, fontSize = 40.sp)
                 Text(text = userData.userId, color = Color.Gray)
             }
        }
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(7.dp)){
            TextButton(
                onClick = {
                }
            ) {
                Icon(imageVector = Icons.Filled.Email, contentDescription = "email")
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = userData.userName)
            }
        }
        Divider(
            color = MaterialTheme.colors.inputTextColor,
            thickness = 2.dp,
            startIndent = 16.dp,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun DrawerBody(
 items: List<NavigationBodyItems>,
 modifier: Modifier = Modifier,
 itemTextStyle: TextStyle = TextStyle(fontSize = 18.sp),
 onItemClick: (NavigationBodyItems) -> Unit
){

    LazyColumn(modifier){
        items(items){ item ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onItemClick(item)
                    }
                    .padding(16.dp)
            ){
                Icon(imageVector = item.icon, contentDescription = item.contentDescription)
                Spacer(modifier = Modifier.width(16.dp))
                Text(text = item.title,modifier = Modifier.weight(1f), style = itemTextStyle)
            }
        }
    }

}



@Composable
fun NavigationDrawer() {

}