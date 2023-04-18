package com.learning.walletv21.presentation.authentication.login.mock_template

import androidx.lifecycle.ViewModel
import com.learning.walletv21.presentation.authentication.login.LoginEvents

abstract class LoginScreenViewModelBase : ViewModel() {
    abstract fun onLoginEvent(events: LoginEvents)
    abstract fun reSetStatus()
}