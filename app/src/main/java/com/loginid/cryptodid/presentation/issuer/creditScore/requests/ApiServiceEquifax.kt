package com.loginid.cryptodid.presentation.issuer.creditScore.requests

import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST


interface ApiServiceEquifax {
    @POST("Equifax/GetReport")
    fun postData(
        @Header("Authorization") authToken: String?,
        @Body requestBody: RequestBody?
    ): Call<ResponseBody?>?
}