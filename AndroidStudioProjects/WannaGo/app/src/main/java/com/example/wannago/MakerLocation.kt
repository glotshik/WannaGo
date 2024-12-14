package com.example.wannago

data class MarkerLocation(
    val id: String = "", // Add an ID for Firestore document
    val latitude: Double,
    val longitude: Double,
    val address: String,
    val timestamp: Long = System.currentTimeMillis()
)