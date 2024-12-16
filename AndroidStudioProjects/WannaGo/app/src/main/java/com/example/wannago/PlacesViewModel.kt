package com.example.wannago

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore

class PlacesViewModel(application: Application) : AndroidViewModel(application) {
    private val TAG = "PlacesViewModel"

    private val firestore: FirebaseFirestore by lazy {
        try {
            if (FirebaseApp.getApps(application).isEmpty()) {
                FirebaseApp.initializeApp(application)
            }
            FirebaseFirestore.getInstance()
        } catch (e: Exception) {
            Log.e(TAG, "Error initializing Firestore", e)
            throw e
        }
    }

    private val _markers = MutableLiveData<List<MarkerLocation>>(emptyList())
    val markers: LiveData<List<MarkerLocation>> = _markers

    init {
        fetchMarkers()
    }

    fun addMarker(marker: MarkerLocation) {
        Log.d(TAG, "Adding marker: $marker")
        val documentRef = firestore.collection("locations").document()
        val firestoreMarker = marker.copy(id = documentRef.id)

        documentRef.set(firestoreMarker)
            .addOnSuccessListener {
                Log.d(TAG, "Successfully added marker with ID: ${documentRef.id}")
                fetchMarkers()
            }
            .addOnFailureListener { exception ->
                Log.e(TAG, "Error adding marker", exception)
            }
    }

    // In PlacesViewModel.kt

    private fun fetchMarkers() {
        Log.d(TAG, "Fetching markers")
        firestore.collection("locations")
            .get()
            .addOnSuccessListener { querySnapshot ->
                try {
                    val fetchedMarkers = querySnapshot.documents.mapNotNull { doc ->
                        doc.toObject(MarkerLocation::class.java)?.copy(id = doc.id)
                    }
                    Log.d(TAG, "Fetched ${fetchedMarkers.size} markers")
                    _markers.value = fetchedMarkers
                } catch (e: Exception) {
                    Log.e(TAG, "Error parsing markers", e)
                    _markers.value = emptyList()
                }
            }
            .addOnFailureListener { exception ->
                Log.e(TAG, "Error fetching markers", exception)
                _markers.value = emptyList()
            }
    }

    fun deleteMarker(marker: MarkerLocation) {
        Log.d(TAG, "Deleting marker: $marker")
        firestore.collection("locations")
            .document(marker.id)
            .delete()
            .addOnSuccessListener {
                Log.d(TAG, "Successfully deleted marker with ID: ${marker.id}")
                fetchMarkers()
            }
            .addOnFailureListener { exception ->
                Log.e(TAG, "Error deleting marker", exception)
            }
    }
}