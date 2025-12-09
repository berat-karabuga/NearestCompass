package com.stargazer.nearestcompass.data.location

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Looper
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class LocationManager (private val context: Context) {

    //googleın konum servisi
    private val fusedLocationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
    fun getLocationUpdates(): Flow<Location> = callbackFlow {

        if(!hasLocationPermission()){
            close(Exception("no location permission"))
            return@callbackFlow
        }

        //konum güncellemerini ayarlarla
        val locationRequest = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY,  //en yüksek doğruluk
            5000L //her 5 saniyede yeniden güncellensin
        ).apply {
            setMinUpdateIntervalMillis(2000L) //arama süresi en az 2sn olsun
            setWaitForAccurateLocation(true) //doğru konumu bekle
        }.build()


        //konum güncellemelerini dinleyen kısım
        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                // her yeni konum geldiğinde floowa gönderiyor yeni konumu
                result.lastLocation?.let { location ->
                    trySend(location)
                }
            }
        }


        //konum güncellemelerini başlat
        try {
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
        } catch (e: SecurityException) {
            close(e)
        }

        awaitClose{
            fusedLocationClient.removeLocationUpdates(locationCallback)
        }
    }

    //konum izninin verilip verilmediğinin kontrolü
    private  fun  hasLocationPermission():Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    //son bilinen konumu al
    suspend fun getLastKnownLocation(): Location? {
        if (!hasLocationPermission()) return null
        return try {
            fusedLocationClient.lastLocation.await()
        } catch (e: SecurityException){
            null
        }catch (e: Exception){
            null
        }
    }
}

/*
normalde sonun böyle olması gerekiyordu ama sürekli hata aldığım için değiştirdim
return try {
    fusedLocationClient.lastLocation.await() //özellikle burada hata alıyom
} catch (e: Exception){
    null
}*/