package com.learning.walletv21.domain.model

data class ClaimModel (
        val id: String,
        val is_active: Boolean,
        val name: String,
        val rank: Int,
        val symbol: String,
)