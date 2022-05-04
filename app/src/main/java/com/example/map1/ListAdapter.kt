package com.example.map1

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.map1.databinding.ActivityMainBinding.bind
import kotlinx.android.synthetic.main.marker_cardlist.view.*



class ListAdapter(val itemList:ArrayList<locationArray>): RecyclerView.Adapter<ListAdapter.ViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListAdapter.ViewHolder {

        val view=LayoutInflater.from(parent.context).inflate(R.layout.marker_cardlist,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {

        if (itemList!=null){
            return itemList.size
        }
        return 0
    }

    override fun onBindViewHolder(holder: ListAdapter.ViewHolder, position: Int) {

        val item=itemList[position]

        holder.index.text=itemList[position].id.toString()
        holder.latitude.text=itemList[position].latitude.toString()
        holder.longitude.text=itemList[position].longitude.toString()

        holder.itemView.setOnClickListener {
            setPosition(position)
        }

        holder.deleteButton.setOnClickListener {
            itemList.removeAt(position)
            notifyDataSetChanged()

        }

        holder.itemView.setOnClickListener {
            itemClickListener.onClick(it, position)
        }

        holder.apply {
            //bind(item)
        }



    }

    var mPosition=0

    fun removeItem(position: Int){
        if(position>0){
            itemList.removeAt(position)
            notifyDataSetChanged()
        }
    }

    fun setPosition(position: Int){
        mPosition=position
    }

    fun getPosition():Int{
        return mPosition
    }

    class ViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){
        var view:View=itemView


        val index:TextView=itemView.findViewById(R.id.indexNumber)
        val latitude:TextView=itemView.findViewById(R.id.latitude)
        val longitude:TextView=itemView.findViewById(R.id.longitude)
        val deleteButton:Button=itemView.findViewById(R.id.deleteButton)
    }






    interface OnItemClickListener {
        fun onClick(v: View, position: Int)
    }
    private lateinit var itemClickListener : OnItemClickListener

    fun setItemClickListener(itemClickListener: OnItemClickListener) {
        this.itemClickListener = itemClickListener
    }




}