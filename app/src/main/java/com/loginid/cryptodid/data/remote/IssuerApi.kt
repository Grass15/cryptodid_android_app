package com.loginid.cryptodid.data.remote

import com.loginid.cryptodid.data.remote.dto.ClaimDetailDto
import com.loginid.cryptodid.data.remote.dto.ClaimDto
import com.loginid.cryptodid.data.remote.vcdto.VCDto
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Main interface, holds all mothods we need to fetch a vc
 */
//AKA CoinPaprikaApi
interface IssuerApi {
    /**
     * it is the main function in our interface, we just got to pass
     * the type of the vc then the provider we will use
     * @param VCType the type of the vc (age, credit score, bank ...)
     * @param VCProvider the source that will provider us with the vc
     * @return VCDto the model of the returned data, will be later transformed and normalized
     * so we can present it in our view
     */
    @GET("/v1/{VCType}/{VCProvider}")
    suspend fun getVC(@Path("VCType") VCType: String, @Path("VCProvider") VCProvider: String): VCDto

}