package com.learning.walletv21.domain.use_case.save_vc

import android.database.sqlite.SQLiteException
import com.learning.walletv21.core.protocols.javamodels.Claim
import com.learning.walletv21.data.local.entity.VCEntity
import com.learning.walletv21.domain.repository.UserRepository
import com.learning.walletv21.presentation.home.vc.VCViewModel.VCEnteryState
import com.learning.walletv21.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SaveVCUseCase @Inject constructor(
    private val repository: UserRepository
) {
    operator fun invoke(vcID: String,vcContent: VCEnteryState,ownerID: String): Flow<Resource<Boolean>> = flow {
        try {
            emit(Resource.Loading<Boolean>())
            val VC = Claim(vcContent.VCTitle,vcContent.VCType,vcContent.issuerName,vcContent.VCContentOverview)
            VC.expirationDate = vcContent.experationDate
            repository.insertVC(VCEntity(vcID,VC,ownerID))
            emit(Resource.Success<Boolean>(true))

        }catch (e: SQLiteException){
            emit(Resource.Error<Boolean>(e.localizedMessage?:"An error occured",null))
        }
    }
}