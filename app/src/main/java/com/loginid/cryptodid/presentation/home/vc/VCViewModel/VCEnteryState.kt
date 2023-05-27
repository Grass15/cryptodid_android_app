package com.loginid.cryptodid.presentation.home.vc.VCViewModel

import com.loginid.cryptodid.utils.Status
import java.util.*
import com.loginid.cryptodid.data.local.entity.VCType
data class VCEnteryState(
    val VCTypeEnum: VCType? = VCType.DEFAULT,
    val experationDate: Date? = null,
    val issuerName: String="Linquit",
    val VCTypeText: String = "Persenal document",
    val VCTitle: String = "Age",
    val VCContentOverview: String="Age > 18",
    val VCAttribute: Int = 0,
    //val VCId: String,
    val status: Status=Status.NO_ACTION,
    ) {
}