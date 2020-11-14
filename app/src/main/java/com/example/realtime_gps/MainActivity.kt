
package com.example.realtime_gps

import android.Manifest
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
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.libraries.maps.model.LatLng


class MainActivity : AppCompatActivity() {

    private val REQUEST_FINE_LOCATION = 1000
    private val NOTIFICATION_ID = 1001
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: MyLocationCallBack


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getPermission()


        locationInit()

        val btn: Button = findViewById(R.id.btn)

        btn.setOnClickListener { createNotificationChannel() }



    }


    fun locationInit(){
        fusedLocationProviderClient = FusedLocationProviderClient(this)

        locationRequest = LocationRequest()

        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 5000
        locationRequest.fastestInterval = 1000

        locationCallback = MyLocationCallBack()

    }

    fun addLocationListener(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null)
    }

    inner class MyLocationCallBack: LocationCallback(){
        override fun onLocationResult(locationResult: LocationResult?) {
            super.onLocationResult(locationResult)

            val location = locationResult?.lastLocation

            location?.run{
                val latLng = LatLng(latitude, longitude)

                Log.d("MainActivity", "위도: $latitude, 경도: $longitude")
            }
        }

    }

    override fun onResume() {
        super.onResume()

        addLocationListener()
    }


    private fun getPermission(){
        if(ContextCompat.checkSelfPermission(this,
                        android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){

            if(ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_FINE_LOCATION)){


            }
            else{
                ActivityCompat.requestPermissions(this@MainActivity, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_FINE_LOCATION)
                ActivityCompat.requestPermissions(this@MainActivity, arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION), REQUEST_FINE_LOCATION)
            }
        }
    }


    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "$packageName-${getString(R.string.app_name)}"
            val title = "Android Developer"
            val content = "Notifications in Android P"

//            val channel = NotificationChannel(channelId, name, importance)
//            channel.description = description
//            channel.setShowBadge(showBadge)


            val intent = Intent(baseContext, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            val pendingIntent = PendingIntent.getActivity(baseContext, 0,
                    intent, PendingIntent.FLAG_UPDATE_CURRENT)    // 3

            val builder = NotificationCompat.Builder(this, channelId)  // 4
            builder.setSmallIcon(R.drawable.maps_sv_error_icon)    // 5
            builder.setContentTitle(title)    // 6
            builder.setContentText(content)    // 7
            builder.priority = NotificationCompat.PRIORITY_HIGH    // 8
            builder.setAutoCancel(true)   // 9
            builder.setContentIntent(pendingIntent)   // 10




            val notificationManager = NotificationManagerCompat.from(this)
            notificationManager.notify(NOTIFICATION_ID, builder.build())
        }
    }




}