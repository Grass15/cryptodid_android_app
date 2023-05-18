package com.loginid.cryptodid.presentation.navigation.bottom_navigation

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.loginid.cryptodid.presentation.home.microblink.ScanIDCard
import com.loginid.cryptodid.presentation.home.vc.VCViewModel.VCEnteryState
import com.loginid.cryptodid.presentation.home.vc.VCViewModel.VCViewModel
import com.loginid.cryptodid.presentation.theme.secondBackGroundColor

@Composable
fun BottomSheetNavigation(
    vcViewModel: VCViewModel = hiltViewModel(),
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
        /*item {
             ScanIDCard(
                 onScanResult = {
                     Log.d("Extracted",it.toString())
                     vcViewModel.saveVC(
                         VCEnteryState(
                             experationDate = java.util.Date(),
                             issuerName = "MicroBlink",
                             VCType = "Personal data",
                             VCTitle = "Age",
                             VCContentOverview = "+18",
                             VCAttribute = it
                         )
                     )
                 },
                 launchCamera = {
                     it.launch(null)
                 })
        }*/
    }
}
