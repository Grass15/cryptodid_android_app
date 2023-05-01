package com.loginid.cryptodid.data.remote.dto

import com.loginid.cryptodid.domain.model.ClaimDetailsModel

data class ClaimDetailDto(
    val description: String,
    val development_status: String,
    val first_data_at: String,
    val hardware_wallet: Boolean,
    val hash_algorithm: String,
    val id: String,
    val is_active: Boolean,
    val is_new: Boolean,
    val last_data_at: String,
    val links: Links,
    val links_extended: List<LinksExtended>,
    val logo: String,
    val message: String,
    val name: String,
    val open_source: Boolean,
    val org_structure: String,
    val proof_type: String,
    val rank: Int,
    val started_at: String,
    val symbol: String,
    val tags: List<Tag>,
    val team: List<TeamMember>,
    val type: String,
    val whitepaper: Whitepaper
)

fun ClaimDetailDto.toClaimDetail(): ClaimDetailsModel{
    return ClaimDetailsModel(
        coinId = id,
      name = name,
     description = description,
     symbol = symbol,
     rank = rank,
     is_active = is_active,
     tags = tags.map{it.name},
     team = team
    )
}