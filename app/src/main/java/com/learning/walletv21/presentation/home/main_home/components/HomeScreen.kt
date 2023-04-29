package com.learning.walletv21.presentation.home.main_home.components

import android.app.Activity
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.learning.walletv21.presentation.home.biometrics.BiometricAuthenticator
import com.learning.walletv21.presentation.home.vc.VCCard
import com.learning.walletv21.presentation.navigation.bottom_navigation.BottomSheetNavBodyItems
import com.learning.walletv21.presentation.navigation.bottom_navigation.BottomSheetNavigation
import com.learning.walletv21.presentation.navigation.drawer_navigation.*
import com.learning.walletv21.presentation.theme.HomeBackGround
import com.learning.walletv21.presentation.theme.OpsIcons
import kotlinx.coroutines.launch


@RequiresApi(Build.VERSION_CODES.P)
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

    //Biometrics Prompt
    val context = LocalContext.current
    val activity = LocalContext.current as Activity
    val biometricAuthenticator = remember { BiometricAuthenticator(context
    ) {
        scope.launch {
            modalSheetState.show()
        }
    }
    }
    var showPrompt by remember { mutableStateOf(false) }


Scaffold(

    floatingActionButton = {
        FloatingActionButton(onClick = {
            showPrompt = true
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
        ExpandableSearchCard()
        VCCard()
    }

    ModalBottomSheetLayout(modifier = Modifier.clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)), sheetState = modalSheetState, sheetContent = {
        BottomSheetNavigation(navitems = listOf(BottomSheetNavBodyItems.BankVC,BottomSheetNavBodyItems.CreditScoreVC,
        BottomSheetNavBodyItems.DriverLicenceVC,BottomSheetNavBodyItems.PersenalVC,BottomSheetNavBodyItems.TelecomVC,
            BottomSheetNavBodyItems.BlinkVC
            ), onItemClick = {Log.d("BI",it.route)})
    }) {

    }

    //Displaying Prompt
    if (showPrompt) {
        LaunchedEffect(true) {
            biometricAuthenticator.authenticate(activity)
            showPrompt = false
        }
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