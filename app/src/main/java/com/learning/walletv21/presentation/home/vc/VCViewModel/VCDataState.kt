package com.learning.walletv21.presentation.home.vc.VCViewModel

import com.learning.walletv21.utils.Status
import java.util.Date

data class VCDataState(
    val experationDate: Date? = Date(),
    val issuerName: String,
    val VCType: String ,
    val VCTitle: String,
    val VCContentOverview: String,
    val status: Status=Status.NO_ACTION
) {
}