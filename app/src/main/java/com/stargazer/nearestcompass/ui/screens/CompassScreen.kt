package com.stargazer.nearestcompass.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.stargazer.nearestcompass.ui.components.CompassView
import com.stargazer.nearestcompass.utils.LocationsUtils
import com.stargazer.nearestcompass.viewmodel.CompassViewModel
import com.stargazer.nearestcompass.viewmodel.LocationViewModel

@Composable
fun CompassScreen(
    compassViewModel: CompassViewModel = viewModel(),
    locationViewModel: LocationViewModel = viewModel()
){
    val deviceAzimuth by compassViewModel.deviceAzimuth.collectAsState()
    val compassRotation by compassViewModel.compassRotation.collectAsState()
    val currentLocation by locationViewModel.currentLocation.collectAsState()

    LaunchedEffect(Unit) {
        compassViewModel.startCompass()
        locationViewModel.startLocationUpdates()
    }

    DisposableEffect(Unit) {
        onDispose {
            compassViewModel.stopCompass()
            locationViewModel.stopLocationUpdates()
        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF2b2d30))
            .padding(24.dp)
            ,horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.padding(50.dp))

        if ( currentLocation !=null){
            Text(
                text = "Locaiton: ${currentLocation!!.latitude.format(6)}, ${currentLocation!!.longitude.format(6)}",
                fontSize = 16.sp,
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center
            )

            Text(
                text = "Accuracy: ${currentLocation!!.accuracy.toInt()}m",
                fontSize = 14.sp,
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.Center
            )
        } else {
            //konum henüz alınmadıysa
            CircularProgressIndicator(color = Color.White)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "location being obtained...",
                fontSize = 16.sp,
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Your distance to your target named Helin is 150 meters",
            fontSize = 20.sp,
            fontFamily = FontFamily.SansSerif,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(40.dp))

        CompassView(
            rotation = compassRotation,
            modifier = Modifier.size(300.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Continue: ${deviceAzimuth.toInt()}",
            fontSize = 18.sp,
            fontFamily = FontFamily.Monospace,
            fontStyle = FontStyle.Italic,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(32.dp))

        val directionText = LocationsUtils.getDirectionText(compassRotation)
        Text(
            text = directionText,
            fontSize = 18.sp,
            fontFamily = FontFamily.SansSerif,
            fontStyle = FontStyle.Italic,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
    }
}

private fun Double.format(decimals: Int): String{
    return  "%.${decimals}f".format(this)
}


@Preview
@Composable
fun CompassScreenPreview() {
    CompassScreen()
}