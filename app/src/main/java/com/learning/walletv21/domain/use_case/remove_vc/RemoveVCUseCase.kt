package com.learning.walletv21.domain.use_case.remove_vc

import android.database.sqlite.SQLiteException
import com.learning.walletv21.data.local.entity.UserAndVC
import com.learning.walletv21.domain.repository.UserRepository
import com.learning.walletv21.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RemoveVCUseCase @Inject constructor(
    private val repository: UserRepository
){
    operator fun invoke(VCId: Int): Flow<Resource<Boolean>> = flow {
        try {
            emit(Resource.Loading<Boolean>())
            repository.deleteVCById(VCId)
            emit(Resource.Success<Boolean>(true))
        }catch (e: Exception){
            emit(Resource.Error<Boolean>(e.localizedMessage?: "Could not delete vc",false))
        }
    }
}