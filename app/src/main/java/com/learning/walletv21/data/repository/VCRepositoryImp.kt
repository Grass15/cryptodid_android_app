package com.learning.walletv21.data.repository

import com.learning.walletv21.data.local.dao.VCDao
import com.learning.walletv21.data.remote.IssuerApi
import com.learning.walletv21.data.remote.vcdto.VCDto
import com.learning.walletv21.domain.repository.VCRepository
import javax.inject.Inject

/**
 * our main repository, That will provide us with the necessary function
 * to pull our vc and then storing the VC in our local cash
 * @param api
 * @param dao
 */
class VCRepositoryImp @Inject constructor(
    private val api: IssuerApi,
    private val dao: VCDao
): VCRepository{
    override suspend fun getVC(VCType: String, VCProvider: String): VCDto {
        return api.getVC(VCType,VCProvider)
    }

}