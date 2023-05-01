package com.loginid.cryptodid.domain.use_case.remove_vc

import com.loginid.cryptodid.domain.repository.UserRepository
import com.loginid.cryptodid.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RemoveVCUseCase @Inject constructor(
    private val repository: UserRepository
){
    operator fun invoke(VCId: String): Flow<Resource<Boolean>> = flow {
        try {
            emit(Resource.Loading<Boolean>())
            repository.deleteVCById(VCId)
            emit(Resource.Success<Boolean>(true))
        }catch (e: Exception){
            emit(Resource.Error<Boolean>(e.localizedMessage?: "Could not delete vc",false))
        }
    }
}