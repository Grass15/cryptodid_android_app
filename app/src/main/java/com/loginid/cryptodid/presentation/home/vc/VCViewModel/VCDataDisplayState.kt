package com.loginid.cryptodid.presentation.home.vc.VCViewModel

import com.loginid.cryptodid.data.local.entity.VCType
import com.loginid.cryptodid.model.Claim
import com.loginid.cryptodid.utils.Status
import java.util.Date

data class VCDataDisplayState(
    val experationDate: Date? = null, //Date(),
    val issuingDate: Date?=null,
    val issuerName: String,
    val VCTypeText: String,
    val VCTypeEnum:VCType?=VCType.DEFAULT,
    val VCTitle: String,
    val VCContentOverview: String,
    val status: Status=Status.NO_ACTION,
    val VCID: String,
    val rawVC: Claim? = null
) {
}