package com.loginid.cryptodid.presentation.claim_details

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.loginid.cryptodid.domain.use_case.get_claim.GetClaimUseCase
import com.loginid.cryptodid.utils.Constants.PARAM_CLAIM_ID
import com.loginid.cryptodid.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class ClaimDetailsViewModel @Inject constructor(
    private val getClaimUseCase: GetClaimUseCase,
    private val savedStateHandle: SavedStateHandle
) :ViewModel() {
     private val _state = mutableStateOf(ClaimDetailState())
    val state: State<ClaimDetailState> = _state

    init {
        savedStateHandle.get<String>(PARAM_CLAIM_ID)?.let{
            getClaim(it)
        }
    }

    private fun getClaim(coinId: String){
        getClaimUseCase(coinId).onEach { result ->
            when(result){
                is Resource.Success -> {
                    _state.value = ClaimDetailState(
                       claim = result.data?: null
                    )
                }
                is Resource.Error -> {
                    _state.value = ClaimDetailState(
                        error = result.message?: "An unexpected error occured"
                    )
                }
                is Resource.Loading -> {
                    _state.value = ClaimDetailState(
                            isLoading = true
                    )
                }
            }
        }.launchIn(viewModelScope)
    }
}