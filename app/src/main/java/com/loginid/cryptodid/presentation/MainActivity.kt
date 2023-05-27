package com.loginid.cryptodid.presentation

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.getValue
import androidx.core.app.ActivityCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.loginid.cryptodid.model.Claim
import com.loginid.cryptodid.presentation.loginid_welcome.SplashViewModel
import com.loginid.cryptodid.presentation.navigation.graphs.SetupNavGraph
import com.loginid.cryptodid.presentation.theme.WalletV21Theme
import com.loginid.cryptodid.utils.Constants.BLINK_ID_LICENCE
import com.microblink.blinkid.MicroblinkSDK
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import javax.inject.Inject


@ExperimentalAnimationApi
@ExperimentalPagerApi
@AndroidEntryPoint
class MainActivity : ComponentActivity() {



    lateinit var navController: NavHostController
    @Inject
    lateinit var splashViewModel: SplashViewModel



    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        MicroblinkSDK.setLicenseKey(BLINK_ID_LICENCE, this)
        ActivityCompat.requestPermissions(
            this, arrayOf(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ),
            PackageManager.PERMISSION_GRANTED
        )
        path = filesDir
      installSplashScreen().setKeepOnScreenCondition{
          !splashViewModel.isLoading.value
      }
        setContent {
            WalletV21Theme {
                val screen by splashViewModel.startDestination

                navController = rememberNavController()
                // SetupNavGraph(navController = navController)
               /* NavHost(navController = navController, startDestination =  Screen.ClaimListScreen.route){
                    composable(
                        route = Screen.ClaimListScreen.route
                    ){
                        ClaimListScreen(navController = navController)
                    }
                    composable(
                        route = Screen.ClaimDetailScreen.route + "/{$PARAM_CLAIM_ID}"
                    ){
                        ClaimDetailScreen()
                    }
                }
                */
                SetupNavGraph(navController = navController, startDestination = /*AUTH_GRAPH*//*HOME_ROOT_GRAPH*/screen)
                //BiometricPromptScreen()
               // DemoScan()
               // LaunchScannerScreen()
            }
        }
    }

    companion object {
        init {
            System.loadLibrary("tfhemain")
        }
        var path: File? = null
        @JvmStatic
        fun  getFilesFolder(): File? {
            return MainActivity.path;
        }
    }
}

