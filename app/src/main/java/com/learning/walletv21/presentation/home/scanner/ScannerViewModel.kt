package com.learning.walletv21.presentation.home.scanner


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.learning.walletv21.domain.repository.ScannerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ScannerViewModel @Inject constructor(
      private val repository: ScannerRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ScannerState())
    val state = _state.asStateFlow()

    /*init {
        startScanning()
    }*/

    fun startScanning(){
        viewModelScope.launch {
            repository.startScanning().collect{data ->
                if(!data.isNullOrBlank()){
                    _state.update { it.copy(
                        details = data
                    ) }
                }
            }
        }
    }
}