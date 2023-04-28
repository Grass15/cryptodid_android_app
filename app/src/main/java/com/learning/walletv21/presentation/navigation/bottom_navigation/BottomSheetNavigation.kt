package com.learning.walletv21.presentation.navigation.bottom_navigation

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.learning.walletv21.presentation.theme.secondBackGroundColor

@Composable
fun BottomSheetNavigation(
    navitems: List<BottomSheetNavBodyItems>,
    modifier: Modifier = Modifier,
    itemTextStyle: TextStyle = TextStyle(fontSize = 18.sp),
    onItemClick: (BottomSheetNavBodyItems) -> Unit
) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .background(MaterialTheme.colors.secondBackGroundColor)
        .padding(15.dp)) {
        Text(text = "Add new VC", style = MaterialTheme.typography.h4)
        
    }
    LazyColumn(modifier = modifier){
        items(navitems){item ->
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
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {

                    }
                    .padding(16.dp)
            ){
                Icon(imageVector = Icons.Filled.Home, contentDescription = "joizhfoirehfoirehoigfrhegoirhegoire")
            }
        }
    }
}
