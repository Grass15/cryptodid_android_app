package com.learning.walletv21.domain.use_case.get_vc

import android.database.sqlite.SQLiteException
import com.learning.walletv21.data.local.entity.UserAndVC
import com.learning.walletv21.domain.repository.UserRepository
import com.learning.walletv21.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetVCUseCase @Inject constructor(
    private val repository: UserRepository
) {
    operator fun invoke(userId: String): Flow<Resource<UserAndVC>> = flow {
        try {
            emit(Resource.Loading<UserAndVC>())
            repository.getUserWithClaims(userId).collect{
                emit(Resource.Success<UserAndVC>(data = it))
            }
        }catch (e: SQLiteException){
            emit(Resource.Error<UserAndVC>(e.localizedMessage?:"An error occured",null))
        }
    }
}