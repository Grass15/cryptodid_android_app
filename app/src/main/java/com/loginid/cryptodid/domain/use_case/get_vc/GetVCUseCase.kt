package com.loginid.cryptodid.domain.use_case.get_vc

import android.database.sqlite.SQLiteException
import com.loginid.cryptodid.data.local.entity.UserAndVC
import com.loginid.cryptodid.domain.repository.UserRepository
import com.loginid.cryptodid.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetVCUseCase @Inject constructor(
    private val repository: UserRepository
) {
    operator fun invoke(userId: String): Flow<Resource<UserAndVC>> = flow {
        try {
            emit(Resource.Loading<UserAndVC>())
            repository.getUserWithVCs(userId).collect{
                emit(Resource.Success<UserAndVC>(data = it))
            }
        }catch (e: SQLiteException){
            emit(Resource.Error<UserAndVC>(e.localizedMessage?:"An error occured",null))
        }
    }
}