package com.learning.walletv21.presentation.home.vc.VCViewModel

import com.learning.walletv21.utils.Status

data class VCActionState (
    val actionText:String = "NO TEXT RETURNED",
    val status : Status = Status.NO_ACTION
)