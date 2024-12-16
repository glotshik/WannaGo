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
import android.widget.Toast
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
        locationPermissionGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        updateMapUI()
        getDeviceLocation()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlacesBinding.inflate(inflater, container, false)
        binding.mapView.onCreate(savedInstanceState)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize dependencies
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())
        geocoder = Geocoder(requireContext(), Locale.getDefault())

        // Initialize RecyclerView
        adapter = MarkerAdapter(
            onDeleteClick = { marker ->
                viewModel.deleteMarker(marker)
            },
            onItemClick = { marker ->
                map?.animateCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        LatLng(marker.latitude, marker.longitude),
                        DEFAULT_ZOOM
                    )
                )
            }
        )
        binding.markersRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.markersRecyclerView.adapter = adapter

        // Initialize Map
        binding.mapView.getMapAsync(this)

        // Observe markers
        viewModel.markers.observe(viewLifecycleOwner) { markers ->
            adapter.submitList(markers)
            updateMapMarkers(markers)
        }

        // Request permissions
        checkAndRequestLocationPermissions()
    }

    // In your PlacesFragment.kt, update the onMapReady function:

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        updateMapUI()

        map?.setOnMapClickListener { latLng ->
            val marker = try {
                val addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
                val address = addresses?.firstOrNull()?.getAddressLine(0) ?: "Unknown Location"

                MarkerLocation(
                    id = "", // Firestore will generate this
                    latitude = latLng.latitude,
                    longitude = latLng.longitude,
                    address = address
                )
            } catch (e: Exception) {
                Log.e(TAG, "Error getting address, using default", e)
                MarkerLocation(
                    id = "",
                    latitude = latLng.latitude,
                    longitude = latLng.longitude,
                    address = "Unknown Location"
                )
            }

            try {
                viewModel.addMarker(marker)
                // Show feedback to user
                Toast.makeText(
                    context,
                    "Location marked: ${marker.address}",
                    Toast.LENGTH_SHORT
                ).show()
            } catch (e: Exception) {
                Log.e(TAG, "Error adding marker", e)
                Toast.makeText(
                    context,
                    "Error saving location",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        // Enable zoom controls
        map?.uiSettings?.apply {
            isZoomControlsEnabled = true
            isMyLocationButtonEnabled = true
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
                getDeviceLocation()
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
                Log.d(TAG, "Getting device location")
                fusedLocationProviderClient.getCurrentLocation(
                    Priority.PRIORITY_HIGH_ACCURACY,
                    CancellationTokenSource().token
                ).addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful && task.result != null) {
                        val location = task.result
                        Log.d(
                            TAG,
                            "Got device location: ${location.latitude}, ${location.longitude}"
                        )
                        map?.moveCamera(
                            CameraUpdateFactory.newLatLngZoom(
                                LatLng(location.latitude, location.longitude),
                                DEFAULT_ZOOM
                            )
                        )
                    } else {
                        Log.d(TAG, "Using default location")
                        map?.moveCamera(
                            CameraUpdateFactory.newLatLngZoom(defaultLocation, DEFAULT_ZOOM)
                        )
                    }
                }
            } else {
                Log.d(TAG, "Location permission not granted")
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
        Log.d(TAG, "Updated ${markers.size} markers on map")
    }

    // MapView lifecycle methods
    override fun onStart() {
        super.onStart()
        _binding?.mapView?.onStart()
    }

    override fun onResume() {
        super.onResume()
        _binding?.mapView?.onResume()
    }

    override fun onPause() {
        super.onPause()
        _binding?.mapView?.onPause()
    }

    override fun onStop() {
        super.onStop()
        _binding?.mapView?.onStop()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        _binding?.mapView?.onSaveInstanceState(outState)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        _binding?.mapView?.onLowMemory()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding?.mapView?.onDestroy()
        _binding = null
    }
}