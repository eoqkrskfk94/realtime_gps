
package com.example.realtime_gps

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.libraries.maps.model.LatLng
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.collections.HashMap


class MainActivity : AppCompatActivity() {

    private val REQUEST_FINE_LOCATION = 1000
    private lateinit var fbFirestore: FirebaseFirestore


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fbFirestore = FirebaseFirestore.getInstance()

        val fb = fbFirestore.collection("order_gps_tracking_document").document("order_id")
        fb.addSnapshotListener { value, error ->
            if(error != null){
                Log.w("GPS", "Listen failed.", error)
                return@addSnapshotListener
            }
            if (value != null && value.exists()) {
                Log.d("GPS", "Current data: ${value.data}")
            } else {
                Log.d("GPS", "Current data: null")
            }
        }


        getPermission()

        val btnStart: Button = findViewById(R.id.btn_start)
        val btnStop: Button = findViewById(R.id.btn_stop)




        btnStart.setOnClickListener {
            startLocationService()
        }

        btnStop.setOnClickListener {
            stopLocationService()
        }



    }



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





    override fun onResume() {
        super.onResume()
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