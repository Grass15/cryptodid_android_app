package com.loginid.cryptodid.presentation.issuer.issuer_gateway


import android.content.Intent
import android.net.Uri
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.loginid.cryptodid.data.local.entity.VCType
import com.loginid.cryptodid.presentation.MainActivity
import com.loginid.cryptodid.presentation.MainActivity.Companion.getFilesFolder
import com.loginid.cryptodid.presentation.MainActivity.Companion.path
import com.loginid.cryptodid.presentation.home.vc.VCViewModel.VCEnteryState
import com.loginid.cryptodid.presentation.home.vc.VCViewModel.VCViewModel
import com.loginid.cryptodid.presentation.issuer.encryptSin
import com.loginid.cryptodid.presentation.navigation.screens.HomeScreen
import com.loginid.cryptodid.utils.Status
import java.util.*

@Composable
fun issuerGateWay(
    navController: NavController,
    vcViewModel: VCViewModel = hiltViewModel()
){
    val context = LocalContext.current
    val webView = remember { WebView(context) }

    val reachedDesiredPage = remember { mutableStateOf(false) }

    val url by remember {
        mutableStateOf("https://mostafa-hmoura.github.io/issuer-gateway/")
    }
Scaffold(
    topBar = {
        TopAppBar(
            title = { Text(text = "Issuer Gateway") },
            navigationIcon = {
                IconButton(onClick = { webView.goBack() }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
            },
            actions = {
                IconButton(onClick = { webView.goForward() }) {
                    Icon(Icons.Default.ArrowForward, contentDescription = "Forward")
                }
            }
        )
    }
) {
    AndroidView(modifier = Modifier.padding(it),
        factory = { context ->
            webView.apply {
                settings.javaScriptEnabled = true
                webViewClient = object : WebViewClient() {
                    override fun onPageFinished(view: WebView?, url: String?) {

                        if (url?.startsWith("https://mostafa-hmoura.github.io/issuer-gateway/final.html") == true) {
                            try{
                                val socialInss = "Social Insurance"
                                val uri = Uri.parse(url)
                                val regex = "\\d+".toRegex()
                                val name = if(uri.getQueryParameter("Name").toString().startsWith(socialInss)) "SIN" else uri.getQueryParameter("Name")
                                val value = regex.find(uri.getQueryParameter("Value").toString())
                                val issuer = uri.getQueryParameter("IssuerName")
                                value?.value?.toIntOrNull().let {
                                    it!!.let { it1->
                                        Log.d("ExtractedFromBr",name.toString() + "  " + it1.toString())
                                        var temp = name.toString()
                                        if (name.toString() == "Date of Birth"){
                                            temp = "age"
                                        } else if (name.toString() == "Bank Balance"){
                                            temp = "balance"
                                        } else if (name.toString() == "Credit Score"){
                                            temp = "creditScore"
                                        }
                                        println(temp)
                                        vcViewModel.saveVC(
                                            VCEnteryState(
                                                experationDate = Date(),
                                                issuerName = issuer.toString().trim(),
                                                VCTypeText = temp,
                                                VCTitle = name.toString().trim(),
                                                VCContentOverview = "none",
                                                VCTypeEnum = VCType.BANK,
                                                VCAttribute = it1
                                            )
                                        )
                                    }
                                }
                            }catch (e: Exception){
                                e.printStackTrace()
                            }

                            //navigating back to home
                            navController.navigate(HomeScreen.MainHomeScreen.route){
                                popUpTo(HomeScreen.MainHomeScreen.route){
                                    inclusive = true
                                }
                            }


                        }

                        super.onPageFinished(view, url)
                    }
                }
                loadUrl(url)
            }
        }
    )
}

}