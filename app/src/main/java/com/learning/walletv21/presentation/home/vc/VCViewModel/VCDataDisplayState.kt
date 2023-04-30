package com.learning.walletv21.presentation.home.vc.VCViewModel

import com.learning.walletv21.core.protocols.javamodels.Claim
import com.learning.walletv21.utils.Status
import java.util.Date

data class VCDataDisplayState(
    val experationDate: Date? = null,//Date(),
    val issuerName: String,
    val VCType: String ,
    val VCTitle: String,
    val VCContentOverview: String,
    val status: Status=Status.NO_ACTION,
    val VCID: String,
    val rawVC: Claim? = null
) {
}