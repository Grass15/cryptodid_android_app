package com.learning.walletv21.domain.repository

import com.learning.walletv21.data.remote.dto.ClaimDetailDto
import com.learning.walletv21.data.remote.dto.ClaimDto


interface ClaimRepository {

    suspend fun getClaims() : List<ClaimDto>

    suspend fun getClaimById(coinId: String): ClaimDetailDto
}