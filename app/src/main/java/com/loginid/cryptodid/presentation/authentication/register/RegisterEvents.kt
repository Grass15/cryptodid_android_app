package com.loginid.cryptodid.presentation.authentication.register

sealed interface RegisterEvents {
    data class SetFirstName(val firstname: String): RegisterEvents
    data class SetLastName(val lastname: String): RegisterEvents
    data class SetEmailName(val email: String): RegisterEvents //means username for the moment
    data class SetPassword(val password: String): RegisterEvents
    data class SetRePassword(val repassword: String): RegisterEvents

    object Register: RegisterEvents
    object DemoRegister: RegisterEvents
}