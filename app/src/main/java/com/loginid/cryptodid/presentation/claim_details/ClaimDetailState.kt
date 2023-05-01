package com.loginid.cryptodid.presentation.claim_details

import com.loginid.cryptodid.domain.model.ClaimDetailsModel

data class ClaimDetailState(
    val isLoading: Boolean = false,
    val claim: ClaimDetailsModel? = null,
    val error: String = ""
)
