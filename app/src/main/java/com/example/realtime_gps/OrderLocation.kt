package com.example.realtime_gps

import com.google.android.libraries.maps.model.LatLng

data class OrderLocation(var orderOd: String? = null,
                         var pickup_gps: LatLng? = null,
                         var delivery_gps: String? = null) {
}