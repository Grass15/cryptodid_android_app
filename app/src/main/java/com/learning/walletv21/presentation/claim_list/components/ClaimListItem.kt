package com.learning.walletv21.presentation.claim_list.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.learning.walletv21.domain.model.ClaimModel


@Composable
fun ClaimListItem(
    claim: ClaimModel,
    onItemClick: (ClaimModel) ->Unit
) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .clickable { onItemClick(claim) }
            .padding(20.dp),
        horizontalArrangement =   Arrangement.SpaceBetween
    ){
         Text(
             text = "${claim.rank}.  ${claim.name}  (${claim.symbol})",
             style= MaterialTheme.typography.body1,
             overflow = TextOverflow.Ellipsis
         )
        Text(text = if(claim.is_active) "active" else "inactive",
            color = if(claim.is_active) Color.Green else Color.Red,
            fontStyle = FontStyle.Italic,
            textAlign = TextAlign.End,
            style = MaterialTheme.typography.body2,
            modifier = Modifier.align(CenterVertically)
            )


    }
}