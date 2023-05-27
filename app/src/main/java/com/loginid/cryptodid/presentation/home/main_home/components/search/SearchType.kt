package com.loginid.cryptodid.presentation.home.main_home.components.search


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.google.accompanist.flowlayout.FlowRow
import com.loginid.cryptodid.data.local.entity.VCType


@Composable
fun SearchType(
    onvcTypeOptionSelected : (VCType) -> Unit
) {
    val radioOps = listOf(
        VCType.BANK,
        VCType.CREDIT_SCORE,
        VCType.AGE,
        VCType.DEFAULT,
        VCType.ID,
        VCType.INSURANCE_NUMBER,
        VCType.DRIVER_LICENCE,
    )
    val (selectedOption, onOptionSelected) = remember { mutableStateOf(radioOps[2]) }
    FlowRow(
        mainAxisSpacing = 5.dp,
        crossAxisSpacing = 5.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        // each radio button in columns.
        radioOps.forEach { text ->
            Row(
                Modifier
                    .selectable(
                        selected = (text == selectedOption),
                        onClick = { onOptionSelected(text)
                            onvcTypeOptionSelected(text)
                        }
                    )
                    .padding(horizontal = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {

                RadioButton(
                    // inside this method we are
                    // adding selected with a option.
                    selected = (text == selectedOption),modifier = Modifier.padding(all = Dp(value = 4F)),
                    onClick = {

                    }
                )
                // below line is use to add
                // text to our radio buttons.
                Text(
                    text = text.name,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
        }
    }
}