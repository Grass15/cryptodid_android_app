package com.loginid.cryptodid.presentation.home.modalDialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.loginid.cryptodid.presentation.theme.CardForGround

class ModalDialogs (
    val message: String = "",
    val title: String = "",
    val vcid:String = "",
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

    @Composable
    fun VCDeletionAlertDialog(onDismiss: (Boolean) -> Unit,onProceed: (Boolean) -> Unit,extra: @Composable (String) -> Unit){
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
                        onProceed(false)
                    },
                  /*  colors  = ButtonDefaults.buttonColors(
                        contentColor = Color.Red,
                       // backgroundColor = Color.Red
                    ),*/
                ) {
                    Text(text = "Confirm")
                    IconButton(onClick = {  }) {
                        Icon(Icons.Default.Delete, contentDescription = "deletion Button Icon")
                    }
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
                    IconButton(onClick = {  }) {
                        Icon(Icons.Default.Cancel, contentDescription = "deletion Button Icon")
                    }
                }
            },
            title = {
                Text(text = this.title)
            },
            text = {
               // Text(text = this.message)
                   extra(this.message)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            shape = RoundedCornerShape(5.dp),
            backgroundColor = MaterialTheme.colors.CardForGround
        )
    }

}