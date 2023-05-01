package com.loginid.cryptodid.data.repository

import com.loginid.cryptodid.data.local.dao.VCDao
import com.loginid.cryptodid.data.remote.IssuerApi
import com.loginid.cryptodid.data.remote.vcdto.VCDto
import com.loginid.cryptodid.domain.repository.VCRepository
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