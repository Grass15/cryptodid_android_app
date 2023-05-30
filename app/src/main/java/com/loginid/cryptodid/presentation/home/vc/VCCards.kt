package com.loginid.cryptodid.presentation.home.vc

import android.app.Activity
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.loginid.cryptodid.claimVerifier.VerificationStatus
import com.loginid.cryptodid.data.local.entity.VCType
import com.loginid.cryptodid.model.Claim
import com.loginid.cryptodid.presentation.home.biometrics.BiometricsAuthenticationProvider
import com.loginid.cryptodid.presentation.home.biometrics.BiomtricType
import com.loginid.cryptodid.presentation.home.modalDialogs.ModalDialogs
import com.loginid.cryptodid.presentation.home.scanner.ScannerStrategy.Scanner
import com.loginid.cryptodid.presentation.home.scanner.ScannerStrategy.ScannerProvider
import com.loginid.cryptodid.presentation.home.scanner.ScannerStrategy.emptyScanner
import com.loginid.cryptodid.presentation.home.scanner.ScannerViewModel
import com.loginid.cryptodid.presentation.home.vc.VCViewModel.MultipleVCOperations
import com.loginid.cryptodid.presentation.home.vc.VCViewModel.VCDataDisplayState
import com.loginid.cryptodid.presentation.home.vc.VCViewModel.VCViewModel
import com.loginid.cryptodid.presentation.issuer.voting.VotingViewModel
import com.loginid.cryptodid.utils.Status
import kotlinx.coroutines.delay

import kotlinx.coroutines.launch


//@ExperimentalMaterialApi
@RequiresApi(Build.VERSION_CODES.P)
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun VCCard(
    vcViewModel: VCViewModel = hiltViewModel(),
    voteViewModel: VotingViewModel = hiltViewModel(),
    onStartMutipleVCOperationFlag : (Boolean) -> Unit,
    startOperation : MultipleVCOperations,
    onVerificationStateAction: (VerificationStatus) -> Unit
) {


    //Handle Checklist
    var displaCheckBox by remember { mutableStateOf(false) }
    var checkedListIndex by remember {
        mutableStateOf(0)
    }
    val checkedVCList = remember { mutableListOf<VCDataDisplayState>() }

    //Scanner
    //val scannerViewModel: ScannerViewModel = hiltViewModel()
    //val scannedText = scannerViewModel.state.collectAsState()

    //Providing Scanner
    val scannerProvider = ScannerProvider(hiltViewModel(), hiltViewModel(), hiltViewModel())
    var scanner : Scanner by remember {
        mutableStateOf(emptyScanner())
    }

    //Biometrics Modal Dialog
    var showDialog by remember {
        mutableStateOf(false)
    }
    val modalDialogsFlow = remember { mutableStateOf<ModalDialogs?>(null) }

    //Deletion Modal Dialog
    var showDeletionDialog by remember {
        mutableStateOf(false)
    }
    val modalDeletionDialogsFlow = remember { mutableStateOf<ModalDialogs?>(null) }


    //Biometrics Prompt
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val activity = LocalContext.current as Activity

    var showPrompt by remember { mutableStateOf(false) }

    //States
    val vcstate = vcViewModel.vcDataState.collectAsState()
    val verificationState = scanner.getVerificationStatus().collectAsState()
    //scannerViewModel.vState.collectAsState()
    val vcActionState = vcViewModel.vcAction.collectAsState()

    LazyColumn(modifier = Modifier.padding(12.dp)){
        itemsIndexed(vcstate.value){index,vc->
            vc?.let {
                CardSwiper(
                    VCState = it,
                    onDeleteButtonClicked = {
                            modalDeletionDialogsFlow.value = ModalDialogs(message ="Are you sure you want to delete this vc",title="VC Deletion", vcid = it.VCID)
                            showDeletionDialog = true
                            //showDialog = true

                      /*  vcViewModel.deleteVC(it.VCID).run {
                            /*when(vcActionState.value.status){
                                Status.ERROR -> TODO()
                                Status.SUCCESS -> TODO()
                                Status.FAILLED -> TODO()
                                Status.LOADING -> TODO()
                                Status.NO_ACTION -> TODO()
                            }*/
                        }*/
                    },
                    onVerifyButtonClicked = {
                        it.rawVC?.let {it1->
                            if(it.VCTypeEnum  == VCType.PRIVILEGE){
                               scanner = scannerProvider.getScanner("priv")
                               scanner.setupVerifier(it1)
                               scanner.resetStatus()
                               scanner.displayScannerType()
                               showPrompt = true

                            }else if(it.VCTypeText  == "sin"){
                                scanner = scannerProvider.getScanner("sin")
                                scanner.setupVerifier(it1)
                                scanner.resetStatus()
                                scanner.displayScannerType()
                                showPrompt = true

                            }
                            else{
                               scanner = scannerProvider.getScanner("vc")
                                scanner.setupVerifier(it1)
                                scanner.resetStatus()
                                scanner.displayScannerType()
                                showPrompt = true
                            }
                        }
                    },
                    onDisplayCheckBoxes = {
                        if(it){
                            if(checkedVCList.size>0) {
                                checkedVCList.clear()
                                checkedListIndex = 0
                            }
                        }
                                          displaCheckBox = it
                        onStartMutipleVCOperationFlag(it)
                    },
                    displaCheckBox = displaCheckBox,
                    onCheckBoxChecked = {checked,vcDatastate ->
                        if(displaCheckBox){
                            if(checked){
                                vcDatastate.rawVC?.let { it1 -> checkedVCList.add(checkedListIndex, vcDatastate)
                                checkedListIndex ++
                                }
                            }else{
                                    vcDatastate.rawVC?.let {
                                        if(checkedVCList.contains(vcDatastate)){
                                            checkedVCList.remove(vcDatastate)
                                            checkedListIndex--
                                        }
                                    }
                            }
                            Log.d("CheckList","${checkedVCList.size}")
                        }
                    }
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            /*
             * Just to test verification outside of the couroutine scope
             */
            /*
            Text(text = "https://www.google.com", modifier = Modifier.clickable {
                scannerViewModel.startVerification()
            })
           */
        }
    }

    val biometricAuthenticator = remember(scanner) { BiometricsAuthenticationProvider(context,
        onBiometricFailled = {
            if(!it.isSupported){
                modalDialogsFlow.value = ModalDialogs(it.erroMessage,"Biometrics")
                showDialog = true
            }
        }
    ) {
            scope.launch {
                //scannerViewModel.startScanning()
                // voteViewModel.verifyVoteScan()
               // scanner.startScanning()
                delay(500)
                scanner.startScanning()
                showPrompt = false
            }
    }
    }


    //scannerViewModel.startScanning()

    //Displaying dialog incase of errors

    if(showDialog){
        modalDialogsFlow.value?.let {
            it.BiometricsAlertDialog(onDismiss = {
                showDialog = it
            })
        }

    }

    //VCDeletion Dialog

    if(showDeletionDialog){
        modalDeletionDialogsFlow.value?.let {
            if(it.vcid.length>10)
            {
                it.VCDeletionAlertDialog(
                    onDismiss = {
                        modalDeletionDialogsFlow.value = null
                        showDeletionDialog = it
                    },
                    onProceed = {proceed ->
                        vcViewModel.deleteVC(it.vcid).run {
                            /*when(vcActionState.value.status){
                                Status.ERROR -> TODO()
                                Status.SUCCESS -> TODO()
                                Status.FAILLED -> TODO()
                                Status.LOADING -> TODO()
                                Status.NO_ACTION -> TODO()
                            }*/
                        }
                        showDeletionDialog = proceed
                    }) {
                    Text(text = it, color = Color.Red)
                }

            }else if (it.vcidList.isNotEmpty()){
                it.VCDeletionAlertDialog(
                    onDismiss = {
                        modalDeletionDialogsFlow.value = null
                        showDeletionDialog = it
                    },
                    onProceed = {proceed ->
                        vcViewModel.multipleDelete(it.vcidList).run {
                            /*when(vcActionState.value.status){
                                Status.ERROR -> TODO()
                                Status.SUCCESS -> TODO()
                                Status.FAILLED -> TODO()
                                Status.LOADING -> TODO()
                                Status.NO_ACTION -> TODO()
                            }*/
                        }
                        showDeletionDialog = proceed
                    }) {
                    Text(text = it, color = Color.Red)
                }
            }
        }
    }

    //Displaying Prompt
    if (showPrompt) {
        LaunchedEffect(showPrompt,scanner) {
            biometricAuthenticator.getBiometricAuthenticator(BiomtricType.AUTO)?.authenticate(activity)
           // showPrompt = false
//            scope.launch {
//                //scannerViewModel.startScanning()
//                // voteViewModel.verifyVoteScan()
//                // scanner.startScanning()
//                delay(500)
//                scanner.startScanning()
//                showPrompt = false
//            }

        }

    }

    when(verificationState.value.vStatus){
        Status.ERROR -> {
            Log.d("Verification",verificationState.value.vMessage)
            onVerificationStateAction(verificationState.value)
            scanner.resetStatus()
        }
        Status.SUCCESS -> {
            Log.d("Verification",verificationState.value.vMessage)
            onVerificationStateAction(verificationState.value)
            scanner.resetStatus()
        }
        Status.FAILLED -> {
            Log.d("Verification",verificationState.value.vMessage)
            scanner.resetStatus()
        }
        Status.LOADING -> {
            Log.d("Verification",verificationState.value.vMessage)
            onVerificationStateAction(verificationState.value)
            scanner.resetStatus()
        }
        Status.NO_ACTION -> {
            Log.d("Verification",verificationState.value.vMessage)
            onVerificationStateAction(verificationState.value)
        }
    }

    //Multiple and single deletion Operation
    when(startOperation){
        MultipleVCOperations.ON_MULTIPLE_DELETE -> {
            Log.d("Delete","${checkedVCList.size}")

            if(checkedVCList.size>0) {
            displaCheckBox = false
                modalDeletionDialogsFlow.value = ModalDialogs(message ="Are you sure you want to delete these VCs",title="VC Deletion", vcidList = checkedVCList.map { it.VCID })
                showDeletionDialog = true
                checkedVCList.clear()
                checkedListIndex = 0
                onStartMutipleVCOperationFlag(false)
            }
        }
        MultipleVCOperations.ON_MULTIPLE_VERIFICATION -> {
            Log.d("Verify","${checkedVCList.size}")
        }
        MultipleVCOperations.ON_CANCEL -> {
            Log.d("Cancel","${checkedVCList.size}")
            if(checkedVCList.size>0) {
                displaCheckBox = false
                checkedVCList.clear()
                checkedListIndex = 0
                onStartMutipleVCOperationFlag(false)
            }else{
                displaCheckBox = false
                checkedListIndex = 0
                onStartMutipleVCOperationFlag(false)
            }
        }
        MultipleVCOperations.ON_NO_ACTION -> {
            //Log.d("NO_ACTION","Finished all ops")
        }
    }

}



/*

@Preview(showBackground = true)
@Composable
fun previewVCCard(){
    VCCard()
}*/