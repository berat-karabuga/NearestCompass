package com.stargazer.nearestcompass.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.stargazer.nearestcompass.data.location.CompassManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CompassViewModel(application: Application) : AndroidViewModel(application) {

    private val compassManager = CompassManager(application)

    private val _deviceAzimuth = MutableStateFlow(0f)
    val deviceAzimuth: StateFlow<Float> = _deviceAzimuth.asStateFlow()

    private val _targetBearing = MutableStateFlow(0f)
    val targetBearing: StateFlow<Float> = _targetBearing.asStateFlow()

    private val _compassRotation = MutableStateFlow(0f)
    val compassRotation: StateFlow<Float> = _compassRotation.asStateFlow()

    private val _isCompassActive = MutableStateFlow(false)
    val isCompassActive: StateFlow<Boolean> = _isCompassActive.asStateFlow()

    fun startCompass(){
        viewModelScope.launch {
            if(!compassManager.isSensorsAvailable()){
                return@launch
            }
            _isCompassActive.value = true

            compassManager.getCompassFlow().collect{azimuth ->
                _deviceAzimuth.value = azimuth
                updateCompassRotation()
            }
        }
    }

    fun stopCompass(){
        _isCompassActive.value = false
    }

    fun updateTargetBearing(bearing: Float){
        _targetBearing.value = bearing
        updateCompassRotation()
    }

    private fun updateCompassRotation(){
        val rotation = _targetBearing.value - deviceAzimuth.value
        _compassRotation.value = rotation
    }

    override fun onCleared() {
        super.onCleared()
        stopCompass()
    }
}