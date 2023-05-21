package com.loginid.cryptodid.presentation.issuer.voting

import android.app.Activity
import android.util.Log
import com.loginid.cryptodid.data.local.entity.VCType
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.loginid.cryptodid.presentation.home.biometrics.BiometricsAuthenticationProvider
import com.loginid.cryptodid.presentation.home.modalDialogs.ModalDialogs
import com.loginid.cryptodid.presentation.home.vc.VCViewModel.VCEnteryState
import com.loginid.cryptodid.presentation.navigation.screens.HomeScreen
import com.loginid.cryptodid.presentation.theme.HomeBackGround
import kotlinx.coroutines.launch
import java.util.*


class VotingScreen constructor(
    val navController: NavController,
    val onScanResult: (VCEnteryState) -> Unit,
){
    private var showProgress = true

    @Composable
    fun AddPrivilegeScreen(
    ){
        //data to send
        var votenumbers = remember {
            listOf(1,1,1)
        }

        //getting current context
        val context = LocalContext.current
        val activity = context as Activity

        //getting the scanner
        val scannerViewModel: VotingViewModel = hiltViewModel()

        //couroutune scope for async work
        val scope = rememberCoroutineScope()

        //modal dialog to display incase of errors
        val modalDialogsFlow = remember { mutableStateOf<ModalDialogs?>(null) }


        //Biometrics Modal Dialog
        var showDialog by remember {
            mutableStateOf(false)
        }

        val biometricAuthenticator = remember { BiometricsAuthenticationProvider(context,
            onBiometricFailled = {
                if(!it.isSupported){
                    modalDialogsFlow.value = ModalDialogs(it.erroMessage,"Biometrics")
                    showDialog = true
                }
            }
        ) {
            scope.launch {
                  //Here we can make the same thing
            }
        }
        }

        var showPrompt by remember { mutableStateOf(true) }

        Box(modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.HomeBackGround)) {

            if (showProgress) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }


        //This does not require biometrics
        LaunchedEffect(true){
            scope.launch {
                scannerViewModel.registreVoteScan{
                    onScanResult(
                        VCEnteryState(
                            experationDate = Date(),
                            issuerName = "Vote",
                            VCTypeText = "vote",
                            VCTitle = "Privilege",
                            VCContentOverview = "none",
                            VCTypeEnum = VCType.PRIVILEGE,
                            VCAttribute = it
                    )
                    )
                  //  delay(1000)
                    navController.navigate(HomeScreen.MainHomeScreen.route){
                        popUpTo(HomeScreen.MainHomeScreen.route){
                            inclusive = true
                        }
                    }

                }
            }
        }


        //Showing dialog

        //Dialogs for biometrics
/*
        if(showDialog){
            modalDialogsFlow.value?.let {
                it.BiometricsAlertDialog(onDismiss = {
                    showDialog = it
                    showProgress = it
                })
            }
        }

        if (showPrompt) {
            LaunchedEffect(true) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    biometricAuthenticator.getBiometricAuthenticator(BiomtricType.AUTO)?.authenticate(activity)
                }
                showPrompt = false
                showProgress = false
            }
        }*/

    }

}

