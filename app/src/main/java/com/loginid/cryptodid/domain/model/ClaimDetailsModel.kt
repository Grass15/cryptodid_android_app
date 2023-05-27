package com.loginid.cryptodid.domain.model

import com.loginid.cryptodid.data.remote.dto.TeamMember


data class ClaimDetailsModel(
    val coinId: String,
    val name: String,
    val description: String,
    val symbol: String,
    val rank: Int,
    val is_active: Boolean,
    val tags : List<String>,
    val team: List<TeamMember>
)
