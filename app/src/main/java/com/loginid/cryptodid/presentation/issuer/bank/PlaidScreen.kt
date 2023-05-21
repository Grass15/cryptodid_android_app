package com.loginid.cryptodid.presentation.issuer.bank


import android.util.Log
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.loginid.cryptodid.presentation.home.vc.VCViewModel.VCEnteryState
import com.loginid.cryptodid.presentation.issuer.bank.network.AccountRequester
import com.loginid.cryptodid.presentation.issuer.bank.network.ResponseClass
import com.plaid.link.configuration.LinkTokenConfiguration
import com.plaid.link.result.LinkExit
import com.plaid.link.result.LinkSuccess
import retrofit2.Call
import retrofit2.Response
import java.util.*
import com.loginid.cryptodid.presentation.home.vc.VCViewModel.VCViewModel
import com.loginid.cryptodid.presentation.issuer.bank.network.LinkTokenRequester.token
import com.loginid.cryptodid.presentation.navigation.screens.HomeScreen
import com.plaid.link.OpenPlaidLink
import com.plaid.link.result.LinkResult

//var vcViewModel : VCViewModel = TODO();
@Composable
fun PlaidScreen( navController: NavController,) {
    var vcViewModel: VCViewModel = hiltViewModel()
    val linkAccountToPlaid = rememberLauncherForActivityResult(contract = OpenPlaidLink()){
        when(it){
            is LinkSuccess -> showSuccess(it, vcViewModel, navController)
            is LinkExit -> showFailure(it)
        }
    }
    openLink(linkAccountToPlaid)

}




private fun showSuccess(success: LinkSuccess, vcViewModel: VCViewModel, navController: NavController,) {
    AccountRequester.getAccounts(object : retrofit2.Callback<ResponseClass>{
        override fun onResponse(call: Call<ResponseClass>, response: Response<ResponseClass>) {
            if(response.isSuccessful){
                val responseBody = response.body()
                if(responseBody != null){
                    println(responseBody)
                    Log.d("Extracted", responseBody.accounts[1].balances.available.toString())
                    vcViewModel.saveVC(
                        VCEnteryState(
                            experationDate = Date(),
                            issuerName = "Plaid",
                            VCTypeText = "Balance Amount",
                            VCTitle = "Balance",
                            VCContentOverview = "+100",
                            VCAttribute = responseBody.accounts[1].balances.available
                        )
                    )
                    navController.popBackStack()
                    navController.navigate(HomeScreen.MainHomeScreen.route)

                }else{
                    println(responseBody)
                }
            }else{
                println("Request was not successful"+response)
            }
        }
        override fun onFailure(call: Call<ResponseClass>, t: Throwable) {
            println("request failed")
        }

    })


}



private fun showFailure(exit: LinkExit) {
    if (exit.error != null) {
        println(exit.error?.displayMessage+"  "+exit.error?.errorCode)
    } else {
        println(exit.metadata.status?.jsonValue ?: "unknown")
    }
}



/**
 * For all Link configuration options, have a look at the
 * [parameter reference](https://plaid.com/docs/link/android/#parameter-reference)>
 */
fun openLink(linkAccountToPlaid: ManagedActivityResultLauncher<LinkTokenConfiguration, LinkResult>) {
    token
        .subscribe({ token: String? ->
            if (token != null) {
                onLinkTokenSuccess(token, linkAccountToPlaid)
            }
        }) { error: Throwable? ->
            if (error != null) {
                onLinkTokenError(
                    error
                )
            }
        }
}

fun onLinkTokenSuccess(linkToken: String, linkAccountToPlaid: ManagedActivityResultLauncher<LinkTokenConfiguration, LinkResult>) {
    val tokenConfiguration = LinkTokenConfiguration.Builder()
        .token(linkToken)
        .build()
    linkAccountToPlaid.launch(tokenConfiguration)
}

private fun onLinkTokenError(error: Throwable) {
    if (error is java.net.ConnectException) {
        println("Please run `sh start_server.sh <client_id> <sandbox_secret>`")
        return
    }
    println(error.message)
}
