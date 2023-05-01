package com.loginid.cryptodid.presentation.authentication.login.mock_template

import androidx.lifecycle.ViewModel
import com.loginid.cryptodid.presentation.authentication.login.LoginEvents

abstract class LoginScreenViewModelBase : ViewModel() {
    abstract fun onLoginEvent(events: LoginEvents)
    abstract fun reSetStatus()
}