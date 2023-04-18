package com.learning.walletv21.presentation.claim_list

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.learning.walletv21.domain.use_case.get_claims.GetClaimsUseCase
import com.learning.walletv21.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class ClaimListViewModel @Inject constructor(
    private val getClaimsUseCase: GetClaimsUseCase
) :ViewModel() {
     private val _state = mutableStateOf(ClaimListState())
    val state: State<ClaimListState> = _state

    init {
        getClaims()
    }

    private fun getClaims(){
        getClaimsUseCase().onEach { result ->
            when(result){
                is Resource.Success -> {
                    _state.value = ClaimListState(
                       claims = result.data?: emptyList()
                    )
                }
                is Resource.Error -> {
                    _state.value = ClaimListState(
                        error = result.message?: "An unexpected error occured"
                    )
                }
                is Resource.Loading -> {
                    _state.value = ClaimListState(
                            isLoading = true
                    )
                }
            }
        }.launchIn(viewModelScope)
    }
}