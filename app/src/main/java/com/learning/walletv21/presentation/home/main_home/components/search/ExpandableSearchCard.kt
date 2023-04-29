package com.learning.walletv21.presentation.home.main_home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.learning.walletv21.presentation.home.main_home.components.search.SearchType
import com.learning.walletv21.presentation.theme.CardForGround


@Composable
fun ExpandableSearchCard() {
    var expanded by remember { mutableStateOf(false) }
    Card(
        shape = RoundedCornerShape(8.dp),
        elevation = 4.dp,
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colors.CardForGround)
            .padding(9.dp)
            .clickable { expanded = !expanded }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Filter", style = MaterialTheme.typography.h6)
            if (expanded) {
                /*Text(
                    "This is the expanded content of the card.",
                    style = MaterialTheme.typography.body2
                )*/
                SearchType()
            }
        }
    }
}
