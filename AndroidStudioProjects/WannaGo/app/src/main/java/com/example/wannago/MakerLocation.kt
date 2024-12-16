package com.example.wannago

data class MarkerLocation(
    val id: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val address: String = "Unknown Location"
) {
    // Required no-argument constructor for Firestore
    constructor() : this("", 0.0, 0.0, "Unknown Location")
}