package com.learning.walletv21.domain.use_case.get_claims

import com.learning.walletv21.data.remote.dto.toCoin
import com.learning.walletv21.domain.model.ClaimModel
import com.learning.walletv21.domain.repository.ClaimRepository
import com.learning.walletv21.utils.Resource
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetClaimsUseCase @Inject constructor(
    private val repository: ClaimRepository
) {
    operator fun invoke(): Flow<Resource<List<ClaimModel>>> = flow{
        try {
           emit(Resource.Loading<List<ClaimModel>>())
            val claims = repository.getClaims().map{it.toCoin()}
            emit(Resource.Success<List<ClaimModel>>(claims))
        }catch (e: HttpException){
              emit(Resource.Error<List<ClaimModel>>(e.localizedMessage?: "An error has occured"))
        }catch (e: IOException){
              emit(Resource.Error<List<ClaimModel>>("Could not reach server"))
        }
    }
}