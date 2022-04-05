package com.example.map1

import android.content.ContentValues
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


private lateinit var mMap: GoogleMap
class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var db:locationDatabase

    private val screenShotButton: AppCompatButton by lazy {
        findViewById<AppCompatButton>(R.id.screenShotButton)
    }

    private val mapFragmentView: ImageView by lazy {
        findViewById(R.id.map)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)




        db= Room.databaseBuilder(applicationContext,locationDatabase::class.java,"locationDatabase").allowMainThreadQueries().build()




        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment

        mapFragment.getMapAsync(this)

        //var v=window.decorView




        screenShotButton.setOnClickListener {
            val bitmap=getScreenShotFromView(mapFragmentView)
            if (bitmap!=null){
                saveMediaToStorage(bitmap)
            }
        }




    }


    ////////////////////////////////캡쳐기능구현

    /*private fun snapshot(googleMap: GoogleMap){

        var mapImageView:ImageView=findViewById(R.id.map)

        screenShotButton.setOnClickListener {
            var callback:GoogleMap.SnapshotReadyCallback=GoogleMap.SnapshotReadyCallback {
                var screenshot: Bitmap? = null
                screenshot = Bitmap.createBitmap(mapFragmentView.measuredWidth, mapFragmentView.measuredHeight, Bitmap.Config.ARGB_8888)
                val canvas = Canvas(screenshot)
                mapFragmentView.draw(canvas)
            }
            googleMap.snapshot(callback)

        }


    }*/

    /*fun snapshot(googleMap: GoogleMap){



        screenShotButton.setOnClickListener {
            var mapImageView:ImageView=findViewById(R.id.map)
            var callback:GoogleMap.SnapshotReadyCallback=GoogleMap.SnapshotReadyCallback {
                fun onSnapshotReady(snapshot:Bitmap){
                    mapImageView.setImageBitmap(snapshot)
                }
            }
            googleMap.snapshot(callback)

        }


    }*///






    /*private fun takePicture(googleMap: GoogleMap) {

        var mapImageView:ImageView=findViewById(R.id.map)

        var bitmapfrommap: Bitmap? = null
        val snapshotReadyCallback : GoogleMap.SnapshotReadyCallback = GoogleMap.SnapshotReadyCallback {
            fun onSnapshotReady(snapshot: Bitmap) {
                bitmapfrommap = snapshot
                mapImageView.setImageBitmap(bitmapfrommap)
                var filename = "export.png"
                var path = getExternalFilesDir(null)
                var fileOut = File(path, filename)
                if (bitmapfrommap != null) {
                    fileOut.writeBitmap(bitmapfrommap!!, Bitmap.CompressFormat.PNG, 85)
                }
            }
        }
        val onMapLoadedCallback : GoogleMap.OnMapLoadedCallback = GoogleMap.OnMapLoadedCallback {
            googleMap.snapshot(snapshotReadyCallback, bitmapfrommap)
        }
        googleMap.setOnMapLoadedCallback(onMapLoadedCallback)
    }*/










    //////////////////////////////////////////


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


    // this method saves the image to gallery
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


        }


        val position:List<locationArray> = db.locationArrayDao().getAll()

        for (i in 0 until position.size){
            val location = LatLng(position[i].latitude,position[i].longitude)
            googleMap.addMarker(MarkerOptions().position(location).title("좌표"). snippet("위도 : ${position[i].latitude}, 경도 : ${position[i].longitude}"))

        }



        var mMap = googleMap
        mMap.mapType=GoogleMap.MAP_TYPE_HYBRID



        mMap.setOnMapLongClickListener { latLng->
            val location = LatLng(latLng.latitude,latLng.longitude)
            var markerOptions= MarkerOptions().position(location).title("좌표"). snippet("위도 : ${latLng.latitude}, 경도 : ${latLng.longitude}")
            googleMap.addMarker(markerOptions)



            var current=locationArray(latLng.latitude,latLng.longitude)

            db.locationArrayDao().insert(current)


            /*
            //저장하는 기능(READ)
            var size=0
            val preferences = getSharedPreferences("0",0)
            val jsonData=preferences.getString("0","")
            val gson=Gson()
            val token:TypeToken<MutableList<locationArray>> = object : TypeToken<MutableList<locationArray>>(){}
            val list:MutableList<locationArray>?=gson.fromJson(jsonData,token.type)

            //저장하는 기능(Write)
            if (list != null) {
                list.add(locationArray(latLng.latitude,latLng.longitude))
            }
            preferences.edit {
                putString("0",gson.toJson(list,token.type))
            }
             */
            //Toast.makeText(this, "${latLng.latitude},${latLng.longitude}",Toast.LENGTH_SHORT).show()
        }


    }







}


