package com.learning.walletv21.presentation.claim_list

import com.learning.walletv21.domain.model.ClaimModel

data class ClaimListState(
    val isLoading: Boolean = false,
    val claims: List<ClaimModel> = emptyList(),
    val error: String = ""
)
