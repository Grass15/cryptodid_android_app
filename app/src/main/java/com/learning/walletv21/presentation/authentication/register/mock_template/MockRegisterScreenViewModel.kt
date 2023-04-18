package com.learning.walletv21.presentation.authentication.register.mock_template

import androidx.lifecycle.viewModelScope
import com.learning.walletv21.presentation.authentication.login.Status
import com.learning.walletv21.presentation.authentication.register.RegisterDataState
import com.learning.walletv21.presentation.authentication.register.RegisterEvents
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MockRegisterScreenViewModel: RegisterScreenViewModelBase() {
    //userId = UUID.randomUUID().toString()
    private val _state = MutableStateFlow(RegisterDataState())
    private val _status = MutableStateFlow(Status.NO_ACTION)

    val state = combine(_state,_status){state,status ->
        state.copy(
            status = status
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), RegisterDataState())

    override fun resetStatus(){
        _status.value = Status.NO_ACTION
    }

   override fun onRegisterEvent(event: RegisterEvents){
        when(event){
            RegisterEvents.Register -> {
                val username = state.value.username
                val firstName = state.value.firstname
                val lastName = state.value.lastname
                val password = state.value.password
                val repassword = state.value.repassword

                if(username.isBlank() || firstName.isBlank() || lastName.isBlank() || password.isBlank() || repassword.isBlank()){
                    return
                }
                if(password != repassword) return

                viewModelScope.launch {
                    delay(1000)
                    _status.value = Status.NO_ACTION
                }
            }

            is RegisterEvents.SetEmailName -> {
                _state.update {it.copy(
                    username = event.email
                )
                }
            }
            is RegisterEvents.SetFirstName -> {
                _state.update {it.copy(
                    firstname = event.firstname
                )
                }
            }
            is RegisterEvents.SetLastName -> {
                _state.update {it.copy(
                    lastname = event.lastname
                )
                }
            }
            is RegisterEvents.SetPassword -> {
                _state.update {it.copy(
                    password = event.password
                )
                }
            }
            is RegisterEvents.SetRePassword -> {
                _state.update {it.copy(
                    repassword =  event.repassword
                )
                }
            }
        }
    }

}