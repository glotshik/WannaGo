// PlacesFragment.kt
package com.example.wannago

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.wannago.databinding.FragmentPlacesBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.CancellationTokenSource
import java.util.Locale

private const val TAG = "PlacesFragment"
private const val DEFAULT_ZOOM = 15f

class PlacesFragment : Fragment(), OnMapReadyCallback {
    private var _binding: FragmentPlacesBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PlacesViewModel by viewModels()
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var geocoder: Geocoder
    private var locationPermissionGranted = false
    private var map: GoogleMap? = null
    private lateinit var adapter: MarkerAdapter

    private val defaultLocation = LatLng(47.6062, -122.3321) // Seattle coordinates

    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        locationPermissionGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true
        updateMapUI()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlacesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize dependencies
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())
        geocoder = Geocoder(requireContext(), Locale.getDefault())

        // Initialize RecyclerView
        adapter = MarkerAdapter { marker ->
            viewModel.deleteMarker(marker)
        }
        binding.markersRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.markersRecyclerView.adapter = adapter

        // Initialize Map
        val mapFragment = childFragmentManager
            .findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)

        // Observe markers
        viewModel.markers.observe(viewLifecycleOwner) { markers ->
            adapter.submitList(markers)
            updateMapMarkers(markers)
        }

        // Request permissions
        checkAndRequestLocationPermissions()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        updateMapUI()

        map?.setOnMapClickListener { latLng ->
            try {
                val addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
                val address = addresses?.firstOrNull()?.getAddressLine(0) ?: "Unknown Location"

                val marker = MarkerLocation(
                    id = "", // Firestore will generate this
                    latitude = latLng.latitude,
                    longitude = latLng.longitude,
                    address = address
                )
                viewModel.addMarker(marker)
            } catch (e: Exception) {
                Log.e(TAG, "Geocoding error", e)
                // Add marker without address if geocoding fails
                val marker = MarkerLocation(
                    id = "",
                    latitude = latLng.latitude,
                    longitude = latLng.longitude,
                    address = "Unknown Location"
                )
                viewModel.addMarker(marker)
            }
        }

        // Get device location after map is ready
        getDeviceLocation()
    }

    private fun checkAndRequestLocationPermissions() {
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
                locationPermissionGranted = true
                updateMapUI()
            }
            else -> {
                locationPermissionRequest.launch(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                )
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun getDeviceLocation() {
        try {
            if (locationPermissionGranted) {
                fusedLocationProviderClient.getCurrentLocation(
                    Priority.PRIORITY_HIGH_ACCURACY,
                    CancellationTokenSource().token
                ).addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful && task.result != null) {
                        val location = task.result
                        map?.moveCamera(
                            CameraUpdateFactory.newLatLngZoom(
                                LatLng(location.latitude, location.longitude),
                                DEFAULT_ZOOM
                            )
                        )
                    } else {
                        Log.d(TAG, "Current location is null. Using defaults.")
                        map?.moveCamera(
                            CameraUpdateFactory.newLatLngZoom(defaultLocation, DEFAULT_ZOOM)
                        )
                    }
                }
            }
        } catch (e: SecurityException) {
            Log.e(TAG, "Exception: ${e.message}", e)
        }
    }

    private fun updateMapUI() {
        try {
            map?.let { googleMap ->
                if (locationPermissionGranted) {
                    googleMap.isMyLocationEnabled = true
                    googleMap.uiSettings.isMyLocationButtonEnabled = true
                } else {
                    googleMap.isMyLocationEnabled = false
                    googleMap.uiSettings.isMyLocationButtonEnabled = false
                }
            }
        } catch (e: SecurityException) {
            Log.e(TAG, "Exception: ${e.message}", e)
        }
    }

    private fun updateMapMarkers(markers: List<MarkerLocation>) {
        map?.clear()
        markers.forEach { marker ->
            map?.addMarker(
                MarkerOptions()
                    .position(LatLng(marker.latitude, marker.longitude))
                    .title(marker.address)
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}