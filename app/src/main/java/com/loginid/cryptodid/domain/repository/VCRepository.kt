package com.loginid.cryptodid.domain.repository

import com.loginid.cryptodid.data.remote.vcdto.VCDto


interface VCRepository {
    suspend fun getVC(VCType: String, VCProvider: String): VCDto
}