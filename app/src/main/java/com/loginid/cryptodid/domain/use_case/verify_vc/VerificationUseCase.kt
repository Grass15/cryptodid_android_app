package com.loginid.cryptodid.domain.use_case.verify_vc

import com.loginid.cryptodid.claimVerifier.VerificationStatus
import com.loginid.cryptodid.claimVerifier.Verifier
import com.loginid.cryptodid.domain.repository.UserRepository
import com.loginid.cryptodid.utils.Resource
import com.loginid.cryptodid.utils.Status
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


class VerificationUseCase @Inject constructor(
    private val repository: UserRepository
) {
   // private val verifier: Verifier = Verifier()
    operator fun invoke(verifier: Verifier): Flow<Resource<VerificationStatus>> = flow {
        try {
            emit(Resource.Loading<VerificationStatus>())
            val vResult: VerificationStatus = verifier.verifyVCsRentalHouse()
            emit(Resource.Success<VerificationStatus>(vResult))
        }catch (e: Exception){
            emit(Resource.Error<VerificationStatus>(e.localizedMessage?: "Could not delete vc",VerificationStatus("OOOPS SOMETHING WENT WRONG WHILE VERIFICATION", Status.ERROR)))
        }
    }

}