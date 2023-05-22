package com.loginid.cryptodid.domain.use_case.save_vc

import android.database.sqlite.SQLiteException
import androidx.compose.animation.ExperimentalAnimationApi
import com.loginid.cryptodid.data.local.entity.VCEntity
import com.loginid.cryptodid.data.local.entity.VCType
import com.loginid.cryptodid.domain.repository.UserRepository
import com.loginid.cryptodid.model.Claim
import com.loginid.cryptodid.presentation.MainActivity.Companion.getFilesFolder
import com.loginid.cryptodid.presentation.home.vc.VCViewModel.VCEnteryState
import com.loginid.cryptodid.presentation.issuer.encryptSin
import com.loginid.cryptodid.protocols.Issuer
import com.loginid.cryptodid.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SaveVCUseCase @Inject constructor(
    private val repository: UserRepository
) {
    operator fun invoke(vcID: String,vcContent: VCEnteryState,ownerID: String,vcType: VCType,vcTitle: String): Flow<Resource<Boolean>> = flow {
        try {
            emit(Resource.Loading<Boolean>())
            val VC = prepareVC(vcContent = vcContent)
            repository.insertVC(VCEntity(id = vcID,vc = VC, claimOwner = ownerID,vcType = vcType,vcTitle = vcTitle))
            emit(Resource.Success<Boolean>(true))

        }catch (e: SQLiteException){
            emit(Resource.Error<Boolean>(e.localizedMessage?:"An error occured",null))
        }
    }
    external fun TFHE(n1: Int, filepath: String?, attribute: String?): Int

    private fun prepareVC(vcContent: VCEnteryState): Claim {
        val issuer: Issuer =
            Issuer()
        issuer.setAttribute(vcContent.VCAttribute)
        val path = getFilesFolder()

        if(vcContent.VCTypeEnum != VCType.PRIVILEGE && vcContent.VCTypeText != "sin"){
            TFHE(vcContent.VCAttribute, java.lang.String.valueOf(path), vcContent.VCTypeText.split(" ")[0].lowercase().trim())
        }
        else if(vcContent.VCTypeText == "sin" ){
            encryptSin(123456789, java.lang.String.valueOf(getFilesFolder()), "sin")
        }
        val VC: Claim = issuer.getClaim(vcContent.issuerName,vcContent.VCTypeText,vcContent.VCTitle,vcContent.VCContentOverview, vcContent.VCTypeText)// Claim(vcContent.VCTitle,vcContent.VCType,vcContent.issuerName,vcContent.VCContentOverview)
        VC.expirationDate = vcContent.experationDate
        return  VC
    }
}

