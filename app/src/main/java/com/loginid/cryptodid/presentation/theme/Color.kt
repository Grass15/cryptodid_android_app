package com.loginid.cryptodid.presentation.theme

import androidx.compose.material.Colors
import androidx.compose.ui.graphics.Color

val Purple200 = Color(0xFFBB86FC)
val Purple500 = Color(0xFF6200EE)
val Purple700 = Color(0xFF3700B3)
val Teal200 = Color(0xFF03DAC5)
val Blue200 = Color(0xFF73e8ff)
val Blue500 = Color(0xFF29b6f6)
val Blue700 = Color(0xFF0086c3)
val black500 = Color(0xFF2D3436)
val black300 = Color(0xFF34495E)
val TelegramBlue1 = Color(0xFF2980b9)
val TelegramBlue2 = Color(0xFF3498DB)
val TelegramGray2=  Color(0xFF2F3640)
val TelegramBackgroundBlack1 = Color(181818)
val TelegramBackgroundBlack2 = Color(0xFF181818)
val TelegramBackground2 = Color(0xFFFFFFFF)

//Cards

val CardBackGroundColor1 = Color(0xFFBBDEFB)
val CardBackGroundColor2 = Color(0xFF2A2550)

val CardForColor1 = Color(0xFFF1F6F9)
val CardForColor2 = Color(0xFF212121)


val whiteBackground = Color(0xFFF7F7F7)

val Colors.firstBackGroundColor: Color
     get() = if(isLight) Color.White else black500

val Colors.secondBackGroundColor: Color
     get() = if(isLight) whiteBackground else black300

val Colors.topAppContentColor: Color
     get() = if(isLight) Color.White else Color.LightGray

val Colors.inputTextColor: Color
     get() = if(isLight) Color.Black else Color.White

val Colors.AppBar: Color
     get() = if(isLight) TelegramBlue1 else TelegramBackgroundBlack1

val Colors.HomeBackGround: Color
     get() = if(isLight) TelegramBackground2  else TelegramBackgroundBlack2

val Colors.RandomMaterial: Color
     get() = if(isLight) TelegramBlue1 else TelegramGray2


val Colors.CardBackGround: Color
     get() = if(isLight) CardBackGroundColor1  else CardBackGroundColor2

val Colors.CardForGround: Color
     get() = if(isLight) CardForColor1  else CardForColor2

val Colors.OpsIcons: Color
     get() = if(isLight) TelegramBlue1 else TelegramBlue2

