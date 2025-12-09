package com.stargazer.nearestcompass.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.stargazer.nearestcompass.data.location.LocationManager
import com.stargazer.nearestcompass.data.model.LocationData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LocationViewModel(application: Application): AndroidViewModel(application) {

    private val locationManager = LocationManager(application)

    private val _currentLocation = MutableStateFlow<LocationData?>(null)

    val currentLocation: StateFlow<LocationData?> = _currentLocation.asStateFlow()

    private val _isLocationUpdatesActive = MutableStateFlow(false)
    val isLocationUpdatesActive: StateFlow<Boolean> = _isLocationUpdatesActive.asStateFlow()

    fun startLocationUpdates(){
        viewModelScope.launch {
            _isLocationUpdatesActive.value = true

            locationManager.getLocationUpdates().collect{location ->
                val locationData = LocationData.from(location)
                _currentLocation.value = locationData
            }
        }
    }

    fun stopLocationUpdates(){
        _isLocationUpdatesActive.value = false
    }

    override fun onCleared() {
        super.onCleared()
        stopLocationUpdates()
    }
}