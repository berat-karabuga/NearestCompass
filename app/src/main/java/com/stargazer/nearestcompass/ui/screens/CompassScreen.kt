package com.stargazer.nearestcompass.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.stargazer.nearestcompass.ui.components.CompassView
import com.stargazer.nearestcompass.viewmodel.CompassViewModel
import com.stargazer.nearestcompass.viewmodel.LocationViewModel
import com.stargazer.nearestcompass.viewmodel.StoreViewModel
import com.stargazer.nearestcompass.utils.LocationsUtils
import com.stargazer.nearestcompass.data.model.LocationData

@Composable
fun CompassScreen(
    compassViewModel: CompassViewModel = viewModel(),
    locationViewModel: LocationViewModel = viewModel(),
    storeViewModel: StoreViewModel = viewModel()
) {
    val deviceAzimuth by compassViewModel.deviceAzimuth.collectAsState()
    val compassRotation by compassViewModel.compassRotation.collectAsState()
    val currentLocation by locationViewModel.currentLocation.collectAsState()
    val nearestStore by storeViewModel.nearestStore.collectAsState()
    val isLoading by storeViewModel.isLoading.collectAsState()
    val errorMessage by storeViewModel.errorMessage.collectAsState()

    LaunchedEffect(Unit) {
        compassViewModel.startCompass()
        locationViewModel.startLocationUpdates()
    }

    LaunchedEffect(currentLocation) {
        currentLocation?.let { location ->
            storeViewModel.searchNearbyStores(location)
        }
    }

    LaunchedEffect(nearestStore, currentLocation) {
        if (nearestStore != null && currentLocation != null) {
            val targetLocation = LocationData(
                latitude = nearestStore!!.latitude,
                longitude = nearestStore!!.longitude,
                accuracy = 0f
            )

            val bearing = LocationsUtils.caculateBearing(
                from = currentLocation!!,
                to = targetLocation
            )

            compassViewModel.updateTargetBearing(bearing)
        }
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
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(50.dp))

        if (currentLocation != null) {
            Text(
                text = "Location: ${currentLocation!!.latitude.format(6)}, ${currentLocation!!.longitude.format(6)}",
                fontSize = 14.sp,
                fontFamily = FontFamily.SansSerif,
                color = Color.LightGray,
                textAlign = TextAlign.Center
            )

            Text(
                text = "Accuracy: ${currentLocation!!.accuracy.toInt()}m",
                fontSize = 12.sp,
                fontFamily = FontFamily.SansSerif,
                color = Color.Gray,
                textAlign = TextAlign.Center
            )
        } else {
            CircularProgressIndicator(color = Color.White)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Location being obtained...",
                fontSize = 16.sp,
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        when {
            isLoading -> {
                CircularProgressIndicator(color = Color.White)
                Text(
                    text = "Searching for stores...",
                    fontSize = 16.sp,
                    color = Color.White
                )
            }
            errorMessage != null -> {
                Text(
                    text = errorMessage!!,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.error,
                    textAlign = TextAlign.Center
                )
            }
            nearestStore != null -> {
                Text(
                    text = "Your distance to ${nearestStore!!.name} is ${LocationsUtils.formatDistance(nearestStore!!.distance)}",
                    fontSize = 20.sp,
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
            }
            else -> {
                Text(
                    text = "No stores found nearby",
                    fontSize = 16.sp,
                    color = Color.Gray
                )
            }
        }

        Spacer(modifier = Modifier.height(40.dp))

        CompassView(
            rotation = compassRotation,
            modifier = Modifier.size(300.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Direction: ${deviceAzimuth.toInt()}°",
            fontSize = 14.sp,
            fontFamily = FontFamily.Monospace,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(32.dp))

        if (nearestStore != null) {
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
}

private fun Double.format(decimals: Int): String {
    return "%.${decimals}f".format(this)
}