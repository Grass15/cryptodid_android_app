package com.loginid.cryptodid.presentation.issuer.bank.network;

import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response


import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object AccountRequester {
    val retrofit = Retrofit.Builder()
        .baseUrl("https://sandbox.plaid.com")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val accountsApi = retrofit.create(AccountsApi::class.java)

    val request = Request(
        "63dec92bcf1981001288b438",
        "b8e1156221d8433d080068c9ccc200",
        "access-sandbox-3dcbad86-8f4c-44bd-beab-98b40f889690"
    )

    val call = accountsApi.getAccounts(request)

    fun getAccounts(callback: retrofit2.Callback<ResponseClass>) {
        call.enqueue(callback)
    }


}


