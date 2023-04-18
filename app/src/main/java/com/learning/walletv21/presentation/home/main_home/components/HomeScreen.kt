package com.learning.walletv21.presentation.home.main_home.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.learning.walletv21.presentation.home.vc.VCCard
import com.learning.walletv21.presentation.navigation.bottom_navigation.BottomSheetNavBodyItems
import com.learning.walletv21.presentation.navigation.bottom_navigation.BottomSheetNavigation
import com.learning.walletv21.presentation.navigation.drawer_navigation.*
import com.learning.walletv21.presentation.theme.HomeBackGround
import com.learning.walletv21.presentation.theme.OpsIcons
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeScreen(
    navController: NavController,
    appBarViewModel: SearchAppBarViewModel = hiltViewModel(),
) {
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    val modalSheetState =  rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)

    //AppBar Component
    val searchWidgetState by appBarViewModel.searchWidgetState
    val searchTextState by appBarViewModel.searchTextState


Scaffold(

    floatingActionButton = {
        FloatingActionButton(onClick = {
            scope.launch {
                modalSheetState.show()
            }
        }, backgroundColor = MaterialTheme.colors.OpsIcons) {
          Icon(imageVector = Icons.Default.Add, contentDescription = "Adding a new VC")
        }
    },
    isFloatingActionButtonDocked = true,
    scaffoldState = scaffoldState,
    topBar = {
             MainAppBar(
                 searchWidgetState = searchWidgetState,
                 searchTextState = searchTextState,
                 onTextChange = {
                     appBarViewModel.updateSearchTextState(newValue = it)
                 },
                 onCloseClicked = {
                     appBarViewModel.updateSearchWidgetState(newValue = SearchWidgetState.CLOSED)
                 },
                 onSearchClicked = {
                     Log.d("Searched Text", it)
                 },
                 onSearchTriggered = {
                     appBarViewModel.updateSearchWidgetState(newValue = SearchWidgetState.OPENED)
                 },
                 onNavigationIconClick = {
                     scope.launch {
                         scaffoldState.drawerState.open()
                     }
                 }
                 )
            /* AppBar (
                 onNavigationIconClick = {
                     scope.launch {
                         scaffoldState.drawerState.open()
                     }
                 },{}
             )*/
    },
    drawerContent = {
        DrawerHeader()
        DrawerBody(
            items = listOf(
                NavigationBodyItems.DeletedVCs,
                NavigationBodyItems.PersonalInfos,
                NavigationBodyItems.Status,
            ),
            ){

                Log.d("Drawer","Item clicked")

        }
    },
    
) {
    Column(modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colors.HomeBackGround)
        .padding(it)) {
       // Text(text = "Hello", fontSize = MaterialTheme.typography.h3.fontSize)
        VCCard()
    }

    ModalBottomSheetLayout(sheetState = modalSheetState, sheetContent = {
        BottomSheetNavigation(navitems = listOf(BottomSheetNavBodyItems.BankVC,BottomSheetNavBodyItems.CreditScoreVC,
        BottomSheetNavBodyItems.DriverLicenceVC,BottomSheetNavBodyItems.PersenalVC,BottomSheetNavBodyItems.TelecomVC
            ), onItemClick = {})
    }) {

    }
}

}


//Top AppBar widget

@Composable
fun MainAppBar(
    searchWidgetState: SearchWidgetState,
    searchTextState: String,
    onTextChange: (String) -> Unit,
    onCloseClicked: () -> Unit,
    onSearchClicked: (String) -> Unit,
    onSearchTriggered: () -> Unit,
    onNavigationIconClick : () -> Unit
) {
    when (searchWidgetState) {
        SearchWidgetState.CLOSED -> {
            AppBar(
                onNavigationIconClick = onNavigationIconClick,
                onSearchClicked = onSearchTriggered
            )
        }
        SearchWidgetState.OPENED -> {
            SearchAppBar(
                text = searchTextState,
                onTextChange = onTextChange,
                onCloseClicked = onCloseClicked,
                onSearchClicked = onSearchClicked
            )
        }
    }
}