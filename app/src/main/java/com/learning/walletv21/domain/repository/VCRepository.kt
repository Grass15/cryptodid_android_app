package com.learning.walletv21.domain.repository

import com.learning.walletv21.data.remote.vcdto.VCDto


interface VCRepository {
    suspend fun getVC(VCType: String, VCProvider: String): VCDto
}