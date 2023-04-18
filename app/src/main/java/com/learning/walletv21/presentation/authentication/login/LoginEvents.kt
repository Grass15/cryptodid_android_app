package com.learning.walletv21.presentation.authentication.login

sealed interface LoginEvents {
    data class SetUsername(val username: String): LoginEvents
    data class SetPassword(val password: String): LoginEvents
    object Login: LoginEvents
    object DemoData: LoginEvents
}