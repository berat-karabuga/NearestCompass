package com.stargazer.nearestcompass.data.model

data class LocationData(
    val latitude: Double, //enlem
    val longitude: Double, //boylam
    val accuracy:Float   //doğruluk (metre)
) {

    companion object{
        fun from (location: android.location.Location): LocationData {
            return LocationData(
                latitude = location.latitude,
                longitude = location.longitude,
                accuracy = location.accuracy
            )
        }
    }
}