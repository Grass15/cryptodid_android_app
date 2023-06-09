package com.loginid.cryptodid.presentation.navigation.drawer_navigation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.loginid.cryptodid.data.local.entity.VCType
import com.loginid.cryptodid.presentation.home.main_home.components.ExpandableSearchCard
import com.loginid.cryptodid.presentation.home.vc.VCViewModel.MultipleVCOperations
import com.loginid.cryptodid.presentation.theme.AppBar
import com.loginid.cryptodid.presentation.theme.inputTextColor


@Composable
fun AppBar(
    onNavigationIconClick: () -> Unit,
    onSearchClicked: () -> Unit
) {
        TopAppBar(
            title = {
                Text(text = "Login ID"/*stringResource(id = R.string.app_name)*/, color = MaterialTheme.colors.inputTextColor)
            },
            backgroundColor = MaterialTheme.colors.AppBar,
            contentColor = MaterialTheme.colors.onPrimary,
            navigationIcon = {
                IconButton(onClick = onNavigationIconClick) {
                    Icon(imageVector = Icons.Default.Menu, contentDescription = "Toogle Drawer", tint = MaterialTheme.colors.inputTextColor)

                }
            },
            actions = {
                IconButton(onClick = {onSearchClicked()}) {
                    Icon(imageVector = Icons.Filled.Search,contentDescription = "Search Icon",tint = MaterialTheme.colors.inputTextColor)
                }
            }
        )
}

@Composable
fun SearchAppBar(
  text: String,
  onSearchOptionSelected : (VCType) -> Unit,
  onTextChange: (String) -> Unit,
  onCloseClicked: () -> Unit,
  onSearchClicked: (String) -> Unit
){
    Surface(modifier = Modifier
        .fillMaxWidth()
      //  .height(56.dp),
       , elevation = AppBarDefaults.TopAppBarElevation,
        color = MaterialTheme.colors.AppBar
    ) {
        Column {
        TextField(modifier = Modifier.fillMaxWidth(),
        value = text,
            onValueChange = {
                onTextChange(it)
            },
            placeholder = {
                Text(
                    modifier = Modifier.alpha(ContentAlpha.medium),
                    text = "Search here ...",
                    color = MaterialTheme.colors.inputTextColor
                )
            },
            textStyle = TextStyle(
                fontSize = MaterialTheme.typography.subtitle1.fontSize
            ),
            singleLine = true,
            leadingIcon = {
                IconButton(
                modifier = Modifier.alpha(ContentAlpha.medium),
                    onClick = { /*TODO*/ }
                ) {
                    Icon(imageVector = Icons.Default.Search, contentDescription = "Search Icon")
                }
            },
            trailingIcon = {
                IconButton(
                    onClick = {
                        if(text.isNotEmpty()){
                            onTextChange("")
                        }else{
                            onCloseClicked()
                        }
                    }
                ) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Close Icon")
                }
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = { onSearchClicked(text) }
            ),
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.Transparent,
                cursorColor = Color.White.copy(alpha = ContentAlpha.medium)
            )
        )
        ExpandableSearchCard{
            onSearchOptionSelected(it)
        }
        }
    }
}

@Composable
fun MultipleVCOperationAppBar(
    onProceedVCOperation : (MultipleVCOperations) -> Unit
){
    Surface(modifier = Modifier
        .fillMaxWidth()
        .height(56.dp),
         elevation = AppBarDefaults.TopAppBarElevation,
        color = MaterialTheme.colors.AppBar
    ) {
       Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.End){
           TextButton(onClick = {onProceedVCOperation(MultipleVCOperations.ON_MULTIPLE_VERIFICATION)}) {
               Text(text = "Verify")
               IconButton(onClick = {  }) {
                   Icon(Icons.Default.Send, contentDescription = "verify Button Icon")
               }
           }
           TextButton(onClick = {onProceedVCOperation(MultipleVCOperations.ON_CANCEL)}) {
               Text(text = "Cancel")
               IconButton(onClick = {  }) {
                   Icon(Icons.Default.Cancel, contentDescription = "cancel Button Icon")
               }
           }
           TextButton(onClick = {onProceedVCOperation(MultipleVCOperations.ON_MULTIPLE_DELETE)}) {
               Text(text = "Delete")
               IconButton(onClick = {  }) {
                   Icon(Icons.Default.Delete, contentDescription = "deletion Button Icon")
               }
           }
       }
    }
}


@Preview(showBackground = true)
@Composable
fun previewAppBar(){
    AppBar({},{})
}