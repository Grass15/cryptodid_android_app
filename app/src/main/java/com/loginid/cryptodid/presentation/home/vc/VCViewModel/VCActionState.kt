package com.loginid.cryptodid.presentation.home.vc.VCViewModel

import com.loginid.cryptodid.utils.Status

data class VCActionState (
    val actionText:String = "NO TEXT RETURNED",
    val status : Status = Status.NO_ACTION
)