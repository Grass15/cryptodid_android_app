package com.learning.walletv21.presentation.authentication.register.mock_template

import androidx.lifecycle.ViewModel
import com.learning.walletv21.presentation.authentication.register.RegisterEvents

abstract class RegisterScreenViewModelBase: ViewModel() {
    abstract fun resetStatus()
    abstract fun onRegisterEvent(event: RegisterEvents)
}