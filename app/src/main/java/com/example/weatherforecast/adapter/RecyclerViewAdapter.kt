package com.example.weatherforecast.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherforecast.R
import com.example.weatherforecast.model.ResponseWeather
import com.example.weatherforecast.model.TemaratureModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.roundToInt

class RecyclerViewAdapter(private val list: ArrayList<TemaratureModel>):RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context).inflate(R.layout.rows_item,parent,false)
        return MyViewHolder(inflater)
    }

    //setting data values here
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = list[position]
        holder.date.text = when(position){
            0-> "Today"
            1-> "Tomorrow"
            else -> item.time?:""
        }
        holder.temp.text = "${item.temperature2m?.roundToInt().toString()}\u00B0"

    }

    override fun getItemCount()= list.size

    class MyViewHolder(view: View):RecyclerView.ViewHolder(view) {
        val date:TextView = itemView.findViewById(R.id.tvDate)
        val temp:TextView = itemView.findViewById(R.id.tvTemp)
    }




}