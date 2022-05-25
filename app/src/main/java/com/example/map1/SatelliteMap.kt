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

    private val screenShotButton: AppCompatButton by lazy {
        findViewById<AppCompatButton>(R.id.screenShotButton)
    }

    private val mapFragmentView: ImageView by lazy {
        findViewById(R.id.map)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        db= Room.databaseBuilder(applicationContext,locationDatabase::class.java,"locationDatabase").allowMainThreadQueries().build()


        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment

        mapFragment.getMapAsync(this)




    }

    private fun getScreenShotFromView(v: View): Bitmap? {
        // create a bitmap object
        var screenshot: Bitmap? = null
        try {
            // inflate screenshot object
            // with Bitmap.createBitmap it
            // requires three parameters
            // width and height of the view and
            // the background color
            screenshot = Bitmap.createBitmap(v.measuredWidth, v.measuredHeight, Bitmap.Config.ARGB_8888)
            // Now draw this bitmap on a canvas
            val canvas = Canvas(screenshot)
            v.draw(canvas)
        } catch (e: Exception) {
            Log.e("GFG", "Failed to capture screenshot because:" + e.message)
        }
        // return the bitmap
        return screenshot
    }

    private fun saveMediaToStorage(bitmap: Bitmap) {
        // Generating a file name
        val filename = "${System.currentTimeMillis()}.jpg"

        // Output stream
        var fos: OutputStream? = null

        // For devices running android >= Q
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // getting the contentResolver
            this.contentResolver?.also { resolver ->

                // Content resolver will process the contentvalues
                val contentValues = ContentValues().apply {

                    // putting file information in content values
                    put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                    put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg")
                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
                }

                // Inserting the contentValues to
                // contentResolver and getting the Uri
                val imageUri: Uri? = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

                // Opening an outputstream with the Uri that we got
                fos = imageUri?.let { resolver.openOutputStream(it) }
            }
        } else {
            // These for devices running on android < Q
            val imagesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
            val image = File(imagesDir, filename)
            fos = FileOutputStream(image)
        }

        fos?.use {
            // Finally writing the bitmap to the output stream that we opened
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
            Toast.makeText(this , "Captured View and saved to Gallery" , Toast.LENGTH_SHORT).show()
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {


        screenShotButton.setOnClickListener {
            googleMap.snapshot {
                it?.let {
                    saveMediaToStorage(it)
                }
            }
        }


        val position:List<locationArray> = db.locationArrayDao().getAll()

        for (i in 0 until position.size){
            val location = LatLng(position[i].latitude,position[i].longitude)
            googleMap.addMarker(MarkerOptions().position(location).title("좌표"). snippet("위도 : ${position[i].latitude}, 경도 : ${position[i].longitude}"))

        }



        var mMap = googleMap
        mMap.mapType= GoogleMap.MAP_TYPE_HYBRID



        mMap.moveCamera(CameraUpdateFactory.newLatLng(LatLng(37.52,127.0)))
        mMap.animateCamera(CameraUpdateFactory.zoomTo(10.0F))


        mMap.setOnMapLongClickListener { latLng->
            val location = LatLng(latLng.latitude,latLng.longitude)
            var markerOptions= MarkerOptions().position(location).title("좌표"). snippet("위도 : ${latLng.latitude}, 경도 : ${latLng.longitude}")
            googleMap.addMarker(markerOptions)



            var current=locationArray(latLng.latitude,latLng.longitude)

            db.locationArrayDao().insert(current)

        }


    }







}