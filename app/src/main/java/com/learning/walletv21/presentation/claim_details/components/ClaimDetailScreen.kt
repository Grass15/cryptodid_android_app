package com.learning.walletv21.presentation.claim_details.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.accompanist.flowlayout.FlowRow
import com.learning.walletv21.presentation.TempNav.Screen
import com.learning.walletv21.presentation.claim_details.ClaimDetailsViewModel
import com.learning.walletv21.presentation.claim_list.ClaimListViewModel




@Composable
fun ClaimDetailScreen(
    viewModel: ClaimDetailsViewModel = hiltViewModel()
){
     val state = viewModel.state.value
    Box(modifier = Modifier.fillMaxSize()){
        state.claim?.let{ claim ->
            LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(20.dp)){
                item{
                    Row(modifier = Modifier.fillMaxSize(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(
                            text = "${claim.rank}. ${claim.name} (${claim.symbol})",
                            style = MaterialTheme.typography.h2,
                            modifier = Modifier.weight(8f)
                        )
                        Text(
                            text = if(claim.is_active) "active" else "inactive",
                            color = if(claim.is_active) Color.Green else Color.Red,
                            fontStyle = FontStyle.Italic,
                            textAlign = TextAlign.End,
                            modifier = Modifier
                                .align(CenterVertically)
                                .weight(2f)
                        )
                    }
                    Spacer(modifier = Modifier.height(15.dp))
                    Text(text = claim.description,
                         style = MaterialTheme.typography.body2
                    )
                    Spacer(modifier = Modifier.height(15.dp))
                    Text(text = "tags",
                        style = MaterialTheme.typography.h3
                    )
                    FlowRow(mainAxisSpacing = 10.dp,
                    crossAxisSpacing = 10.dp,
                    modifier = Modifier.fillMaxSize()) {
                        claim.tags.forEach{
                            ClaimTag(tag = it)
                        }
                    }
                    Spacer(modifier = Modifier.height(15.dp))
                    Text(text = "Team Members",
                        style = MaterialTheme.typography.h3
                    )
                }
                items(claim.team){
                    TeamListItem(teamMember = it, modifier = Modifier
                        .fillMaxSize()
                        .padding(10.dp))
                    Divider()
                }
                
            }
        }


        if(state.error.isNotBlank()){
            Text(
                text = state.error,
                color = MaterialTheme.colors.error,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp)
                    .align(Alignment.Center)
            )
        }

        if(state.isLoading){
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
    }
}
