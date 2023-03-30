package com.loginid.cryptodid.claimFetcher.plaid.network

import android.content.ClipData
import com.google.gson.annotations.SerializedName
import io.reactivex.rxjava3.core.Single
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

/**
 * API calls to our localhost token server.
 */
interface LinkSampleApi {

  @POST("/api/create_link_token")
  fun getLinkToken(): Single<LinkToken>
}

interface AccountsApi {
  @POST("/identity/get")
  @Headers("Content-Type: application/json")
  fun getAccounts(@Body request: Request): Call<ResponseClass>
}

data class Request(
  val client_id: String,
  val secret: String,
  val access_token: String
)

data class ResponseClass(
  val accounts: List<Account>,
  val item: Item,
  val request_id: String
)

data class Account(
  val account_id: String,
  val balances: Balances,
  val mask: String,
  val name: String,
  val official_name: String,
  val owners: List<Owner>,
  val subtype: String,
  val type: String
)

data class Owner(
  val addresses: List<Address>,
  val emails: List<Email>,
  val names: List<String>,
  val phone_numbers: List<PhoneNumber>
)

data class Address(
  val data: AddressData,
  val primary: Boolean
)

data class AddressData(
  val city: String,
  val country: String,
  val postal_code: String,
  val region: String,
  val street: String
)

data class Email(
  val data: String,
  val primary: Boolean,
  val type: String
)

data class PhoneNumber(
  val data: String,
  val primary: Boolean,
  val type: String
)

data class Item(
  val available_products: List<String>,
  val billed_products: List<String>,
  val consent_expiration_time: Any?,
  val error: Any?,
  val institution_id: String,
  val item_id: String,
  val optional_products: Any?,
  val products: List<String>,
  val update_type: String,
  val webhook: String
)

data class Balances(
  val available: Int,
  val current: Int,
  val iso_currency_code: String,
  val limit: Int?,
  val unofficial_currency_code: String?
)

data class LinkToken(
  @SerializedName("link_token") val link_token: String
)

