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
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.CancellationTokenSource
import java.util.Locale

private const val TAG = "PlacesFragment"
private const val DEFAULT_ZOOM = 15f

class PlacesFragment : Fragment(), OnMapReadyCallback {

    private var _binding: FragmentPlacesBinding? = null
    // Non-null getter that requires the binding to exist
    private val binding get() = _binding!!

    private val viewModel: PlacesViewModel by viewModels()
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var geocoder: Geocoder
    private var locationPermissionGranted = false
    private var googleMap: GoogleMap? = null

    private val defaultLocation = LatLng(-33.8523341, 151.2106085)
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

        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireContext())
        geocoder = Geocoder(requireContext(), Locale.getDefault())

        setupMapView(savedInstanceState)
        setupRecyclerView()
        checkAndRequestLocationPermissions()

        return binding.root
    }

    private fun setupMapView(savedInstanceState: Bundle?) {
        binding.mapView.onCreate(savedInstanceState)
        binding.mapView.getMapAsync(this)
    }

    private fun setupRecyclerView() {
        binding.markersRecyclerView.layoutManager = LinearLayoutManager(context)

        viewModel.markers.observe(viewLifecycleOwner) { markers ->
            // Clear existing markers on the map
            googleMap?.clear()

            // Add markers to the map
            markers.forEach { markerLocation ->
                val latLng = LatLng(markerLocation.latitude, markerLocation.longitude)
                googleMap?.addMarker(
                    MarkerOptions()
                        .position(latLng)
                        .title(markerLocation.address)
                )
            }

            // Set up RecyclerView adapter with delete functionality
            binding.markersRecyclerView.adapter = MarkerAdapter(markers) { marker ->
                viewModel.deleteMarker(marker)
            }
        }
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
        googleMap?.let { map ->
            if (locationPermissionGranted) {
                fusedLocationProviderClient.getCurrentLocation(
                    Priority.PRIORITY_HIGH_ACCURACY,
                    CancellationTokenSource().token
                ).addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful) {
                        val location = task.result
                        if (location != null) {
                            map.moveCamera(
                                CameraUpdateFactory.newLatLngZoom(
                                    LatLng(location.latitude, location.longitude),
                                    DEFAULT_ZOOM
                                )
                            )
                        } else {
                            Log.d(TAG, "Current location is null. Using defaults.")
                            map.moveCamera(
                                CameraUpdateFactory.newLatLngZoom(defaultLocation, DEFAULT_ZOOM)
                            )
                        }
                    } else {
                        Log.d(TAG, "Current location is null. Using defaults.")
                        map.moveCamera(
                            CameraUpdateFactory.newLatLngZoom(defaultLocation, DEFAULT_ZOOM)
                        )
                    }
                }
            }
        }
    }

    private fun updateMapUI() {
        googleMap?.let { map ->
            try {
                if (locationPermissionGranted) {
                    map.isMyLocationEnabled = true
                    map.uiSettings.isMyLocationButtonEnabled = true
                } else {
                    map.isMyLocationEnabled = false
                    map.uiSettings.isMyLocationButtonEnabled = false
                }
            } catch (e: SecurityException) {
                Log.e(TAG, "Location permission error", e)
            }
        }
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        updateMapUI()

        map.setOnMapClickListener { latLng ->
            try {
                val addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
                val address = addresses?.firstOrNull()?.getAddressLine(0) ?: "Unknown Location"

                val markerLocation = MarkerLocation(
                    latitude = latLng.latitude,
                    longitude = latLng.longitude,
                    address = address
                )

                viewModel.addMarker(markerLocation)
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, DEFAULT_ZOOM))
            } catch (e: Exception) {
                Log.e(TAG, "Error adding marker", e)
            }
        }

        getDeviceLocation()
    }

    // Lifecycle methods for MapView
    override fun onResume() {
        super.onResume()
        binding.mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        binding.mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.mapView.onLowMemory()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}