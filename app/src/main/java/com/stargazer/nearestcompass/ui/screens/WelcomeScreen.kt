package com.stargazer.nearestcompass.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.rememberPermissionState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun WelcomeScreen(
    onNavigateToCompass: () -> Unit
){

    //konum izinlerinin yönetildiği kısım
    val locationPermissionsState = rememberMultiplePermissionsState(
        permissions = listOf(
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFF2b2d30))
            , horizontalAlignment = Alignment.CenterHorizontally
            , verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Nearest Compass",
            fontSize = 40.sp,
            fontFamily = FontFamily.SansSerif,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )

        Spacer(modifier = Modifier.padding(horizontal = 24.dp))

        Text(
            text = "Find nearest!",
            fontSize = 16.sp,
            fontFamily = FontFamily.SansSerif,
            fontStyle = FontStyle.Italic,
            color = Color.White
        )

        Spacer(modifier = Modifier.padding(horizontal =  48.dp))

        Button(
            modifier = Modifier
                .padding(horizontal = 32.dp)
                .fillMaxWidth()
                ,colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White
                ),
            onClick = {
                /*buttona nasıldığı an izinler verildi mi kontrol etsin
                izinler verildi ise direkt geçsin verilmemiş ise izinleri istesin*/
                if (locationPermissionsState.allPermissionsGranted){
                    onNavigateToCompass()
                } else{
                    locationPermissionsState.launchMultiplePermissionRequest()
                }
            }
        ) {
            Text(
                text = "Let's Find",
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        }

        //kullanıcı izin vermeyi redderse bilgilendirme mesajı

        if (locationPermissionsState.shouldShowRationale){
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "The app cannot work without location permission. Please grant permission.",
                color = MaterialTheme.colorScheme.error,
                textAlign = TextAlign.Center,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        }

    }

    //eğer izinler veriliyse bu sayfayı göstermeden direkt devam edecek
    LaunchedEffect(locationPermissionsState.allPermissionsGranted) {
        if (locationPermissionsState.allPermissionsGranted){
            onNavigateToCompass()
        }
    }


}

