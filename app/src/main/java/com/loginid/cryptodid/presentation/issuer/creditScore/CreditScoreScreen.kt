package com.loginid.cryptodid.presentation.issuer.creditScore

import android.content.Context
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import androidx.appcompat.app.AlertDialog
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.loginid.cryptodid.presentation.home.vc.VCViewModel.VCEnteryState
import com.loginid.cryptodid.presentation.home.vc.VCViewModel.VCViewModel
import com.loginid.cryptodid.presentation.issuer.creditScore.requests.ApiServiceTransUnion
import com.loginid.cryptodid.presentation.issuer.creditScore.tokenrequest.AccessTokenRequest
import com.loginid.cryptodid.presentation.issuer.creditScore.tokenrequest.AccessTokenResponse
import com.loginid.cryptodid.presentation.issuer.creditScore.tokenrequest.ApiService
import com.loginid.cryptodid.presentation.issuer.creditScore.tokenrequest.ApiServiceClient
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.OkHttpClient.Builder
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.*
import java.util.concurrent.TimeUnit









@Composable
fun CreditScoreScreen() {
    Text(text = "This is Credit Score")
    val policy = ThreadPolicy.Builder().permitAll().build()
    StrictMode.setThreadPolicy(policy)

    var apiService: ApiServiceTransUnion? = null
    var vcViewModel: VCViewModel = hiltViewModel()

    var context = LocalContext.current


    val getfirstName: String = "KATHY"
    val getlastName: String = "BETHEL"
    val getcity: String = "MONROEVILLE"
    val getStreetName: String = "OLD CONCORD"
    val getStreetNum: String = "1240"
    val getPostalCode: String = "15146"
    val getTaxId: String = "666791984"

    var fullName = ""
    var score = ""

    var source = ""
    var uid = ""


//    getWindow().setFlags(
//        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
//        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
//    )
//    StartPrograss()

    val postData = """{
                      "PersonInfo": {
                        "PersonName": {
                          "FirstName": "$getfirstName",
                          "LastName": "$getlastName",
                          "MiddleName": "J"
                        },
                        "ContactInfo": {
                          "PostAddr": {
                            "StreetNum": "$getStreetNum",
                            "StreetName": "$getStreetName",
                            "City": "$getcity",
                            "StateProv": "PA",
                            "PostalCode": "$getPostalCode"
                          }
                        },
                        "TINInfo": {
                          "TINType": "1",
                          "TaxId": "$getTaxId"
                        }
                      }
                }"""

    //your bearer token

    //your bearer token
    var bearerToken = ""
    val CLIENT_ID = "8BtwPBcFW00X9cO2OigCBiDJArAkIVUU"
    val CLIENT_SECRET = "pO6SVWztu95iziAh"

    // Create the access token request object

    // Create the access token request object
    val request = AccessTokenRequest(CLIENT_ID, CLIENT_SECRET, "client_credentials")

    // Call the API endpoint with Retrofit

    // Call the API endpoint with Retrofit
    val apiServiceToken: ApiService = ApiServiceClient.getClient()
    val call: Call<AccessTokenResponse> = apiServiceToken.getAccessToken(request)

    bearerToken = try {
        val response: Response<AccessTokenResponse> = call.execute()
        if (response.isSuccessful()) {
            val accessTokenResponse: AccessTokenResponse = response.body()!!
            accessTokenResponse.getAccess_token()
        } else {
            println("Error: " + response.code())
            noGetToken(context)
            return
        }
    } catch (e: IOException) {
        println("Error: " + e.message)
        noGetToken(context)
        return
    }

    // Create a Retrofit instance with the desired base URL and a Gson converter factory

    // Create a Retrofit instance with the desired base URL and a Gson converter factory
    val client: OkHttpClient = Builder()
        .connectTimeout(100, TimeUnit.SECONDS)
        .readTimeout(100, TimeUnit.SECONDS).build()
    val retrofit = Retrofit.Builder()
        .baseUrl("https://apitest.microbilt.com/").client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    // Create an instance of your API interface using the Retrofit instance

    // Create an instance of your API interface using the Retrofit instance
    apiService = retrofit.create(ApiServiceTransUnion::class.java)

    // Create a RequestBody object to hold the data you want to send in your POST request

    // Create a RequestBody object to hold the data you want to send in your POST request
    val requestBody = RequestBody.create("application/json".toMediaTypeOrNull(), postData)



    // Call your API interface method with the bearer token and the request body as parameters, and use `enqueue()` to execute the request asynchronously


    // Call your API interface method with the bearer token and the request body as parameters, and use `enqueue()` to execute the request asynchronously
    apiService.postData("Bearer $bearerToken", requestBody)?.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    try {
                        // Convert the response body to a String
                        val responseBodyString = response.body()!!.string()

                        // Parse the response body String into a JSON object
                        val json = JSONObject(responseBodyString)
                        uid = "" + json.getJSONObject("MsgRsHdr").getString("RqUID")
                        source = "" + json.getJSONArray("Subject").getJSONObject(0)
                            .getJSONObject("PersonInfo").getString("Source")
                        fullName = json.getJSONArray("Subject").getJSONObject(0)
                            .getJSONObject("PersonInfo").getJSONObject("PersonName")
                            .getString("FirstName") + " " + json.getJSONArray("Subject")
                            .getJSONObject(0).getJSONObject("PersonInfo")
                            .getJSONObject("PersonName").getString("LastName")
                        score =
                            "" + json.getJSONArray("Subject").getJSONObject(0).getJSONArray("Score")
                                .getJSONObject(0).getLong("Value")
                        // Print the JSON object to the console
                        println("Response: $json")
//                        StopPrograss()
//                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                        Check(fullName, score, uid, source, vcViewModel, context)
                    } catch (e: JSONException) {
                        // Handle the JSON parsing error
                        e.printStackTrace()
                        val fullName = ""
                        val score = ""
                        val source = ""
                        val uid = ""
                        Check(fullName, score, uid, source, vcViewModel, context)
                    } catch (e: IOException) {
                        // Handle the IO error
                        e.printStackTrace()
                        val fullName = ""
                        val score = ""
                        val source = ""
                        val uid = ""
                        Check(fullName, score, uid, source, vcViewModel, context)
                    }
                } else {
                    println("ELSE -> $response")
                    val fullName = ""
                    val score = ""
                    val source = ""
                    val uid = ""
                    Check(fullName, score, uid, source, vcViewModel, context)
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                println("ERROR -> $t")
                val fullName = ""
                val score = ""
                val source = ""
                val uid = ""
                Check(fullName, score, uid, source, vcViewModel, context)
            }
        })

}

private fun allFieldRequired(context: Context) {
    val builder = AlertDialog.Builder(context)
    builder.setMessage("Please, all fields are required !")
        .setCancelable(false)
        .setPositiveButton(
            "OK"
        ) { dialog, id -> dialog.dismiss() }
    val alert = builder.create()
    alert.show()
}

private fun Check(fullName: String, score: String, uid: String, source: String, vcViewModel: VCViewModel, context: Context) {
    val builder = AlertDialog.Builder(context)
    if (fullName !== "" && score !== "" && uid !== "" && source !== "") {
        builder.setMessage("Full Name : $fullName\nScore : $score\nSource : $source")
            .setCancelable(false)
            .setPositiveButton("OK") { dialog, id ->
                dialog.dismiss()
                //finish()
            }
//        val alert = builder.create()
//        alert.show()
        println("Full Name : $fullName\nScore : $score\nSource : $source")
        //val intent: Intent = getIntent()
        //With big numbers like 700 the encryption bug, so we divide by ten to be good
        //setResult(Activity.RESULT_OK, intent)
        vcViewModel.saveVC(
            VCEnteryState(
                experationDate = Date(),
                issuerName = "Trans Union",
                VCType = "CreditScore Points",
                VCTitle = "Credit Score",
                VCContentOverview = "+700",
                VCAttribute = score.toInt()
            )
        )
    }
    if (fullName === "" && score === "" && uid === "" && source === "") {
        builder.setMessage("No Score found ! Please Retry !!")
            .setCancelable(false)
            .setPositiveButton("OK") { dialog, id ->
                dialog.dismiss()
                //showMainActivity()
            }
        val alert = builder.create()
        alert.show()
    }
}

private fun noGetToken(context: Context) {
    val builder = AlertDialog.Builder(context)
    builder.setMessage("Can't get Token Acces ! Please Retry !!")
        .setCancelable(false)
        .setPositiveButton("OK") { dialog, id ->
            dialog.dismiss()
            //showMainActivity()
        }
    val alert = builder.create()
    alert.show()
}

//private fun StartPrograss() {
//    progressBar.setVisibility(View.VISIBLE)
//}
//
//private fun StopPrograss() {
//    progressBar.setVisibility(View.INVISIBLE)
//}

//private fun showMainActivity() {
//    val intent = Intent(this, CreditScoreActivity::class.java)
//    startActivity(intent)
//    finish()
//}
