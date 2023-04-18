package com.learning.walletv21.presentation.claim_details

import com.learning.walletv21.domain.model.ClaimDetailsModel
import com.learning.walletv21.domain.model.ClaimModel

data class ClaimDetailState(
    val isLoading: Boolean = false,
    val claim: ClaimDetailsModel? = null,
    val error: String = ""
)
