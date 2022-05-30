package com.example.map1

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.map1.ListAdapter.ViewHolder
import com.example.map1.databinding.ActivityMainBinding.bind
import kotlinx.android.synthetic.main.marker_cardlist.view.*



class ListAdapter(val itemList:ArrayList<locationArray>): RecyclerView.Adapter<ListAdapter.ViewHolder>(){



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListAdapter.ViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.marker_cardlist, parent, false)

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

        holder.index.text=position.toString()
        holder.latitude.text=itemList[position].latitude.toString()
        holder.longitude.text=itemList[position].longitude.toString()


        holder.deleteButton.setOnClickListener {
            itemClickListener.onClick(it, position,item.id)
            itemList.removeAt(position)
            notifyDataSetChanged()

        }

        holder.mapButton.setOnClickListener {

            val context=holder.itemView.context
            val intent = Intent(context, MarkerMap::class.java)
            intent.putExtra("latitude",itemList[position].latitude)
            intent.putExtra("longitude",itemList[position].longitude)
            context.startActivity(intent)

        }



    }



    class ViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){

        val index:TextView=itemView.findViewById(R.id.indexNumber)
        val latitude:TextView=itemView.findViewById(R.id.latitude)
        val longitude:TextView=itemView.findViewById(R.id.longitude)
        val deleteButton:Button=itemView.findViewById(R.id.deleteButton)
        val mapButton:Button=itemView.findViewById(R.id.mapButton)



    }


    interface OnItemClickListener {
        fun onClick(v: View, position: Int,id:Int)
    }
    private lateinit var itemClickListener : OnItemClickListener

    fun setItemClickListener(itemClickListener: OnItemClickListener) {
        this.itemClickListener = itemClickListener
    }




}