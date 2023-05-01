package com.loginid.cryptodid.presentation.home.vc.VCViewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.loginid.cryptodid.domain.use_case.get_vc.GetVCUseCase
import com.loginid.cryptodid.domain.use_case.remove_vc.RemoveVCUseCase
import com.loginid.cryptodid.domain.use_case.save_vc.SaveVCUseCase
import com.loginid.cryptodid.utils.Resource
import com.loginid.cryptodid.utils.Status
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import java.util.UUID
import javax.inject.Inject
import kotlin.collections.List

@HiltViewModel
class VCViewModel @Inject constructor(
  //  private val repository: UserRepository,
    private val getVCUseCase: GetVCUseCase,
    private val removeVCUseCase: RemoveVCUseCase,
    private val saveVCUseCase: SaveVCUseCase
): ViewModel() {
    private val _VCEnteryState = MutableStateFlow(VCEnteryState())
    private val _status = MutableStateFlow(Status.NO_ACTION)
    private val _vcAction = MutableStateFlow(VCActionState())
    private val _vcDataDisplayState = MutableStateFlow<List<VCDataDisplayState?>>(emptyList())
    val vcDataState = combine(_vcDataDisplayState,_status){ vcDataState, status ->
        vcDataState.map {vcdata ->
        vcdata?.copy(
            status = status
        )
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val vcAction = combine(_vcAction,_status){vcAction, status ->
        vcAction.copy(
            status = status
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), VCActionState())
    val userId = "660cc213-e07a-430d-af52-0b69a6b1d33b"
    init {
        fetchVCFlow(userId)
    }

      fun fetchVCFlow(userId: String){
          resetStatus()
          getVCUseCase(userId).onEach { result ->
              when(result){
                  is Resource.Error -> {
                     _status.value = Status.ERROR
                  }
                  is Resource.Loading -> {
                      _status.value = Status.LOADING
                  }
                  is Resource.Success -> {
                      _status.value = Status.SUCCESS

                      val vcdataDisplayStates: List<VCDataDisplayState?>? = result.data?.VCs?.let {
                          it.map {
                              it?.vc?.let {it1 ->
                                 VCDataDisplayState(
                                      experationDate = it1.expirationDate ?: null,
                                      issuerName = it1.issuerName.toString(),
                                      VCType = it1.type.toString(),
                                      VCTitle = it1.title.toString(),
                                      VCContentOverview = it1.content.toString(),
                                      VCID = it.id,
                                      rawVC = it1
                                  )
                              }

                          }
                      }
                      _vcDataDisplayState.value = vcdataDisplayStates?: emptyList()
                  }
              }
          }.launchIn(viewModelScope)

    }

    fun deleteVC(VCID: String){
        resetStatus()
        removeVCUseCase(VCID).onEach {res ->
            _vcAction.update { it.copy(
                actionText = "Deleting a vc"
            ) }
            when(res){
                is Resource.Error -> {
                    _status.value = Status.ERROR
                    Log.d("error","oppps something went wrong")
                }
                is Resource.Loading -> _status.value = Status.LOADING
                is Resource.Success -> {
                    _status.value = Status.SUCCESS
                    Log.d("success","oppps something went wrong")
                }
            }
        }.launchIn(viewModelScope)
    }

    fun resetStatus(){
        _status.value = Status.NO_ACTION
    }


    fun saveVC(newVC: VCEnteryState){
        resetStatus()
        _VCEnteryState.update { newVC }
        saveVCUseCase(UUID.randomUUID().toString(), ownerID = userId, vcContent = newVC).onEach { result ->
            when(result){
                is Resource.Error -> {
                    _status.value = Status.ERROR
                    Log.d("error","oppps something went wrong")
                }
                is Resource.Loading -> _status.value = Status.LOADING
                is Resource.Success -> {
                    _status.value = Status.SUCCESS
                    Log.d("success","oppps something went wrong")
                }
            }
        }.launchIn(viewModelScope)
    }

/*
    fun storeClaim(newVC: VCEnteryState){
        _VCEnteryState.update { newVC }
        val VC = Claim(_VCEnteryState.value.VCTitle,_VCEnteryState.value.VCType,_VCEnteryState.value.issuerName,_VCEnteryState.value.VCContentOverview)
        viewModelScope.launch {
            repository.insertVC(VCEntity("13",VC,"5b111b90-a07a-4c91-ae96-9e71d188cd10"))
        }
    }
    */

}