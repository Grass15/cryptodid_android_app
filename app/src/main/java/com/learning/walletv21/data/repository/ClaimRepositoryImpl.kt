package com.learning.walletv21.data.repository

import com.learning.walletv21.data.remote.IssuerApi
import com.learning.walletv21.data.remote.dto.ClaimDetailDto
import com.learning.walletv21.data.remote.dto.ClaimDto
import com.learning.walletv21.domain.repository.ClaimRepository
import javax.inject.Inject

class ClaimRepositoryImpl @Inject constructor(
    private val api: IssuerApi
) : ClaimRepository{
    override suspend fun getClaims(): List<ClaimDto> {
        return api.getClaims()
    }

    override suspend fun getClaimById(coinId: String): ClaimDetailDto {
        return api.getClaimById(coinId)
    }
}