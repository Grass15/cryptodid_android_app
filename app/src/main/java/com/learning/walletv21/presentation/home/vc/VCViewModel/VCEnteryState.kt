package com.learning.walletv21.presentation.home.vc.VCViewModel

import com.learning.walletv21.utils.Status

data class VCEnteryState(
    val experationDate: String="18-12-200",
    val issuerName: String="Linquit",
    val VCType: String = "Persenal document",
    val VCTitle: String = "Age",
    val VCContentOverview: String="Age > 18",
    val VCId: Int,
    val status: Status=Status.NO_ACTION
) {
}