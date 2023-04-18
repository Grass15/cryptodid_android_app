package com.learning.walletv21.domain.use_case.authentication

import android.util.Log
import com.learning.walletv21.data.local.entity.UserEntity
import com.learning.walletv21.domain.repository.UserRepository
import com.learning.walletv21.presentation.authentication.AuthEvents
import com.learning.walletv21.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val repository: UserRepository
) {
    operator fun invoke(username: String, pass:String): Flow<Resource<Boolean>> = flow {
           try {
               emit(Resource.Loading<Boolean>())
               val res = repository.checkUserCreds(username,pass)
               Log.d("AuthUseCase",res.toString())
               emit(Resource.Success<Boolean>(res))
           }catch (e: Exception){
              emit(Resource.Error<Boolean>(e.localizedMessage?: "Could not log in",false))
           }
    }
}
