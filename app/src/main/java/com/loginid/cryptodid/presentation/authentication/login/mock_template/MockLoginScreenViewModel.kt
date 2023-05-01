package com.loginid.cryptodid.presentation.authentication.login.mock_template

import androidx.lifecycle.viewModelScope
import com.loginid.cryptodid.presentation.authentication.login.LoginDataState
import com.loginid.cryptodid.presentation.authentication.login.LoginEvents
import com.loginid.cryptodid.presentation.authentication.login.Status
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch


class MockLoginScreenViewModel : LoginScreenViewModelBase() {
    private val _state = MutableStateFlow(LoginDataState())
    private val _status = MutableStateFlow(Status.NO_ACTION)
    val state = combine(_state,_status){state,status ->
        state.copy(
            status = status
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), LoginDataState())

    override fun reSetStatus(){
        _status.value = Status.NO_ACTION
    }
    override fun onLoginEvent(event: LoginEvents){
        when(event){
            LoginEvents.Login -> {
                val username = state.value.username
                val password = state.value.password
                if(username.isBlank() || password.isBlank() ){
                    return
                }
                // Mock implementation of loginUseCase
                viewModelScope.launch {
                    delay(1000) // Simulate network delay
                    _status.value = when (username) {
                        "admin" -> Status.SUCCESS
                        else -> Status.FAILLED
                    }
                }
            }
            is LoginEvents.SetPassword -> {
                _state.update { it.copy(
                    password = event.password
                ) }
            }
            is LoginEvents.SetUsername -> {
                _state.update { it.copy(
                    username = event.username
                ) }
            }
            LoginEvents.DemoData -> {
                // Mock implementation of repository insertUser
                viewModelScope.launch {
                    delay(1000) // Simulate database delay
                    // do nothing
                }
            }
        }
    }
}
