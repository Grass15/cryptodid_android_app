package com.learning.walletv21.presentation.home.main_home.components.search


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.google.accompanist.flowlayout.FlowRow


@Composable
fun SearchType() {
    val radioOps = listOf("microblink","bank","credit","personal")
    val (selectedOption, onOptionSelected) = remember { mutableStateOf(radioOps[2]) }
    FlowRow(
        mainAxisSpacing = 10.dp,
        crossAxisSpacing = 10.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        // each radio button in columns.
        radioOps.forEach { text ->
            Row(
                Modifier
                    .selectable(
                        selected = (text == selectedOption),
                        onClick = { onOptionSelected(text) }
                    )
                    .padding(horizontal = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {

                RadioButton(
                    // inside this method we are
                    // adding selected with a option.
                    selected = (text == selectedOption),modifier = Modifier.padding(all = Dp(value = 8F)),
                    onClick = {

                    }
                )
                // below line is use to add
                // text to our radio buttons.
                Text(
                    text = text,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
        }
    }
}