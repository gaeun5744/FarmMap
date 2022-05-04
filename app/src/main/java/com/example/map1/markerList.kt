package com.example.map1

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil.setContentView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.map1.databinding.ActivityMainBinding
import com.google.android.gms.common.util.DataUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class markerList : AppCompatActivity() {

    var db:locationDatabase?=null

    val recyclerView:RecyclerView by lazy {
        findViewById(R.id.markerList)
    }



    private lateinit var binding: ActivityMainBinding
    var itemList= arrayListOf<locationArray>()

    lateinit var mAdapter: ListAdapter



    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_markerlist)

        db= locationDatabase.getInstance(this)


        //db= Room.databaseBuilder(applicationContext,locationDatabase::class.java,"locationDatabase").allowMainThreadQueries().build()
        val position:List<locationArray> = db!!.locationArrayDao().getAll()
        if(position.isNotEmpty()){
            itemList.addAll(position)
        }
        //findViewById<RecyclerView>(R.id.markerList).adapter=ListAdapter(position as ArrayList<locationArray>)

        val adapter=ListAdapter(itemList)

        adapter.setItemClickListener(object : ListAdapter.OnItemClickListener{
            override fun onClick(v: View, position:Int){
                db?.locationArrayDao()?.deleteData(position)
                itemList.removeAt(position)
                adapter.notifyDataSetChanged()

                Log.d("markerList","리스트!!: ${db!!.locationArrayDao().getAll()}")


            }
        })

        recyclerView.adapter=adapter






        /*for (i in 0 until position.size){
            itemList.add(ListItem(i,position[i].latitude,position[i].longitude))
        }*/

       //var deleteButton:Button = findViewById<Button>(R.id.markerDelete)



        findViewById<RecyclerView>(R.id.markerList).adapter=ListAdapter(position as ArrayList<locationArray>)

        /*deleteButton.setOnClickListener {
            mAdapter.removeItem(mAdpater.getPosition())
        }*/


    }




}