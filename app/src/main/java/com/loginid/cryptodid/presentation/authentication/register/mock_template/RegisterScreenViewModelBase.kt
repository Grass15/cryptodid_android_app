package com.loginid.cryptodid.presentation.authentication.register.mock_template

import androidx.lifecycle.ViewModel
import com.loginid.cryptodid.presentation.authentication.register.RegisterEvents

abstract class RegisterScreenViewModelBase: ViewModel() {
    abstract fun resetStatus()
    abstract fun onRegisterEvent(event: RegisterEvents)
}