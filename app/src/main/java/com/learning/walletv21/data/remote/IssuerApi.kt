package com.learning.walletv21.data.remote

import com.learning.walletv21.data.remote.dto.ClaimDetailDto
import com.learning.walletv21.data.remote.dto.ClaimDto
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Main interface, holds all mothods we need to fetch a vc
 */
//AKA CoinPaprikaApi
interface IssuerApi {
    @GET("/v1/coins")
    suspend fun getClaims() : List<ClaimDto>

    @GET("/v1/coins/{coinId}")
    suspend fun getClaimById(@Path("coinId") coinId: String): ClaimDetailDto
}