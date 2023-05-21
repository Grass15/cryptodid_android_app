package com.loginid.cryptodid.presentation.issuer.creditScore

import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.loginid.cryptodid.R
import com.loginid.cryptodid.presentation.home.vc.VCViewModel.VCEnteryState
import com.loginid.cryptodid.presentation.home.vc.VCViewModel.VCViewModel
import com.loginid.cryptodid.presentation.issuer.creditScore.requests.ApiServiceTransUnion
import com.loginid.cryptodid.presentation.issuer.creditScore.tokenrequest.AccessTokenRequest
import com.loginid.cryptodid.presentation.issuer.creditScore.tokenrequest.AccessTokenResponse
import com.loginid.cryptodid.presentation.issuer.creditScore.tokenrequest.ApiService
import com.loginid.cryptodid.presentation.issuer.creditScore.tokenrequest.ApiServiceClient
import com.loginid.cryptodid.presentation.theme.firstBackGroundColor
import com.loginid.cryptodid.presentation.theme.inputTextColor
import com.loginid.cryptodid.presentation.theme.secondBackGroundColor
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.OkHttpClient.Builder
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.*
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun CreditScoreScreen(navController: NavController) {
    Text(text = "This is Credit Score")
    var firstName: String = "KATHY"
    val lastName: String = "BETHEL"
    val city: String = "MONROEVILLE"
    val streetName: String = "OLD CONCORD"
    val streetNum: String = "1240"
    val postalCode: String = "15146"
    val taxId: String = "666791984"
    var creditScore by remember { mutableStateOf("") }
    val transUnionImg = painterResource(id = R.drawable.transunion)
    var showProgress = false
    val policy = ThreadPolicy.Builder().permitAll().build()
    StrictMode.setThreadPolicy(policy)

    var vcViewModel: VCViewModel = hiltViewModel()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.firstBackGroundColor)
            .padding(1.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,

        ) {

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.firstBackGroundColor)
                .weight(2f)
                .padding(10.dp),
            contentAlignment = Alignment.Center

        ) {
            //The login Image goes here
            Image(painter = transUnionImg, contentDescription = "Trans Union Img")

        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .weight(6f)
                .padding(2.dp),
            contentAlignment = Alignment.TopCenter

        ) {

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp))
                    .background(MaterialTheme.colors.secondBackGroundColor)
                    .padding(15.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    Text(
                        text = "Trans Union",
                        color = MaterialTheme.colors.primary,
                        fontStyle = MaterialTheme.typography.h3.fontStyle,
                        fontSize = MaterialTheme.typography.h3.fontSize,
                        fontWeight = FontWeight.Bold,
                    )
                    Spacer(modifier = Modifier.size(45.dp))
                    //TextFields

                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .onFocusChanged { focusState ->
                                if (!focusState.isFocused) {
                                    Log.d("focus", "lost focus")
                                }
                            },
                        label = {
                            Text(text = "First Name")
                        },
                        textStyle = TextStyle(color = MaterialTheme.colors.inputTextColor),
                        leadingIcon = {
                            IconButton(
                                onClick = {}
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Person,
                                    contentDescription = "Person ICon"
                                )
                            }
                        },
                        value = firstName,
                        onValueChange = {
                            // firstName = it
                        },
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Done,
                            keyboardType = KeyboardType.Email,
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {  },
                        ),

                        )
                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .onFocusChanged { focusState ->
                                if (!focusState.isFocused) {
                                    Log.d("focus", "lost focus")
                                }
                            },
                        label = {
                            Text(text = "Last Name")
                        },
                        textStyle = TextStyle(color = MaterialTheme.colors.inputTextColor),
                        leadingIcon = {
                            IconButton(
                                onClick = {}
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Person,
                                    contentDescription = "Person ICon"
                                )
                            }
                        },
                        value = lastName,
                        onValueChange = {
                            // firstName = it
                        },
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Done,
                            keyboardType = KeyboardType.Email,
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {  },
                        ),

                        )
                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .onFocusChanged { focusState ->
                                if (!focusState.isFocused) {
                                    Log.d("focus", "lost focus")
                                }
                            },
                        label = {
                            Text(text = "City")
                        },
                        textStyle = TextStyle(color = MaterialTheme.colors.inputTextColor),
                        leadingIcon = {
                            IconButton(
                                onClick = {}
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.LocationCity,
                                    contentDescription = "City ICon"
                                )
                            }
                        },
                        value = city,
                        onValueChange = {
                            // firstName = it
                        },
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Done,
                            keyboardType = KeyboardType.Email,
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {  },
                        ),

                        )
                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .onFocusChanged { focusState ->
                                if (!focusState.isFocused) {
                                    Log.d("focus", "lost focus")
                                }
                            },
                        label = {
                            Text(text = "Postal Code")
                        },
                        textStyle = TextStyle(color = MaterialTheme.colors.inputTextColor),
                        leadingIcon = {
                            IconButton(
                                onClick = {}
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Email,
                                    contentDescription = "Postal ICon"
                                )
                            }
                        },
                        value = postalCode,
                        onValueChange = {
                            // firstName = it
                        },
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Done,
                            keyboardType = KeyboardType.Email,
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {  },
                        ),
                        )

                    Button(
                        onClick = {
                            //showProgress = true
                            val postData = getPostData(firstName = firstName, lastName = lastName,
                                streetNum = streetNum, streetName = streetName, city = city,
                                postalCode = postalCode, taxId = taxId)
                            //creditScore = getCreditScore(postData = postData)
                            getCreditScore(postData = postData, vcViewModel, navController)
                            //println(creditScore)
                            //showProgress = false
                        },
                        colors  = ButtonDefaults.buttonColors(
                            contentColor = Color.White
                        ),

                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = "Get Your Score")

                    }

                }
            } //lazy end
        }
    }


}

fun getPostData(firstName: String, lastName: String, streetNum: String, streetName: String,
                city: String, postalCode: String, taxId: String): String{
    return """{
              "PersonInfo": {
                "PersonName": {
                  "FirstName": "$firstName",
                  "LastName": "$lastName",
                  "MiddleName": "J"
                },
                "ContactInfo": {
                  "PostAddr": {
                    "StreetNum": "$streetNum",
                    "StreetName": "$streetName",
                    "City": "$city",
                    "StateProv": "PA",
                    "PostalCode": "$postalCode"
                  }
                },
                "TINInfo": {
                  "TINType": "1",
                  "TaxId": "$taxId"
                }
              }
        }"""

}


fun getCreditScore(postData: String, vcViewModel: VCViewModel, navController: NavController){
    var bearerToken = ""
    val CLIENT_ID = "8BtwPBcFW00X9cO2OigCBiDJArAkIVUU"
    val CLIENT_SECRET = "pO6SVWztu95iziAh"
    var apiService: ApiServiceTransUnion? = null
    var score = ""

    val request = AccessTokenRequest(CLIENT_ID, CLIENT_SECRET, "client_credentials")

    val apiServiceToken: ApiService = ApiServiceClient.getClient()
    val call: Call<AccessTokenResponse> = apiServiceToken.getAccessToken(request)

    bearerToken = try {
        val response: Response<AccessTokenResponse> = call.execute()
        if (response.isSuccessful()) {
            val accessTokenResponse: AccessTokenResponse = response.body()!!
            accessTokenResponse.getAccess_token()
        } else {
            println("Error: " + response.code())
            return
        }
    } catch (e: IOException) {
        println("Error: " + e.message)
        return
    }

    // Create a Retrofit instance with the desired base URL and a Gson converter factory
    val client: OkHttpClient = Builder()
        .connectTimeout(100, TimeUnit.SECONDS)
        .readTimeout(100, TimeUnit.SECONDS).build()
    val retrofit = Retrofit.Builder()
        .baseUrl("https://apitest.microbilt.com/").client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    // Create an instance of your API interface using the Retrofit instance
    apiService = retrofit.create(ApiServiceTransUnion::class.java)

    // Create a RequestBody object to hold the data you want to send in your POST request
    val requestBody = RequestBody.create("application/json".toMediaTypeOrNull(), postData)


    // Call your API interface method with the bearer token and the request body as parameters, and use `enqueue()` to execute the request asynchronously
    apiService.postData("Bearer $bearerToken", requestBody)?.enqueue(object : Callback<ResponseBody> {
        override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
            if (response.isSuccessful) {
                try {
                    val responseBodyString = response.body()?.string()
                    val json = JSONObject(responseBodyString.toString())
                    score =
                        "" + json.getJSONArray("Subject").getJSONObject(0).getJSONArray("Score")
                            .getJSONObject(0).getLong("Value")

                    println("Response: $json")

                    vcViewModel.saveVC(
                        VCEnteryState(
                            experationDate = Date(),
                            issuerName = "Trans Union",
                            VCType = "CreditScore",
                            VCTitle = "Credit Score",
                            VCContentOverview = "+700",
                            VCAttribute = score.toInt()
                        )
                    )
                    navController.popBackStack()

//                        StopPrograss()

                } catch (e: Exception) {
                    e.printStackTrace()
                    score = ""
                }

            } else {
                println("ELSE -> $response")

            }
            println("Countdown success")
        }

        override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
            println("ERROR -> $t")
            //resultWaiter.countDown()
            println("Countdown failure")
        }
    })
//    getScoreThread.start()
//    getScoreThread.join()



    println("return")
    println(score)


}






