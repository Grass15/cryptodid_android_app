package com.loginid.cryptodid.domain.use_case.save_vc

import android.database.sqlite.SQLiteException
import androidx.compose.animation.ExperimentalAnimationApi
import com.google.accompanist.pager.ExperimentalPagerApi
import com.loginid.cryptodid.data.local.entity.VCEntity
import com.loginid.cryptodid.data.local.entity.VCType
import com.loginid.cryptodid.domain.repository.UserRepository
import com.loginid.cryptodid.model.Claim
import com.loginid.cryptodid.presentation.MainActivity
import com.loginid.cryptodid.presentation.MainActivity.Companion.getFilesFolder
import com.loginid.cryptodid.presentation.home.vc.VCViewModel.VCEnteryState
import com.loginid.cryptodid.presentation.issuer.encryptSin
import com.loginid.cryptodid.protocols.Issuer
import com.loginid.cryptodid.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject


@OptIn(ExperimentalAnimationApi::class, ExperimentalPagerApi::class)
class SaveVCUseCase @Inject constructor(
    private val repository: UserRepository
) {
    operator fun invoke(vcID: String,vcContent: VCEnteryState,ownerID: String,vcType: VCType,vcTitle: String): Flow<Resource<Boolean>> = flow {
        try {
            emit(Resource.Loading<Boolean>())
            var id = vcID
            var vcVersion = 0
            val VC = prepareVC(vcContent = vcContent)
            //Checking if a vc with the same vcType already exist in our database
            val existingVC = repository.getVCByType(vcType)
            if (existingVC != null) {
                // Update the existing VC entity
                id = existingVC.id // Preserve the existing primary key
                vcVersion = existingVC.version + 1
                repository.updateVC(VCEntity(id = id,vc = VC, claimOwner = ownerID,vcType = vcType,vcTitle = vcTitle, version = vcVersion))
                val updateVCInBlockchainThread: Thread = object : Thread() {
                    override fun run() {
                        println("Updating")
                        changeBlockchainState(MainActivity.blockChainApi+"/post/vc", "{\"signature\":\""+ Arrays.toString(VC.signature) +"\",\"revNonce\":\""+ Arrays.toString(VC.revocationNonce) +"\",\"version\":\""+vcVersion+"\"}")
                    }
                }
                updateVCInBlockchainThread.start()
            } else {
                // Insert the new VC entity
                repository.insertVC(VCEntity(id = id, vc = VC, claimOwner = ownerID,vcType = vcType,vcTitle = vcTitle))
                val addVCToBlockchainThread: Thread = object : Thread() {
                    override fun run() {
                        println("Adding")
                        changeBlockchainState(MainActivity.blockChainApi+"/update/vc", "{\"signature\":\""+ Arrays.toString(VC.signature) +"\",\"revNonce\":\""+ Arrays.toString(VC.revocationNonce) +"\",\"version\":\""+vcVersion+"\"}")
                    }
                }
                addVCToBlockchainThread.start()

            }
            emit(Resource.Success<Boolean>(true))

        }catch (e: SQLiteException){
            emit(Resource.Error<Boolean>(e.localizedMessage?:"An error occured",null))
        }
    }
    external fun TFHE(n1: Int, filepath: String?, attribute: String?): Int

    @OptIn(ExperimentalAnimationApi::class, ExperimentalPagerApi::class)
    private fun prepareVC(vcContent: VCEnteryState): Claim {
        val issuer: Issuer =
            Issuer()
        issuer.setAttribute(vcContent.VCAttribute)
        val path = getFilesFolder()

        if(vcContent.VCTypeEnum != VCType.PRIVILEGE && vcContent.VCTypeText != "sin"){
            TFHE(vcContent.VCAttribute, java.lang.String.valueOf(path), vcContent.VCTypeText)
        }
        else if(vcContent.VCTypeText == "sin" ){
            encryptSin(123456789, java.lang.String.valueOf(getFilesFolder()), "sin")
        }

        val VC: Claim = issuer.getClaim(vcContent.VCTitle, vcContent.issuerName, vcContent.VCTypeText, vcContent.VCContentOverview)// Claim(vcContent.VCTitle,vcContent.VCType,vcContent.issuerName,vcContent.VCContentOverview)
        VC.expirationDate = vcContent.experationDate
        return  VC
    }

    fun changeBlockchainState(url: String, json: String) {

        val client: OkHttpClient = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
        val JSON = "application/json".toMediaType()
        val body: RequestBody = json.toRequestBody(JSON)
        val request = Request.Builder()
            .method("POST", body)
            .url(url)
            .build()
        client.newCall(request).execute().use { response -> println(response.body!!.string()) }
    }
}

