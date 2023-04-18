package com.learning.walletv21.presentation.loginid_welcome

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.learning.walletv21.data.repository.DataStoreRepository
import com.learning.walletv21.presentation.navigation.screens.WelcomeScreen
import com.learning.walletv21.utils.Constants.AUTH_GRAPH
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


class SplashViewModel @Inject constructor(
    private val repository: DataStoreRepository
) : ViewModel(){

    private val _isLoading: MutableState<Boolean> = mutableStateOf(true)
    val isLoading: State<Boolean> = _isLoading

    private val _startDestination: MutableState<String> = mutableStateOf(WelcomeScreen .Welcome.route)
    val startDestination: State<String> = _startDestination

    init {
        viewModelScope.launch {
            repository.readOnBoardingState().collect { completed ->
                if (completed) {
                    _startDestination.value = AUTH_GRAPH //review here
                } else {
                    _startDestination.value = WelcomeScreen.Welcome.route
                }
            }
            _isLoading.value = false
        }
    }
}