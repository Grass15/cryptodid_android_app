package com.loginid.cryptodid.presentation.issuer.voting

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.loginid.cryptodid.claimVerifier.VerificationStatus
import com.loginid.cryptodid.claimVerifier.Verifier
import com.loginid.cryptodid.domain.repository.ScannerRepository
import com.loginid.cryptodid.domain.use_case.verify_vc.privilege.VotingUseCase
import com.loginid.cryptodid.model.Claim
import com.loginid.cryptodid.presentation.home.scanner.ScannerStrategy.Scanner
import com.loginid.cryptodid.utils.Resource
import com.loginid.cryptodid.utils.Status
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject


//Here we will invoke the use case corresponding to the verification of vote
@HiltViewModel
class VotingViewModel @Inject constructor(
    private val repository: ScannerRepository,
    private val voteUseCase: VotingUseCase
): ViewModel(),Scanner {

    //Configuring Scanner for Privileges
    private var privilegeVars = listOf(1,1,1)
    private val privilegeDataSender = PrivilegeDataSender(privilegeVars)
    private val _vState = MutableStateFlow(VerificationStatus())
    val voteState = _vState.asStateFlow()

    val verifier = Verifier()

    suspend fun preparePrivilegeScan(): String{
        val deferred = CompletableDeferred<String>()
        viewModelScope.launch {
            repository.startScanning().collect{data ->
                if(!data.isNullOrBlank()){
                    deferred.complete(data)
                }
            }
        }

        return deferred.await()
    }

    fun registreVoteScan(completion: (Int) -> Unit){
        viewModelScope.launch {
            repository.startScanning().collect{data ->
                if(!data.isNullOrBlank()){
                    privilegeDataSender.setUrl(data)
                    privilegeDataSender.SendSocketData()
                    //completion(privilegeDataSender.ToCubeValue().get(0))
                    completion(3)
                }
            }
        }
    }

    fun verifyVoteScan(){
        resetStatus()
        viewModelScope.launch {
            repository.startScanning().collect{data ->
                if(!data.isNullOrBlank()){
                    verifier.setUrl(data)
                    startVoteVerification(verifier)
                }
            }
        }
    }

    fun initializeVote(vc: Claim){
      Log.d("StartingVote","Votiiiing")
        verifier.setVc(vc)
    }

    private fun startVoteVerification(verifier: Verifier){
        voteUseCase(verifier).onEach { result ->
            when(result){
                is Resource.Error -> {
                    _vState.update { it.copy(
                        vMessage = result.message.toString(),
                        vStatus = Status.ERROR
                    ) }
                }
                is Resource.Loading -> {
                    _vState.update { it.copy(
                        vMessage = "Waiting verification",
                        vStatus = Status.LOADING
                    ) }
                    Log.d("Loading","Loadiiiiiiiiiiiing")
                }
                is Resource.Success -> {
                    _vState.update { it.copy(
                        vMessage = result.data!!.vMessage,
                        vStatus = result.data!!.vStatus
                    ) }
                    Log.d("Success","Successssssssss")
                }
            }
        }.launchIn(CoroutineScope(Dispatchers.IO))
    }

    override fun startScanning() {
        verifyVoteScan()
    }

    override fun setupVerifier(vc: Claim) {
        initializeVote(vc)
    }

    override fun startVerification(verifier: Verifier) {
        startVoteVerification(verifier)
    }
    override fun resetStatus(){
        _vState.update { it.copy(
            vMessage = "",
            vStatus = Status.NO_ACTION
        ) }
    }

    override fun displayScannerType() {
        Log.d("ChangingScanner","To VotingSCANNER Viewmodel")
    }

    override fun getVerificationStatus(): StateFlow<VerificationStatus> {
       return  _vState.asStateFlow()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        val otherScanner = other as Scanner

        // Implement your own logic to compare properties for equality
        // Return false if any properties are not equal

        // For example:
        // if (property1 != otherScanner.property1) return false
        // if (property2 != otherScanner.property2) return false

        return true
    }

}