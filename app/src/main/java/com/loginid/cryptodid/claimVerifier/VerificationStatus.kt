package com.loginid.cryptodid.claimVerifier

import com.loginid.cryptodid.utils.Status

data class VerificationStatus(
    val vMessage: String = "",
    val vStatus: Status = Status.NO_ACTION
) {

}