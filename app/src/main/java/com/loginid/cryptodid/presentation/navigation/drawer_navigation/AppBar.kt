package com.loginid.cryptodid.presentation.navigation.drawer_navigation

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
  onTextChange: (String) -> Unit,
  onCloseClicked: () -> Unit,
  onSearchClicked: (String) -> Unit
){
    Surface(modifier = Modifier
        .fillMaxWidth()
        .height(56.dp),
        elevation = AppBarDefaults.TopAppBarElevation,
        color = MaterialTheme.colors.AppBar
    ) {
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
    }
}

@Preview(showBackground = true)
@Composable
fun previewAppBar(){
    AppBar({},{})
}