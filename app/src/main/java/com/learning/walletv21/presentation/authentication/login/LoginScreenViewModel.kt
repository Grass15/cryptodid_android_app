package com.learning.walletv21.presentation.authentication.login

import androidx.lifecycle.viewModelScope
import com.learning.walletv21.data.local.entity.UserEntity
import com.learning.walletv21.domain.repository.UserRepository
import com.learning.walletv21.domain.use_case.authentication.LoginUseCase
import com.learning.walletv21.presentation.authentication.login.mock_template.LoginScreenViewModelBase
import com.learning.walletv21.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject



@HiltViewModel
class LoginScreenViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val repository: UserRepository
) : LoginScreenViewModelBase() {
    private val _state = MutableStateFlow(LoginDataState())
    private val _status = MutableStateFlow(Status.NO_ACTION)
    val state = combine(_state,_status){state,status ->
        state.copy(
            status = status
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000),LoginDataState())

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
               loginUseCase(username,password).onEach {result ->
                   when(result){
                       is Resource.Error -> {
                            _status.value = Status.ERROR
                       }
                       is Resource.Loading -> {
                           _status.value = Status.LOADING
                       }
                       is Resource.Success -> {
                           when(result.data){
                               true -> _status.value = Status.SUCCESS
                               false -> _status.value = Status.FAILLED
                           }

                       }
                   }
               }.launchIn(viewModelScope)
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
               viewModelScope.launch {
                   repository.insertUser(UserEntity(
                       userId ="10dezez",
                       username = "user_good",
                       password = "pass_good",
                       phone = "none",
                       address = "none",
                       firstname = "zim",
                       lastname = "harish"
                   ))

               }
            }
        }
    }

}
