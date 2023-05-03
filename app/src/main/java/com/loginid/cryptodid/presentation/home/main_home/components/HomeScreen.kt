package com.loginid.cryptodid.presentation.home.main_home.components

import android.app.Activity
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.SnackbarDefaults.backgroundColor
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.loginid.cryptodid.claimVerifier.VerificationStatus
import com.loginid.cryptodid.presentation.home.biometrics.BiometricAuthenticator
import com.loginid.cryptodid.presentation.home.modalDialogs.ModalDialogs
import com.loginid.cryptodid.presentation.home.vc.VCCard
import com.loginid.cryptodid.presentation.navigation.bottom_navigation.BottomSheetNavBodyItems
import com.loginid.cryptodid.presentation.navigation.bottom_navigation.BottomSheetNavigation
import com.loginid.cryptodid.presentation.navigation.drawer_navigation.*
import com.loginid.cryptodid.presentation.theme.CardForGround
import com.loginid.cryptodid.presentation.theme.HomeBackGround
import com.loginid.cryptodid.presentation.theme.OpsIcons
import com.loginid.cryptodid.utils.Status
import kotlinx.coroutines.launch
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment


@RequiresApi(Build.VERSION_CODES.P)
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeScreen(
    navController: NavController,
    appBarViewModel: SearchAppBarViewModel = hiltViewModel(),
) {
    //Demo dialog
    var showDialog by remember {
        mutableStateOf(false)
    }
    var modalDialogs = ModalDialogs()

    //General states
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp

    //Screen Configuration

    //Verification responce
    var isLoading by remember {
        mutableStateOf(false)
    }



    val snackbarHostState = remember { SnackbarHostState() }
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    val modalSheetState =  rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)

    //AppBar Component
    val searchWidgetState by appBarViewModel.searchWidgetState
    val searchTextState by appBarViewModel.searchTextState

    //Biometrics Prompt
    val context = LocalContext.current
    val activity = LocalContext.current as Activity
    val biometricAuthenticator = remember { BiometricAuthenticator(context,
        onBiometricFailled = {
           modalDialogs = ModalDialogs(it.erroMessage,"Biometrics")
            showDialog = true
        }
       ) {
            scope.launch {
                modalSheetState.show()
            }
        }
        }
    var showPrompt by remember { mutableStateOf(false) }


Scaffold(

    snackbarHost = {

        SnackbarHost(
            hostState = snackbarHostState,
            snackbar = {
                Snackbar(
                    action = {
                        Text(
                            text = it.actionLabel.toString(),
                            style = MaterialTheme.typography.body2,
                            color = Color(0xFF2980B9),
                            modifier = Modifier
                                .padding(horizontal = 8.dp)
                                .clickable { snackbarHostState.currentSnackbarData?.dismiss() }
                        )
                    },
                   // modifier = Modifier.background(Color.Black).padding(8.dp)
                backgroundColor = MaterialTheme.colors.CardForGround
                ) {
                    Text(
                        text = it.message,
                        style = MaterialTheme.typography.body2,
                        color = Color.White
                    )
                }
            },
          //  modifier = Modifier.background(Color.Black)
        )


    },

    floatingActionButton = {
        FloatingActionButton(onClick = {
            showPrompt = true
            showDialog = true
        }, backgroundColor = MaterialTheme.colors.OpsIcons) {
          Icon(imageVector = Icons.Default.Add, contentDescription = "Adding a new VC")
        }
    },
    isFloatingActionButtonDocked = false,
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
        VCCard {
            val snackbarColor = when(it.vStatus) {
                Status.ERROR -> Color.Red
                Status.SUCCESS -> Color.Blue
                Status.LOADING -> Color.Gray
                Status.NO_ACTION -> Color.Red
                Status.FAILLED -> TODO()
            }
            when(it.vStatus){
                Status.ERROR -> {
                    scope.launch {
                        snackbarHostState.showSnackbar(
                            message = it.vMessage,
                            actionLabel = "Error"
                        )
                    }
                    isLoading = false

                }

                Status.SUCCESS -> {
                    scope.launch {
                        snackbarHostState.showSnackbar(it.vMessage,actionLabel = "Success")
                    }
                    isLoading = false
                }
                Status.LOADING -> {
                    scope.launch {
                        snackbarHostState.showSnackbar(it.vMessage,actionLabel = "Loading ...")
                    }
                    isLoading = true
                }
                Status.NO_ACTION -> {
                    isLoading = false
                }
                Status.FAILLED -> TODO()
            }
        }
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
/*
    if(isLoading){
        Box(modifier = Modifier
            .size(screenWidth, screenHeight)
            .background(Color.Transparent),contentAlignment = Alignment.Center) {
          CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
    }
    */
/*
    if(isLoading){
        Box(modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent)
            .blur(20.dp),contentAlignment = Alignment.Center) {
            Box(modifier = Modifier
                .wrapContentSize()
                .background(Color.White, CircleShape)
                .padding(16.dp),contentAlignment = Alignment.Center) {
                CircularProgressIndicator(modifier = Modifier.size(50.dp))
            }
        }
    }
*/
/*
        LaunchedEffect(!isLoading){
            scope.launch {
                snackbarHostState.showSnackbar(
                    message = "fregregre",
                    actionLabel = "Error"
                )
            }
        }
*/

    //Displaying dialog incase of errors

     if(showDialog){
       modalDialogs.BiometricsAlertDialog(onDismiss = {
           showDialog = it
       })
   }


}

}

@Composable
fun VerificationSnackBar(
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    title: String,
    action: String,
    actionColor: Color,
    onAction: () -> Unit = { }
) {
    Snackbar(
        elevation = 0.dp, //removing shadow
        backgroundColor = MaterialTheme.colors.CardForGround
    ) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ){
            Row(modifier = modifier
                .padding(7.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = title)
                Text(text = action, modifier = Modifier.clickable {

                },
                color = actionColor
                    )
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