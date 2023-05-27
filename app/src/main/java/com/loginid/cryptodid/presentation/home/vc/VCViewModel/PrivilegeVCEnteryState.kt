package com.loginid.cryptodid.presentation.home.vc.VCViewModel

import com.loginid.cryptodid.data.local.entity.VCType
import com.loginid.cryptodid.utils.Status
import java.util.*

data class PrivilegeVCEnteryState(
    val VCTypeEnum: VCType? = VCType.DEFAULT,
    val experationDate: Date? = null,
    val issuerName: String="Linquit",
    val VCTypeText: String = "Persenal document",
    val VCTitle: String = "Age",
    val VCContentOverview: String="Age > 18",
    val VCAttribute: List<Int> = listOf(1,1,1),
    //val VCId: String,
    val status: Status = Status.NO_ACTION,
)

