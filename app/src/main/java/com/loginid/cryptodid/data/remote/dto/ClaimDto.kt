package com.loginid.cryptodid.data.remote.dto

import com.loginid.cryptodid.domain.model.ClaimModel

data class ClaimDto(
    val id: String,
    val is_active: Boolean,
    val is_new: Boolean,
    val name: String,
    val rank: Int,
    val symbol: String,
    val type: String
)

fun ClaimDto.toCoin(): ClaimModel {
    return ClaimModel(
        id = id,
        is_active = is_active,
        name = name,
        rank = rank,
        symbol = symbol
            )
}