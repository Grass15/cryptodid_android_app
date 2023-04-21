package com.learning.walletv21.presentation.home.vc.VCViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.learning.walletv21.core.protocols.javamodels.Claim
import com.learning.walletv21.data.local.entity.VCEntity
import com.learning.walletv21.domain.repository.UserRepository
import com.learning.walletv21.domain.use_case.get_vc.GetVCUseCase
import com.learning.walletv21.domain.use_case.remove_vc.RemoveVCUseCase
import com.learning.walletv21.utils.Resource
import com.learning.walletv21.utils.Status
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.collections.List

@HiltViewModel
class VCViewModel @Inject constructor(
    private val repository: UserRepository,
    private val getVCUseCase: GetVCUseCase,
    private val removeVCUseCase: RemoveVCUseCase
): ViewModel() {
    private val _VCEnteryState = MutableStateFlow(VCEnteryState(VCId = 10))
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
    val userId = "945826e3-baaa-44c1-8aba-230b37426df3"
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
                                  com.learning.walletv21.presentation.home.vc.VCViewModel.VCDataDisplayState(
                                      experationDate = it1.expirationDate ?: java.util.Date(),
                                      issuerName = it1.issuerName.toString(),
                                      VCType = it1.type.toString(),
                                      VCTitle = it1.title.toString(),
                                      VCContentOverview = it1.content.toString(),
                                      VCID = it.id
                                  )
                              }

                          }
                      }
                      _vcDataDisplayState.value = vcdataDisplayStates?: emptyList()
                  }
              }
          }.launchIn(viewModelScope)

    }

    fun deleteVC(VCID: Int){
        resetStatus()
        removeVCUseCase(VCID).onEach {res ->
            _vcAction.update { it.copy(
                actionText = "Deleting a vc"
            ) }
            when(res){
                is Resource.Error -> _status.value = Status.ERROR
                is Resource.Loading -> _status.value = Status.LOADING
                is Resource.Success -> _status.value = Status.SUCCESS
            }
        }.launchIn(viewModelScope)
    }

    fun resetStatus(){
        _status.value = Status.NO_ACTION
    }
    init {
        storeClaim(VCEnteryState(VCId = 15))
    }

    fun storeClaim(newVC: VCEnteryState){
        _VCEnteryState.update { newVC }
        val VC = Claim(_VCEnteryState.value.VCTitle,_VCEnteryState.value.VCType,_VCEnteryState.value.issuerName,_VCEnteryState.value.VCContentOverview)
        viewModelScope.launch {
            repository.insertVC(VCEntity(13,VC,"945826e3-baaa-44c1-8aba-230b37426df3"))
        }
    }
}