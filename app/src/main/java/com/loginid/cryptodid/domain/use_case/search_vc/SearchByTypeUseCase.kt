package com.loginid.cryptodid.domain.use_case.search_vc

import android.database.sqlite.SQLiteException
import com.loginid.cryptodid.data.local.entity.VCEntity
import com.loginid.cryptodid.data.local.entity.VCType
import com.loginid.cryptodid.domain.repository.UserRepository
import com.loginid.cryptodid.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SearchByTypeUseCase @Inject constructor(
    private val repository: UserRepository
) {

    operator fun invoke(userId: String,vcType: VCType): Flow<Resource<List<VCEntity>>> = flow {
        try {
            emit(Resource.Loading<List<VCEntity>>())
            repository.getVCsByType(userId,vcType).collect{
                emit(Resource.Success<List<VCEntity>>(data = it))
            }
        }catch (e: SQLiteException){
            emit(Resource.Error<List<VCEntity>>(e.localizedMessage?:"An error occured",null))
        }
    }

}