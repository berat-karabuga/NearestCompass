package com.stargazer.nearestcompass.utils

import com.stargazer.nearestcompass.data.model.LocationData
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.math.sin
import kotlin.math.sqrt

object LocationsUtils {

    fun calculateDistance(
        from: LocationData,
        to: LocationData
    ): Double{
        val earthRadius = 6371000.0 // Dünya'nın yarıçapı (metre)

        val lat1 = Math.toRadians(from.latitude)
        val lat2 = Math.toRadians(to.latitude)
        val deltaLat = Math.toRadians(to.latitude - from.latitude)
        val deltaLon = Math.toRadians(to.longitude - from.longitude)

        val a = sin(deltaLat / 2).pow(2) +
                cos(lat1) * cos(lat2) *
                sin(deltaLon / 2).pow(2)

        val c = 2 * atan2(sqrt(a), sqrt(1 - a))

        return earthRadius * c
    }

    fun caculateBearing(
        from: LocationData,
        to: LocationData
    ): Float {
        val lat1 = Math.toRadians(from.latitude)
        val lat2 = Math.toRadians(to.latitude)
        val deltaLon = Math.toRadians(to.longitude - from.longitude)

        val y = sin(deltaLon) * cos(lat2)
        val x = cos(lat1) * sin(lat2) -
                sin(lat1) * cos(lat2) * cos(deltaLon)

        val bearing = Math.toDegrees(atan2(y, x))

        return ((bearing + 360) % 360).toFloat()
    }

    fun formatDistance(meters: Double): String{
        return when {
            meters < 1000 -> "${meters.roundToInt()}m"
            else -> "${"%.1f".format(meters / 1000)}km"
        }
    }

    fun getDirectionText(bearing: Float): String {
        return when{
            bearing < 22.5 || bearing >= 337.5 -> "continue straight"
            bearing < 67.5  -> "Turn Slightly Right"
            bearing < 112.5 -> "Turn Right"
            bearing < 157.5 -> "Take a Sharp Right Turn"
            bearing < 202.5 -> "Turn Back"
            bearing < 247.5 -> "Take a Sharp Left Turn"
            bearing < 292.5 -> "Turn Left"
            bearing < 337.5 -> "Turn Slightly Left"
            else -> "Continue Straight"
        }
    }
}