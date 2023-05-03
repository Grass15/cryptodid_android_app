package com.loginid.cryptodid.presentation.loginid_welcome

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.loginid.cryptodid.data.repository.DataStoreRepository
import com.loginid.cryptodid.presentation.navigation.screens.WelcomeScreen
import com.loginid.cryptodid.utils.Constants.AUTH_GRAPH
import kotlinx.coroutines.launch
import javax.inject.Inject


class SplashViewModel @Inject constructor(
    private val repository: DataStoreRepository
) : ViewModel(){

    private val _isLoading: MutableState<Boolean> = mutableStateOf(true)
    val isLoading: State<Boolean> = _isLoading

    private val _startDestination: MutableState<String> = mutableStateOf(AUTH_GRAPH)
    val startDestination: State<String> = _startDestination

    init {
        viewModelScope.launch {
            repository.readOnBoardingState().collect { completed ->
                if (completed) {
                    _isLoading.value = false
                    _startDestination.value = AUTH_GRAPH //review here
                } else {
                    _isLoading.value = false
                    _startDestination.value = WelcomeScreen.Welcome.route
                }
            }

        }
    }

}