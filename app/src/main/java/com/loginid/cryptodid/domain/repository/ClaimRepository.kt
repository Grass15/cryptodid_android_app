package com.loginid.cryptodid.domain.repository

import com.loginid.cryptodid.data.remote.dto.ClaimDetailDto
import com.loginid.cryptodid.data.remote.dto.ClaimDto


interface ClaimRepository {

    suspend fun getClaims() : List<ClaimDto>

    suspend fun getClaimById(coinId: String): ClaimDetailDto
}