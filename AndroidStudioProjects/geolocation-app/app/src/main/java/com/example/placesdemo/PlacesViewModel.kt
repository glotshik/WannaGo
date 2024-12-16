package com.example.placesdemo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PlacesViewModel : ViewModel() {
    private val _markers = MutableLiveData<List<MarkerLocation>>(emptyList())
    val markers: LiveData<List<MarkerLocation>> = _markers

    fun addMarker(marker: MarkerLocation) {
        val currentMarkers = _markers.value?.toMutableList() ?: mutableListOf()
        currentMarkers.add(marker)
        _markers.value = currentMarkers
    }
}