
package com.example.realtime_gps

import android.Manifest
import android.app.ActivityManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.*
import com.google.android.libraries.maps.model.LatLng


class MainActivity : AppCompatActivity() {

    private val REQUEST_FINE_LOCATION = 1000
    private val NOTIFICATION_ID = 1001
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    //private lateinit var locationCallback: MyLocationCallBack

    private lateinit var notificationManager: NotificationManagerCompat



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getPermission()
        //locationInit()

        val btnStart: Button = findViewById(R.id.btn_start)
        val btnStop: Button = findViewById(R.id.btn_stop)








        btnStart.setOnClickListener {
            startLocationService()
        }

        btnStop.setOnClickListener {
            stopLocationService()
        }



    }

//    private fun isLocationServiceRunning(){
//        val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
//        if(activityManager != null){
//            for (service in activityManager.getRunningServices(Int.MAX_VALUE)){
//                if(LocationService.class)
//            }
//        }
//    }

    private fun startLocationService(){
        val intent = Intent(applicationContext, GpsService::class.java)
        intent.action = Constants.ACTION_START_LOCATION_SERVICE
        startService(intent)
        Toast.makeText(this, "Location service started", Toast.LENGTH_SHORT).show()
    }

    private fun stopLocationService(){
        val intent = Intent(applicationContext, GpsService::class.java)
        intent.action = Constants.ACTION_STOP_LOCATION_SERVICE
        startService(intent)
        Toast.makeText(this, "Location service stopped", Toast.LENGTH_SHORT).show()
    }






//    fun locationInit(){
//        fusedLocationProviderClient = FusedLocationProviderClient(this)
//
//        locationRequest = LocationRequest()
//
//        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
//        locationRequest.interval = 5000
//        locationRequest.fastestInterval = 1000
//
//        locationCallback = MyLocationCallBack()
//
//    }
//
//    fun addLocationListener(){
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
//                this,
//                Manifest.permission.ACCESS_COARSE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED) {
//            return
//        }
//        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null)
//    }
//
//    inner class MyLocationCallBack: LocationCallback(){
//        override fun onLocationResult(locationResult: LocationResult?) {
//            super.onLocationResult(locationResult)
//
//            val location = locationResult?.lastLocation
//
//            location?.run{
//                val latLng = LatLng(latitude, longitude)
//
//                Log.d("MainActivity", "위도: $latitude, 경도: $longitude")
//            }
//        }
//
//    }

    override fun onResume() {
        super.onResume()
        //addLocationListener()
    }


    private fun getPermission(){
        if(ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED){

            if(ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                )){


            }
            else{
                ActivityCompat.requestPermissions(
                    this@MainActivity,
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                    REQUEST_FINE_LOCATION
                )
                ActivityCompat.requestPermissions(
                    this@MainActivity,
                    arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION),
                    REQUEST_FINE_LOCATION
                )
            }
        }
    }







}