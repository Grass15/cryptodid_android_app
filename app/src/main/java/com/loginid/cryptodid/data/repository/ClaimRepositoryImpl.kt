package com.loginid.cryptodid.data.repository

import com.loginid.cryptodid.data.remote.IssuerApi
import com.loginid.cryptodid.data.remote.dto.ClaimDetailDto
import com.loginid.cryptodid.data.remote.dto.ClaimDto
import com.loginid.cryptodid.domain.repository.ClaimRepository
import javax.inject.Inject

class ClaimRepositoryImpl @Inject constructor(
    private val api: IssuerApi
) : ClaimRepository{
    override suspend fun getClaims(): List<ClaimDto> {
        return api.getVCs()
    }

    override suspend fun getClaimById(coinId: String): ClaimDetailDto {
        return api.getClaimById(coinId)
    }
}