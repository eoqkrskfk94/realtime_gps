package com.example.realtime_gps

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.IBinder
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.libraries.maps.model.LatLng
import com.google.firebase.firestore.FirebaseFirestore
import java.lang.UnsupportedOperationException


class GpsService : Service() {

    private lateinit var fbFirestore: FirebaseFirestore

    private val locationCallBack: LocationCallback? = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult?) {
            super.onLocationResult(locationResult)

            if(locationResult != null && locationResult.lastLocation != null){
                var latitude = locationResult.lastLocation.latitude
                var longitude = locationResult.lastLocation.longitude


                var obj = HashMap<String, LatLng>()
                obj.put("pickup_gps", LatLng(latitude,longitude))

                fbFirestore.collection("order_gps_tracking_document")
                    .document("order_id")
                    .set(obj)

                //Log.d("LOCATION_UPDATE", "위도: $latitude, 경도: $longitude")
            }

        }

    }

    override fun onBind(p0: Intent?): IBinder? {
        throw UnsupportedOperationException("Not yet implemented")
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        fbFirestore = FirebaseFirestore.getInstance()


        Toast.makeText(this, "Service started by user.", Toast.LENGTH_LONG).show()

        if(intent != null){
            val action = intent.action
            if(action != null){
                if(action == Constants.ACTION_START_LOCATION_SERVICE){
                    println("start")
                    startLocationService()
                }
                else if(action == Constants.ACTION_STOP_LOCATION_SERVICE){
                    println("stop")
                    stopLocationService()
                }
            }
        }


        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
        Toast.makeText(this, "Service destroyed by user.", Toast.LENGTH_LONG).show()

    }



    private fun startLocationService(){
        val channelId = "location_notification_channel"
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val resultIntent = Intent(applicationContext, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(applicationContext,0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val builder = NotificationCompat.Builder(applicationContext, channelId)

        builder.setSmallIcon(R.mipmap.ic_launcher)
        builder.setContentTitle("두딜 앱 실행중입니다.")
        builder.setDefaults(NotificationCompat.DEFAULT_ALL)
        builder.setContentText("실시간 GPS 작동중...")
        builder.setContentIntent(pendingIntent)
        builder.setAutoCancel(false)
        builder.setPriority(NotificationCompat.PRIORITY_MAX)


        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if(notificationManager != null && notificationManager.getNotificationChannel(channelId) == null){
                val notificationChannel = NotificationChannel(channelId, "Location Service", NotificationManager.IMPORTANCE_HIGH)
                notificationChannel.description = "This channel is used by location service"
                notificationManager.createNotificationChannel(notificationChannel)
            }
        }

        val locationRequest = LocationRequest()
        locationRequest.interval = 5000
        locationRequest.fastestInterval = 2000
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
        ) {
            LocationServices.getFusedLocationProviderClient(this)
                .requestLocationUpdates(locationRequest, locationCallBack, Looper.getMainLooper())
            startForeground(Constants.LOCATION_SERVICE_ID, builder.build())
        }

    }


    private fun stopLocationService(){
        LocationServices.getFusedLocationProviderClient(this)
            .removeLocationUpdates(locationCallBack)
        stopForeground(true)
        stopSelf()

    }


}