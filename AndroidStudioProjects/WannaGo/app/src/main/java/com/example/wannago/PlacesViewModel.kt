package com.example.wannago

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject

class PlacesViewModel(application: Application) : AndroidViewModel(application) {

    init {
        // This ensures Firebase is initialized for this context
        FirebaseApp.initializeApp(getApplication())
    }

    private val firestore = FirebaseFirestore.getInstance()
    private val _markers = MutableLiveData<List<MarkerLocation>>(emptyList())
    val markers: LiveData<List<MarkerLocation>> = _markers

    init {
        fetchMarkers()
    }

    fun addMarker(marker: MarkerLocation) {
        // Add to Firestore
        val documentRef = firestore.collection("locations").document()
        val firestoreMarker = marker.copy(id = documentRef.id)

        documentRef.set(firestoreMarker)
            .addOnSuccessListener {
                // Fetch updated markers after successful addition
                fetchMarkers()
            }
            .addOnFailureListener { exception ->
                // Handle error, maybe log or show a toast
                // Consider adding error logging or user feedback
            }
    }

    private fun fetchMarkers() {
        firestore.collection("locations")
            .get()
            .addOnSuccessListener { querySnapshot ->
                val fetchedMarkers = querySnapshot.documents.mapNotNull {
                    it.toObject(MarkerLocation::class.java)
                }
                _markers.value = fetchedMarkers
            }
            .addOnFailureListener { exception ->
                // Handle potential errors in fetching markers
                // Consider logging or showing an error message
            }
    }

    fun deleteMarker(marker: MarkerLocation) {
        firestore.collection("locations")
            .document(marker.id)
            .delete()
            .addOnSuccessListener {
                fetchMarkers()
            }
            .addOnFailureListener { exception ->
                // Handle potential deletion errors
                // Consider logging or showing an error message
            }
    }
}