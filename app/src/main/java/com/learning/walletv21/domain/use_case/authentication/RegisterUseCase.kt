package com.learning.walletv21.domain.use_case.authentication


import android.util.Log
import com.learning.walletv21.data.local.entity.UserEntity
import com.learning.walletv21.domain.repository.UserRepository
import com.learning.walletv21.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val repository: UserRepository
) {
    operator fun invoke(user: UserEntity): Flow<Resource<Boolean>> = flow {
        try {
            emit(Resource.Loading<Boolean>())
            repository.insertUser(user)
            val res = true
            Log.d("AuthUseCase",res.toString())
            emit(Resource.Success<Boolean>(res))
        }catch (e: Exception){
            emit(Resource.Error<Boolean>(e.localizedMessage?: "Could not log in",false))
        }
    }
}