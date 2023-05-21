package com.loginid.cryptodid.presentation.home.vc


import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.HowToVote
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.sp
import com.loginid.cryptodid.data.local.entity.VCType
import com.loginid.cryptodid.presentation.home.vc.VCViewModel.VCDataDisplayState
import com.loginid.cryptodid.presentation.theme.*
import java.text.SimpleDateFormat
import java.util.*


@ExperimentalMaterialApi
@Composable
fun CardSwiper(
    VCState: VCDataDisplayState,
    onDeleteButtonClicked: (VCDataDisplayState) -> Unit,
    onVerifyButtonClicked: (VCDataDisplayState) -> Unit,
    onDisplayCheckBoxes : (Boolean) -> Unit,
    displaCheckBox: Boolean = false,
    onCheckBoxChecked: (Boolean,VCDataDisplayState) -> Unit
) {
    //Checkboxex
    val (isCheckboxChecked, setIsCheckboxChecked) = remember { mutableStateOf(false) }
    //var displaCheckBox by remember { mutableStateOf(false) }

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
             .pointerInput(Unit) {
                 detectTapGestures(
                     onLongPress = {
                         onDisplayCheckBoxes(true)
                     },
                     onPress = {
                         onDisplayCheckBoxes(false)
                         /*displaCheckBox = false
                         setIsCheckboxChecked(false)*/
                     }
                 )
             }
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
                     if(VCState.VCTypeEnum == VCType.PRIVILEGE){
                         Icon(imageVector = Icons.Default.HowToVote, contentDescription = "Vote", tint = Color.Yellow)
                     }else{
                         Icon(imageVector = Icons.Default.Send, contentDescription = "Verify", tint = MaterialTheme.colors.OpsIcons)
                     }
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

                 Column{
                     //First element ==> Title
                     Column(
                         modifier = Modifier
                             .fillMaxSize()
                             .padding(5.dp)
                             .weight(3f),
                         verticalArrangement = Arrangement.SpaceBetween,
                         horizontalAlignment = Alignment.CenterHorizontally
                     ) {
                         Text(text = VCState.VCTitle,color = MaterialTheme.colors.inputTextColor, style = Typography.h4)
                     }

                     //Second element ==> content overview
                     Column(
                         modifier = Modifier
                             .fillMaxSize()
                             .padding(5.dp)
                             .weight(2f),
                         verticalArrangement = Arrangement.SpaceBetween,
                         horizontalAlignment = Alignment.CenterHorizontally
                     ) {
                         Text(text = VCState.issuerName,color = MaterialTheme.colors.inputTextColor, style = Typography.body1)
                     }
                     //Third  element ==> date
                     Column(
                         modifier = Modifier
                             .fillMaxSize()
                             .padding(5.dp)
                             .weight(2f),
                         verticalArrangement = Arrangement.SpaceBetween,
                         horizontalAlignment = Alignment.End
                     ) {
                         val format = SimpleDateFormat("MMMM d HH:mm:ss", Locale.ENGLISH)
                         Text(text = format.format(VCState.experationDate).toString(),color = MaterialTheme.colors.inputTextColor, style = Typography.body2, fontStyle = FontStyle.Italic)
                     }
                     if(displaCheckBox){
                         Checkbox(
                             checked = isCheckboxChecked,
                             onCheckedChange = {
                                 setIsCheckboxChecked(it)
                                 onCheckBoxChecked(it,VCState)
                             }
                         )
                     }else{
                         setIsCheckboxChecked(false)
                     }
                 }

                 //Old layout
               /* Row(modifier = Modifier
                    .fillMaxSize()
                ) {
                  Column(modifier = Modifier
                      .fillMaxSize()
                      .padding(5.dp)
                      .weight(3f),
                      verticalArrangement = Arrangement.SpaceBetween

                  ) {
                      val format = SimpleDateFormat("MMMM d HH:mm:ss", Locale.ENGLISH)
                      Text(text = VCState.VCTitle,color = MaterialTheme.colors.inputTextColor, style = Typography.h4)
                      Text(text = format.format(VCState.experationDate).toString(),color = MaterialTheme.colors.inputTextColor, style = Typography.body2, fontStyle = FontStyle.Italic)
                      
                  }
                    Column(modifier = Modifier
                        .fillMaxSize()
                        .padding(5.dp)
                        .weight(7f)) {
                        Text(text = VCState.issuerName,color = MaterialTheme.colors.inputTextColor, style = Typography.body1, fontSize = 15.sp)
                        Spacer(modifier = Modifier.height(7.dp))
                        Text(text = VCState.VCTypeText,color = MaterialTheme.colors.inputTextColor, style = Typography.body1)
                        Text(text = VCState.VCContentOverview,color = MaterialTheme.colors.inputTextColor, style = Typography.body1)
                    }
                    if(displaCheckBox){
                        Checkbox(
                            checked = isCheckboxChecked,
                            onCheckedChange = {
                                setIsCheckboxChecked(it)
                                onCheckBoxChecked(it,VCState)
                            }
                        )
                    }else{
                        setIsCheckboxChecked(false)
                    }
                }*/
             }
         }

        }



@OptIn(ExperimentalMaterialApi::class)
@Preview(showBackground = true)
@Composable
fun previewcardSwiper(){
    CardSwiper(VCDataDisplayState(experationDate = Date(),issuerName = "preview", VCTypeText = "None", VCTitle = "hello", VCContentOverview = "Nooone", VCID = "fefe"),{},{},{},false){
        a,b->
    }
}
/*
 val experationDate: Date? = null,//Date(),
    val issuerName: String,
    val VCTypeText: String,
    val VCTypeEnum:VCType?=VCType.DEFAULT,
    val VCTitle: String,
    val VCContentOverview: String,
    val status: Status=Status.NO_ACTION,
    val VCID: String,
    val rawVC: Claim? = null
 */