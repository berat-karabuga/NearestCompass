package com.stargazer.nearestcompass.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.stargazer.nearestcompass.data.model.LocationData
import com.stargazer.nearestcompass.data.model.Store
import com.stargazer.nearestcompass.data.repository.OverpassRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class StoreViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = OverpassRepository()

    private val _stores = MutableStateFlow<List<Store>>(emptyList())
    val stores: StateFlow<List<Store>> = _stores.asStateFlow()

    private val _nearestStore = MutableStateFlow<Store?>(null)
    val nearestStore: StateFlow<Store?> = _nearestStore.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()


    fun searchNearbyStores(userLocation: LocationData, radiusMeters: Int = 2000) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            Log.d("StoreViewModel", "Searching stores around: ${userLocation.latitude}, ${userLocation.longitude}")

            val result = repository.getNearbyStores(
                latitude = userLocation.latitude,
                longitude = userLocation.longitude,
                radiusMeters = radiusMeters
            )

            result.fold(
                onSuccess = { storeList ->
                    Log.d("StoreViewModel", "Found ${storeList.size} stores")

                    _stores.value = storeList

                    _nearestStore.value = storeList.firstOrNull()

                    if (storeList.isEmpty()) {
                        _errorMessage.value = "Yakında market/bakkal bulunamadı"
                    }
                },
                onFailure = { exception ->
                    Log.e("StoreViewModel", "Error searching stores", exception)
                    _errorMessage.value = "Market arama hatası: ${exception.message}"
                }
            )

            _isLoading.value = false
        }
    }

    fun refreshStores(userLocation: LocationData) {
        searchNearbyStores(userLocation)
    }

    fun clearError() {
        _errorMessage.value = null
    }
}