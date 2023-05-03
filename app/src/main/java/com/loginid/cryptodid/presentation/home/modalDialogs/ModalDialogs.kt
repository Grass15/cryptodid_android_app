package com.loginid.cryptodid.presentation.home.modalDialogs

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.loginid.cryptodid.presentation.theme.CardForGround

class ModalDialogs (
    val message: String = "",
    val title: String = ""
        ){

    @Composable
    fun BiometricsAlertDialog(onDismiss: (Boolean) -> Unit){
        AlertDialog(
            onDismissRequest = {
                // Dismiss the dialog when the user clicks outside the dialog or on the back button.
                // If you want to disable that functionality, simply leave this block empty.
                onDismiss(false)
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        // perform the confirm action and
                        // close the dialog
                        onDismiss(false)
                    }
                ) {
                    Text(text = "Confirm")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        // close the dialog
                        onDismiss(false)
                    }
                ) {
                    Text(text = "Dismiss")
                }
            },
            title = {
                Text(text = this.title)
            },
            text = {
                Text(text = this.message)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            shape = RoundedCornerShape(5.dp),
            backgroundColor = MaterialTheme.colors.CardForGround
        )
    }

}