package com.example.map1

import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.room.Room
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

class SatelliteMap: AppCompatActivity(), OnMapReadyCallback {

    private lateinit var db:locationDatabase


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        db= Room.databaseBuilder(applicationContext,locationDatabase::class.java,"locationDatabase").allowMainThreadQueries().build()


        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment

        mapFragment.getMapAsync(this)




    }





    override fun onMapReady(googleMap: GoogleMap) {


        val position:List<locationArray> = db.locationArrayDao().getAll()

        for (i in 0 until position.size){
            val location = LatLng(position[i].latitude,position[i].longitude)
            googleMap.addMarker(MarkerOptions().position(location).title("좌표"). snippet("위도 : ${position[i].latitude}, 경도 : ${position[i].longitude}"))

        }



        var mMap = googleMap
        mMap.mapType= GoogleMap.MAP_TYPE_HYBRID



        mMap.setOnMapLongClickListener { latLng->
            val location = LatLng(latLng.latitude,latLng.longitude)
            var markerOptions= MarkerOptions().position(location).title("좌표"). snippet("위도 : ${latLng.latitude}, 경도 : ${latLng.longitude}")
            googleMap.addMarker(markerOptions)



            var current=locationArray(latLng.latitude,latLng.longitude)

            db.locationArrayDao().insert(current)

        }


    }







}