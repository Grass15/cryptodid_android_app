package com.learning.walletv21.presentation.home.vc.VCViewModel

import com.learning.walletv21.utils.Status
import java.util.*

data class VCEnteryState(
    val experationDate: Date? = null,
    val issuerName: String="Linquit",
    val VCType: String = "Persenal document",
    val VCTitle: String = "Age",
    val VCContentOverview: String="Age > 18",
    //val VCId: String,
    val status: Status=Status.NO_ACTION
) {
}