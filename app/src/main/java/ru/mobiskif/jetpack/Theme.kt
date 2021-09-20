package ru.mobiskif.jetpack

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
val space = 14.dp
val small = 14.sp

val shapes = Shapes(
    small = RoundedCornerShape(space/2),
    medium = RoundedCornerShape(space),
    large = RoundedCornerShape(space*2)
)

val typography = Typography(
    defaultFontFamily = FontFamily.Default,
)

val DarkPalette = darkColors(
    primary = Color.Gray, //цвет Button и подписей
    //primaryVariant = Color.DarkGray, //цвет StatusBar
    secondary = Color.DarkGray, //цвет FAB и RadioButton
    //secondaryVariant = Color.Gray, //цвет Switch
    //surface = Color.Gray, //в темной теме цвет ActionBar
    onBackground = Color.LightGray,
    error = Color.White,
)

fun setLightPalette(context: Context, theme: String): Colors {
    when (theme) {
        "Фиолетовая" -> {
            LightPalette = lightColors(
                primary = Color(ContextCompat.getColor(context, R.color.primaryColor)), //цвет Button и подписей
                primaryVariant = Color(ContextCompat.getColor(context,R.color.primaryDarkColor)), //цвет StatusBar
                secondary = Color(ContextCompat.getColor(context,R.color.secondaryColor)), //цвет FAB и RadioButton
                secondaryVariant = Color(ContextCompat.getColor(context,R.color.secondaryDarkColor)), //цвет Switch
                surface = Color(ContextCompat.getColor(context,R.color.secondaryLightColor)),
                error = Color(ContextCompat.getColor(context, R.color.secondaryTextColor)),
            )
        }
        "Зеленая" -> {
            LightPalette = lightColors(
                primary = Color(android.graphics.Color.parseColor("#2e7d32")), //цвет Button и подписей
                primaryVariant = Color(android.graphics.Color.parseColor("#005005")), //цвет StatusBar
                secondary = Color(android.graphics.Color.parseColor("#60ad5e")), //цвет FAB и RadioButton
                secondaryVariant = Color(android.graphics.Color.parseColor("#2e7d32")), //цвет Switch
                error = Color(android.graphics.Color.parseColor("#005005")),
            )
        }
        "Красная" -> {
            LightPalette = lightColors(
                primary = Color(ContextCompat.getColor(context, R.color.primaryColor3)), //цвет Button и подписей
                primaryVariant = Color(ContextCompat.getColor(context,R.color.primaryDarkColor3)), //цвет StatusBar
                secondary = Color(ContextCompat.getColor(context,R.color.primaryLightColor3)), //цвет FAB и RadioButton
            )
        }
    }
    return LightPalette
}

@Composable
fun FixModes() {
    modFill = Modifier
        .fillMaxWidth()
        .background(MaterialTheme.colors.secondary, RoundedCornerShape(space))
        .padding(space)

    modBord = Modifier
        .fillMaxWidth()
        .border(1.dp, MaterialTheme.colors.secondary, RoundedCornerShape(space))
        .padding(space)

    mod09 = Modifier.fillMaxWidth(.7f)

    modFillVar = Modifier
        .fillMaxWidth()
        .background(MaterialTheme.colors.secondaryVariant, RoundedCornerShape(space))
        .padding(space)
}

@Composable
fun Theme(dark: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    MaterialTheme(
        colors = if (dark) { DarkPalette } else { LightPalette },
        typography = typography,
        shapes = shapes,
        content = content
    )
}