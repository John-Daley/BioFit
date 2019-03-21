package com.example.delta.fireapp.RunTrack

import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.support.v4.app.ActivityCompat
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.example.delta.fireapp.R
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import kotlinx.android.synthetic.main.activity_map_running.*
import org.w3c.dom.Text

class MapRunningActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var timer: CountDownTimer
    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var currentFusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var lastLocation: Location
    private lateinit var whereIAmAtNow: Location
    private var secondsRemaining: Long = 0
    private lateinit var locationCallback: LocationCallback
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map_running)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val runTimer: Int = intent.getIntExtra("time", 0)
        secondsRemaining = runTimer.toLong() * 60
        startTimer()
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.isZoomControlsEnabled = true
        setUpPermissionsOnMap()
    }

    private fun setUpPermissionsOnMap() {
        if (ActivityCompat.checkSelfPermission(this,
                        android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                    LOCATION_PERMISSION_REQUEST_CODE)
            return
        }
        mMap.isMyLocationEnabled = true
        fusedLocationClient.lastLocation.addOnSuccessListener(this) { location ->
            if (location != null) {
                lastLocation = location
                val currentLatLang = LatLng(location.latitude, location.longitude)
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLang, 12f))
            }
        }
    }

    fun showTimeRemainingText() {
        val runTimer: Int = intent.getIntExtra("time", 0)
        var showRunningTime: TextView = findViewById(R.id.runTimerTextView)
        showRunningTime.text = runTimer.toString()

    }

    private fun updateCountdownUI() {

        val minutesUntilFinished = secondsRemaining / 60
        val secondsInMinuteUntilFinished = secondsRemaining - minutesUntilFinished * 60
        val secondsStr = secondsInMinuteUntilFinished.toString()
        runTimerTextView.text = "$minutesUntilFinished:${if (secondsStr.length == 2) secondsStr else "0" + secondsStr}"

    }

    private fun startTimer() {


        timer = object : CountDownTimer(secondsRemaining * 1000, 1000) {
            override fun onFinish() = onTimerFinished()

            override fun onTick(millisUntilFinished: Long) {
                secondsRemaining = millisUntilFinished / 1000
                updateCountdownUI()
            }
        }.start()
    }

    fun onTimerFinished() {
        runTimerTextView.text = " Good Job "
    }

    fun calculateReturnTime(view: View) {
        val textForToast = "time to head home!"
        val duration = Toast.LENGTH_SHORT

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult ?: return
                for (location in locationResult.locations) {

                }
            }
        }

        val homeLocation: Location = Location("yea")
        homeLocation.latitude = 56.045763 //56.052398
        homeLocation.longitude = 14.151597 //14.146396
        if (ActivityCompat.checkSelfPermission(this,
                        android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                    LOCATION_PERMISSION_REQUEST_CODE)
            return
        }

        val locationRequest = LocationRequest.create()?.apply {
            interval = 10000
            fastestInterval = 5000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
        locationRequest
        fusedLocationClient.lastLocation.addOnSuccessListener(this) { location ->
            if (location != null) {
                whereIAmAtNow = location

                val distanceHome = whereIAmAtNow.distanceTo(homeLocation)
                val distanceHomeText: TextView = findViewById(R.id.distanceHomeTextView)
                distanceHomeText.text = "$distanceHome"
                val currentSpeed = whereIAmAtNow.speed
                val currentSpeedText: TextView = findViewById(R.id.speedTestTextView)
                currentSpeedText.text = currentSpeed.toString()
                val timeUntilHomeText: TextView = findViewById(R.id.timeUntilHomeTextView)
                var timeUntilHome = distanceHome / currentSpeed
                timeUntilHomeText.text = timeUntilHome.toString()
                val timeToGoHomeText: TextView = findViewById(R.id.testGoHome)
                if (timeUntilHome.toLong() <= secondsRemaining) {
                    val toast = Toast.makeText(applicationContext, textForToast, duration)
                    toast.show()
                    timeToGoHomeText.text = " Time to go home ! "
                }
            }
        }

    }

}
