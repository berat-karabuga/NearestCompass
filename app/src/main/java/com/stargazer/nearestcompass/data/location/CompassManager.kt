package com.stargazer.nearestcompass.data.location

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlin.math.roundToInt

class CompassManager(context: Context) {
    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val accelerometer: Sensor? =sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    private val magnetometer: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)

    private val accelerometerReading = FloatArray(3)
    private val magnetometerReading = FloatArray(3)


    fun getCompassFlow(): Flow<Float> = callbackFlow {
        val listener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent){
                when(event.sensor.type){
                    Sensor.TYPE_ACCELEROMETER -> {
                        System.arraycopy(event.values, 0, accelerometerReading, 0, accelerometerReading.size)
                    }
                    Sensor.TYPE_MAGNETIC_FIELD ->{
                        System.arraycopy(event.values, 0, magnetometerReading, 0, magnetometerReading.size)
                    }
                }

                if (accelerometerReading[0] != 0f && magnetometerReading[0] != 0f){
                    updateOrientation()?.let{azimuth ->
                        trySend(azimuth)
                    }
                }
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int){

            }
        }

        accelerometer?.let {
            sensorManager.registerListener(
                listener,
                it,
                SensorManager.SENSOR_DELAY_UI
            )
        }

        magnetometer?.let {
            sensorManager.registerListener(
                listener,
                it,
                SensorManager.SENSOR_DELAY_UI
            )
        }

        awaitClose{
            sensorManager.unregisterListener(listener)
        }
    }

    private fun updateOrientation(): Float?{
        val rotationMatrix = FloatArray(9)
        val orientationAngels = FloatArray(3)

        val success = SensorManager.getRotationMatrix(
            rotationMatrix,
            null,
            accelerometerReading,
            magnetometerReading
        )

        if (success){
            SensorManager.getOrientation(rotationMatrix, orientationAngels)

            val azimuthInRadians = orientationAngels[0]
            val azimuthInDegrees = Math.toDegrees(azimuthInRadians.toDouble()).toFloat()
            val normalizedAzimuth = (azimuthInDegrees + 360) % 360
            return normalizedAzimuth.roundToInt().toFloat()
        }
        return null
    }

    fun isSensorsAvailable(): Boolean{
        return accelerometer !=null && magnetometer != null
    }
}