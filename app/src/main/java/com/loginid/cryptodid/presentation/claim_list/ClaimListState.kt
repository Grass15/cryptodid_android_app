package com.loginid.cryptodid.presentation.claim_list

import com.loginid.cryptodid.domain.model.ClaimModel

data class ClaimListState(
    val isLoading: Boolean = false,
    val claims: List<ClaimModel> = emptyList(),
    val error: String = ""
)
