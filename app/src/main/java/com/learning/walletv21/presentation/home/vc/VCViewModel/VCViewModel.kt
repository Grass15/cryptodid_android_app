package com.learning.walletv21.presentation.home.vc.VCViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.learning.walletv21.core.protocols.javamodels.Claim
import com.learning.walletv21.data.local.entity.VCEntity
import com.learning.walletv21.domain.repository.UserRepository
import com.learning.walletv21.domain.use_case.get_vc.GetVCUseCase
import com.learning.walletv21.utils.Resource
import com.learning.walletv21.utils.Status
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject
import kotlin.collections.List

@HiltViewModel
class VCViewModel @Inject constructor(
    private val repository: UserRepository,
    private val getVCUseCase: GetVCUseCase
): ViewModel() {
    private val _VCState = MutableStateFlow(VCState(VCId = 10))
    private val _status = MutableStateFlow(Status.NO_ACTION)
    private val _vcDataState = MutableStateFlow<List<VCDataState?>>(emptyList())
    val vcDataState = combine(_vcDataState,_status){vcDataState, status ->
        vcDataState.map {vcdata ->
        vcdata?.copy(
            status = status
        )
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val userId = "945826e3-baaa-44c1-8aba-230b37426df3"
    init {
        fetchVCFlow(userId)
    }

      fun fetchVCFlow(userId: String){

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

                      val vcdataState: List<VCDataState?>? = result.data?.VCs?.let {
                          it.map {
                              it?.vc?.let {it1 ->
                                  com.learning.walletv21.presentation.home.vc.VCViewModel.VCDataState(
                                      experationDate = it1.expirationDate ?: java.util.Date(),
                                      issuerName = it1.issuerName.toString(),
                                      VCType = it1.type.toString(),
                                      VCTitle = it1.title.toString(),
                                      VCContentOverview = it1.content.toString()
                                  )
                              }

                          }
                      }
                      _vcDataState.value = vcdataState?: emptyList()
                  }
              }
          }.launchIn(viewModelScope)

    }

    fun storeClaim(newVC: VCState){
        _VCState.update { newVC }
        val VC = Claim(_VCState.value.VCTitle,_VCState.value.VCType,_VCState.value.issuerName,_VCState.value.VCContentOverview)
        viewModelScope.launch {
            repository.insertClaim(VCEntity(11,VC,"945826e3-baaa-44c1-8aba-230b37426df3"))
        }
    }
}