package com.stargazer.nearestcompass.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.stargazer.nearestcompass.ui.components.CompassView

@Composable
fun CompassScreen(){

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF2b2d30))
            ,horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.padding(75.dp))

        Text(
            text = "Your distance to your target named Helin is 150 meters",
            fontSize = 20.sp,
            fontFamily = FontFamily.SansSerif,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(24.dp)
        )

        Spacer(modifier = Modifier.padding(55.dp))

        //pusulanın geleceği alan şimdilik card
        CompassView(
            rotation = 0f,
            modifier = Modifier.size(300.dp)
        )

        Spacer(modifier = Modifier.padding(35.dp))

        Text(
            text = "Continue Straight",
            fontSize = 18.sp,
            fontFamily = FontFamily.SansSerif,
            fontStyle = FontStyle.Italic,
            color = Color.White
        )


    }


}


@Preview
@Composable
fun CompassScreenPreview() {
    CompassScreen()
}