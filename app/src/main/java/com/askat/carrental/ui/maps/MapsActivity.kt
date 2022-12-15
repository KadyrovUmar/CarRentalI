package com.askat.carrental.ui.maps

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.*
import android.os.Bundle
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import java.util.*

class MapsActivity : AppCompatActivity(), OnMapReadyCallback, LocationListener {

    private lateinit var mMap: GoogleMap
    private var locationManager: LocationManager? = null
    private var currentAddress: String? = null
    var fusedLocationProviderClient: FusedLocationProviderClient? = null
    var locationRequest: LocationRequest? = null
    var locationCallback: LocationCallback? = null
    var userLocation: Location? = null

    private var user:Customer? = null
    private val binding: ActivityMapsBinding by lazy {
        ActivityMapsBinding.inflate(layoutInflater)
    }
    private val viewModel:MapsActivityVM by lazy {
        MapsActivityVM()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.getTaxi.setOnClickListener {
            getTaxi()
        }
        viewModel.orders.observe(this){
            if (it.isSuccessful){
                Toast.makeText(this@MapsActivity,"Successful",Toast.LENGTH_SHORT).show()
            }
        }

        user = SharedPrefs.getCurrentUser(this@MapsActivity)!!
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        buildLocationRequest()

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    private fun getTaxi(){
        val geoPoint = GeoPoint(
            __type = "GeoPoint",
            longitude = userLocation!!.longitude,
            latitude = userLocation!!.latitude
        )
        val order = Order(
            location = geoPoint,
            address = currentAddress!!,
            customerName = user!!.username,
            phoneNumber = user!!.phonenumber,
            isTaken = false,
            driverName = null
        )
        viewModel.createOrder(order)
    }

    private fun buildLocationRequest() {
        locationRequest = LocationRequest()
        locationRequest?.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest?.interval = 100
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.mapType = GoogleMap.MAP_TYPE_NORMAL

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                1
            )
            return
        }
        mMap.isMyLocationEnabled = true
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                super.onLocationResult(result)

                for (location in result.locations) {
                    userLocation = location
                    mMap.clear()
                    val position = LatLng(location.latitude, location.longitude)
                    mMap.addMarker(
                        MarkerOptions().position(position).title("You are here!")
                    )
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(position, 13f))
                    val geocoder = Geocoder(this@MapsActivity, Locale.getDefault())
                    val addressList =
                        geocoder.getFromLocation(location.latitude, location.latitude, 1)
                    currentAddress = addressList!![0].getAddressLine(0) as String
                    binding.locationText.setText(currentAddress.toString())
                }
            }
        }
        fusedLocationProviderClient?.requestLocationUpdates(
            locationRequest!!, locationCallback!!,
            Looper.getMainLooper()
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
                locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, 1000, 5f, this
                )
            }
        }
    }

    override fun onLocationChanged(location: Location){
        userLocation = location
        mMap.clear()
        val position = LatLng(location.latitude, location.longitude)
        mMap.addMarker(
            MarkerOptions().position(position).title("You are here!")
        )
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(position, 13f))
        val geocoder = Geocoder(this@MapsActivity, Locale.getDefault())
        try {
            val address = geocoder.getFromLocation(location.latitude, location.longitude,1)
            currentAddress = address!![0].getAddressLine(0) as String
            binding.locationText.setText(currentAddress.toString())
        }catch (e:Exception){
            Toast.makeText(this@MapsActivity, e.message.toString(), Toast.LENGTH_SHORT).show()
        }
    }
}