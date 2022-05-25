package com.example.map1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.RecyclerView


class MainActivity : AppCompatActivity() {



    private val mapButton:AppCompatButton by lazy {
        findViewById(R.id.mapButton)
    }

    private val markerButtom:AppCompatButton by lazy {
        findViewById(R.id.markerButton)
    }

    private val markerList:RecyclerView by lazy {
        findViewById(R.id.markerList)
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mapButton.setOnClickListener {
            startActivity(Intent(this,SatelliteMap::class.java))
        }

        markerButtom.setOnClickListener {
            startActivity(Intent(this,com.example.map1.MarkerList::class.java))
        }


    }

}


