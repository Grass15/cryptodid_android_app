package com.learning.walletv21.presentation.authentication.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.learning.walletv21.data.local.entity.UserEntity
import com.learning.walletv21.domain.repository.UserRepository
import com.learning.walletv21.domain.use_case.authentication.RegisterUseCase
import com.learning.walletv21.presentation.authentication.login.Status
import com.learning.walletv21.presentation.authentication.register.mock_template.RegisterScreenViewModelBase
import com.learning.walletv21.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import java.util.UUID



@HiltViewModel
class RegisterScreenViewModel @Inject constructor(
     private val registerUseCase: RegisterUseCase,
     private val repository: UserRepository
) : RegisterScreenViewModelBase(){

     //userId = UUID.randomUUID().toString()
     private val _state = MutableStateFlow(RegisterDataState())
     private val _status = MutableStateFlow(Status.NO_ACTION)

     val state = combine(_state,_status){state,status ->
           state.copy(
                status = status
           )
     }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000),RegisterDataState())

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

                    _state.update { it.copy(
                         userId = UUID.randomUUID().toString()
                    ) }

                    registerUseCase(_state.value.toUserEntity()).onEach { result ->
                         when(result){
                              is Resource.Error -> {
                                   _status.value = Status.ERROR
                              }
                              is Resource.Loading -> {
                                   _status.value = Status.LOADING
                              }
                              is Resource.Success -> {
                                   _status.value = Status.SUCCESS
                              }
                         }
                    }.launchIn(viewModelScope)
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
               RegisterEvents.DemoRegister -> {
                    viewModelScope.launch {
                         repository.insertUser(
                              UserEntity(
                              userId = UUID.randomUUID().toString(),
                              username = "user_good",
                              password = "pass_good",
                              phone = "none",
                              address = "none",
                              firstname = "zim",
                              lastname = "harish"
                         )
                         )

                    }
               }
          }
     }

}