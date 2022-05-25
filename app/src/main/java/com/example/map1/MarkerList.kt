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

class MarkerList : AppCompatActivity() {

    var db:locationDatabase?=null

    private val recyclerView:RecyclerView by lazy {
        findViewById(R.id.markerList)
    }



    var itemList= arrayListOf<locationArray>()

    lateinit var mAdapter: ListAdapter



    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_markerlist)

        db= locationDatabase.getInstance(this)



        val position:List<locationArray> = db!!.locationArrayDao().getAll()
        if(position.isNotEmpty()){
            itemList.addAll(position)
        }


        mAdapter=ListAdapter(itemList)

        mAdapter.setItemClickListener(object : ListAdapter.OnItemClickListener{
            override fun onClick(v: View, position:Int,id:Int){
                db?.locationArrayDao()?.deleteData(id)
                mAdapter.notifyDataSetChanged()

            }
        })
        recyclerView.adapter=mAdapter
    }




}