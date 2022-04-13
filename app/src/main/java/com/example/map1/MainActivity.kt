package com.example.map1

import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.room.Room
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream



class MainActivity : AppCompatActivity() {

    private val mapButton:AppCompatButton by lazy {
        findViewById(R.id.mapButton)
    }

    private val markerButtom:AppCompatButton by lazy {
        findViewById(R.id.markerButton)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mapButton.setOnClickListener {
            startActivity(Intent(this,satelliteMap::class.java))
        }

        markerButtom.setOnClickListener {

        }


    }

}


