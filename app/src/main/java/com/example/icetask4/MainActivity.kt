package com.example.icetask4

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import android.location.Geocoder
import java.io.IOException
import java.util.Locale

class MainActivity : AppCompatActivity(), LocationListener { // Add LocationListener for real-time updates

    private lateinit var locationManager: LocationManager
    private lateinit var tvOutput: TextView
    private lateinit var locationTextView: TextView
    private val locationPermissionCode = 1

    // Declare FusedLocationProviderClient and currentLocation
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var currentLocation: Location? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Initialize FusedLocationProviderClient
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // Set up button for getting location
        val button: Button = findViewById(R.id.btnGetLocatioin)
        button.setOnClickListener {
            getCurrentLocation() // Fetch the current location on button click
        }

        // Set up button for showing map
        val showMapButton: Button = findViewById(R.id.btnShowMap)
        showMapButton.setOnClickListener {
            if (currentLocation != null) { // Check if location is available
                val intent = Intent(this, MapActivity::class.java)
                intent.putExtra("location", currentLocation)
                startActivity(intent)
            } else {
                tvOutput.text = "Location not available yet!"
            }
        }
    }

    // Function to get the current location using FusedLocationProviderClient
    private fun getCurrentLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            // Request permission if not already granted
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                locationPermissionCode
            )
        } else {
            // Fetch the last known location
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    if (location != null) {
                        currentLocation = location
                        // Update the UI with the current location
                        onLocationChanged(location)
                    } else {
                        tvOutput.text = "Location not found. Please try again."
                    }
                }
                .addOnFailureListener {
                    tvOutput.text = "Failed to get location."
                }
        }
    }

    // Request location updates with LocationManager
    private fun getLocation() {
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            // Request permission if not already granted
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), locationPermissionCode
            )
        } else {
            // If permission is granted, request location updates
            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                5000, 5f, this // Location updates every 5 seconds or 5 meters
            )
        }
    }

    override fun onLocationChanged(location: Location) {
        // When the location changes, update UI
        tvOutput = findViewById(R.id.lblOutput)
        tvOutput.text = "Latitude: ${location.latitude}, \nLongitude: ${location.longitude}"

        // Fetch the address from location
        getAddressFromLocation(location)

        // Fetch nearby places
        fetchNearbyPlaces(location.latitude, location.longitude)
    }

    // Get address from the location coordinates
    private fun getAddressFromLocation(location: Location) {
        val geocoder = Geocoder(this, Locale.getDefault())
        locationTextView = findViewById(R.id.textLocation)
        try {
            val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
            if (addresses != null && addresses.isNotEmpty()) {
                val address = addresses[0]
                val addressLine = address.getAddressLine(0)
                locationTextView.text = "Address: $addressLine"
            } else {
                locationTextView.text = "Unable to get address"
            }
        } catch (e: IOException) {
            e.printStackTrace()
            locationTextView.text = "Error getting address"
        }
    }

    // Placeholder for fetching nearby places using the latitude and longitude
    private fun fetchNearbyPlaces(latitude: Double, longitude: Double) {
        // This is where you will make the API call to fetch nearby places
        // Example: Use Google Places API or an open-source alternative
        // Perform network operation to get nearby places and display them
    }

    // Requesting location permission result handler
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == locationPermissionCode && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // If permission is granted, fetch location
            getCurrentLocation()
        } else {
            // Handle the case where permission is denied
            tvOutput.text = "Permission Denied"
        }
    }
}
