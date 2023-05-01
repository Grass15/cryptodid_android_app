package com.loginid.cryptodid.domain.use_case.get_claim

import com.loginid.cryptodid.data.remote.dto.toClaimDetail
import com.loginid.cryptodid.domain.model.ClaimDetailsModel
import com.loginid.cryptodid.domain.repository.ClaimRepository
import com.loginid.cryptodid.utils.Resource
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetClaimUseCase @Inject constructor(
    private val repository: ClaimRepository
) {
    operator fun invoke(coinId: String): Flow<Resource<ClaimDetailsModel>> = flow{
        try {
            emit(Resource.Loading<ClaimDetailsModel>())
            val claim = repository.getClaimById(coinId).toClaimDetail()
            emit(Resource.Success<ClaimDetailsModel>(claim))
        }catch (e: HttpException){
            emit(Resource.Error<ClaimDetailsModel>(e.localizedMessage?: "An error has occured"))
        }catch (e: IOException){
            emit(Resource.Error<ClaimDetailsModel>("Could not reach server"))
        }
    }
}