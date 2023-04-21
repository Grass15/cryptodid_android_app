package com.learning.walletv21.presentation.home.vc


import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.sp
import com.learning.walletv21.presentation.home.vc.VCViewModel.VCDataDisplayState
import com.learning.walletv21.presentation.theme.*


@ExperimentalMaterialApi
@Composable
fun CardSwiper(
    VCState: VCDataDisplayState,
    onDeleteButtonClicked: (VCDataDisplayState) -> Unit,
    onVerifyButtonClicked: (VCDataDisplayState) -> Unit,
) {
    var bgColor by remember {
      mutableStateOf(Color.Gray)
    }
    //val color = animateColorAsState(targetValue = bgColor, animationSpec = tween(durationMillis = 2000))

    val squareSize = 150.dp
    val swipeableState = rememberSwipeableState(initialValue = 0)
    val sizePx = with(LocalDensity.current){squareSize.toPx()}
    val anchors = mapOf(0f to 0, sizePx to 1)

         Box(modifier = Modifier
             .fillMaxWidth()
             .clip(RoundedCornerShape(8.dp))
             .swipeable(
                 state = swipeableState,
                 anchors = anchors,
                 thresholds = { _, _ ->
                     FractionalThreshold(0.3f)
                 },
                 orientation = Orientation.Horizontal
             )){


             Column(/*modifier = Modifier.background(MaterialTheme.colors.CardBackGround),*/ verticalArrangement = Arrangement.SpaceBetween) {
                 IconButton(onClick = {
                    onDeleteButtonClicked(VCState)
                 },
                 modifier = Modifier
                     .size(50.dp)
                     .clip(CircleShape)
                     //.background(Color.LightGray)
                     ) {
                      Icon(imageVector = Icons.Default.Delete, contentDescription = "", tint = Color.Red)
                 }
                 Spacer(modifier = Modifier.height(10.dp))
                 IconButton(onClick = {
                     onVerifyButtonClicked(VCState)
                 },
                     modifier = Modifier
                         .size(50.dp)
                         .clip(CircleShape)
                         //.background

                 ) {
                     Icon(imageVector = Icons.Default.Send, contentDescription = "Verify", tint = MaterialTheme.colors.OpsIcons)
                 }

             }
             Box(modifier = Modifier
                 .offset {
                     IntOffset(swipeableState.offset.value.roundToInt(), 0)
                 }
                 .clip(RoundedCornerShape(8.dp))
                 .fillMaxWidth()
                 .size(120.dp)
                 .fillMaxHeight()
                 .background(color = MaterialTheme.colors.CardForGround)
                 .align(Alignment.CenterStart)
             ){
                Row(modifier = Modifier
                    .fillMaxSize()
                ) {
                  Column(modifier = Modifier
                      .fillMaxSize()
                      .padding(5.dp)
                      .weight(3f),
                      verticalArrangement = Arrangement.SpaceBetween

                  ) {
                      Text(text = VCState.VCTitle,color = MaterialTheme.colors.inputTextColor, style = Typography.h4)
                      Text(text = VCState.experationDate.toString(),color = MaterialTheme.colors.inputTextColor, style = Typography.body2, fontStyle = FontStyle.Italic)
                      
                  }
                    Column(modifier = Modifier
                        .fillMaxSize()
                        .padding(5.dp)
                        .weight(7f)) {
                        Text(text = VCState.issuerName,color = MaterialTheme.colors.inputTextColor, style = Typography.body1, fontSize = 15.sp)
                        Spacer(modifier = Modifier.height(7.dp))
                        Text(text = VCState.VCType,color = MaterialTheme.colors.inputTextColor, style = Typography.body1)
                        Text(text = VCState.VCContentOverview,color = MaterialTheme.colors.inputTextColor, style = Typography.body1)
                    }
                }
             }
         }

        }



@OptIn(ExperimentalMaterialApi::class)
@Preview(showBackground = true)
@Composable
fun previewcardSwiper(){

}