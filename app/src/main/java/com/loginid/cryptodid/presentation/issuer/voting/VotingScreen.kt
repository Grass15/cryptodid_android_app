package com.loginid.cryptodid.presentation.issuer.voting

import android.app.Activity
import androidx.compose.animation.ExperimentalAnimationApi
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
import com.google.accompanist.pager.ExperimentalPagerApi
import com.loginid.cryptodid.data.local.entity.VCType
import com.loginid.cryptodid.presentation.MainActivity
import com.loginid.cryptodid.presentation.MainActivity.Companion.getFilesFolder
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

    external fun addPrivilege(
        SecretKey_Path: String?,
        Pk_Path: String?,
        CloudKey_Path: String?,
        CloudData_Path: String?,
        x1: Int,
        y1: Int,
        z1: Int
    ): Int
    @OptIn(ExperimentalAnimationApi::class, ExperimentalPagerApi::class)
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
        val path = getFilesFolder()
        val PKPath = path.toString() + "/PK.key"
        val cloudKeyPath = path.toString() + "/CK.key"
        val cloudDataPath = path.toString() + "/CD.data"
        val secretKeyPath = path.toString() + "/SK.key"

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

                //Uncomment the stuff below
                scannerViewModel.registreVoteScan{
                    addPrivilege(secretKeyPath, PKPath, cloudKeyPath, cloudDataPath, 2, 2, 1 )
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

