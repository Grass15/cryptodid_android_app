package com.loginid.cryptodid.presentation.authentication.login

data class LoginDataState(
    val username:String = "",
    val password:String= "",
    val status:Status=Status.LOADING
)

enum class Status{
    ERROR,
    SUCCESS,
    FAILLED,
    LOADING,
    NO_ACTION
}

/*  val firstname: String? = null,
    val lastname: String? = null,
    val phone: String? = null,
    val address: String? = null,

 */